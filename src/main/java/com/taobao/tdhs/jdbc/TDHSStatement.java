/*
 * Copyright(C) 2011-2012 Alibaba Group Holding Limited
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *
 *  Authors:
 *    wentong <wentong@taobao.com>
 */

package com.taobao.tdhs.jdbc;

import com.taobao.tdhs.client.TDHSClient;
import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.easy.Insert;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.easy.Where;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum;
import com.taobao.tdhs.client.statement.BatchStatement;
import com.taobao.tdhs.jdbc.exception.TDHSSQLException;
import com.taobao.tdhs.jdbc.sqlparser.*;
import com.taobao.tdhs.jdbc.util.ConvertUtil;
import com.taobao.tdhs.jdbc.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-8 上午9:12
 */
public class TDHSStatement implements Statement {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Connection connection;

    private final TDHSClient client;

    private final String db;

    private boolean close = false;

    private ResultSet currentResultSet = null;

    private int updateCount = -1;

    private BatchStatement batchStatement = null;

    public TDHSStatement(Connection connection, TDHSClient client, String db) {
        this.connection = connection;
        this.client = client;
        this.db = db;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        execute(sql);
        if (currentResultSet == null) {
            throw new TDHSSQLException("None Resultset!", sql);
        }
        return currentResultSet;
    }

    private void doSelectOrCount(com.taobao.tdhs.client.statement.Statement s, ParseSQL parseSQL, String tableName,
                                 String dbName) throws SQLException {
        Query query = preprocessGet(s, parseSQL, tableName, dbName);
        List<Entry<String, String>> columns = parseSQL.getColumns();
        if (columns == null || columns.isEmpty()) {
            throw new TDHSSQLException("no columns to select!", parseSQL.getSql());
        }
        String[] field = new String[columns.size()];
        String[] alias = new String[columns.size()];
        int i = 0;
        for (Entry<String, String> e : columns) {
            String f = StringUtil.escapeField(e.getValue());
            if (f == null) {
                throw new TDHSSQLException("error in field[" + e.getValue() + "]", parseSQL.getSql());
            }
            field[i] = f;
            String a = StringUtil.escapeField(e.getKey());
            if (a == null) {
                throw new TDHSSQLException("error in alias[" + e.getKey() + "]", parseSQL.getSql());
            }
            alias[i++] = a;
        }
        if (field.length == 1 && field[0].equalsIgnoreCase("count(*)") && !columns.get(0).getValue().startsWith("`")) {
            TDHSResponse response = null;
            try {
                response = query.count();
            } catch (TDHSException e) {
                throw new SQLException(e);
            }
            processResponse(response, alias, false, false);

        } else {
            query.select(field);
            TDHSResponse response = null;
            try {
                response = query.get();
            } catch (TDHSException e) {
                throw new SQLException(e);
            }
            processResponse(response, alias, false, false);
        }
    }

    private Query preprocessGet(com.taobao.tdhs.client.statement.Statement s, ParseSQL parseSQL, String tableName,
                                String dbName) throws TDHSSQLException {
        Query query = s.query().use(dbName).from(tableName);
        List<OperationStruct> operation = parseSQL.getListOperationStructs();
        if (StringUtils.isNotBlank(parseSQL.getErrmsg())) {
            throw new TDHSSQLException(parseSQL.getErrmsg(), parseSQL.getSql());
        }
        if (operation == null || operation.isEmpty()) {
            throw new TDHSSQLException("must have some where condition!", parseSQL.getSql());
        }
        List<OperationStruct> keys = new ArrayList<OperationStruct>(2);
        HintStruct hint = parseSQL.getHint();
        if (hint != null) {
            if (StringUtils.isNotBlank(hint.getErrmsg())) {
                throw new TDHSSQLException(hint.getErrmsg(), parseSQL.getSql());
            }
            String index = hint.getIndexName();
            List<String> listIndexColumns = hint.getListIndexColumns();
            if (listIndexColumns == null || listIndexColumns.isEmpty()) {
                throw new TDHSSQLException("can't get columns from hint", parseSQL.getSql());
            }
            Map<String, List<OperationStruct>> mapOperationStructs = parseSQL.getMapOperationStructs();
            for (String hintKey : listIndexColumns) {
                List<OperationStruct> k = mapOperationStructs.get(hintKey);
                if ((k == null || k.isEmpty())) {
                    if (keys.isEmpty()) {
                        throw new TDHSSQLException("don't find key in Index!", parseSQL.getSql());
                    } else {
                        break;
                    }
                }
                if (keys.isEmpty()) {
                    keys.add(k.get(0));
                    if ("in".equalsIgnoreCase(k.get(0).getOper())) {
                        //in的话只处理一个
                        break;
                    }
                } else {
                    if (keys.get(0).getOper().equalsIgnoreCase(k.get(0).getOper())) {
                        keys.add(k.get(0));
                    } else {
                        break;
                    }
                }
            }

            List<String> orderByColumn = parseSQL.getOrderByColumn();
            if (!keys.get(0).getOper().equalsIgnoreCase("=") &&
                    ((orderByColumn != null && !orderByColumn.isEmpty()) || parseSQL.getSortMethod() != null)) {
                throw new TDHSSQLException("can't support this orderBy!", parseSQL.getSql());
            }

            if (orderByColumn != null) {
                if (orderByColumn.size() > listIndexColumns.size()) {
                    throw new TDHSSQLException("too many orderBy columns for this index!", parseSQL.getSql());
                }
                int j = 0;
                for (String oc : orderByColumn) {
                    String indexField = listIndexColumns.get(listIndexColumns.size() - orderByColumn.size() + j);
                    if (!indexField.equalsIgnoreCase(oc)) {
                        throw new TDHSSQLException(
                                "can't support this orderBy columns which is not in index or error sort!",
                                parseSQL.getSql());
                    }

                }
            }
            Where where = query.where().index(index);
            processKey(parseSQL, keys, where);
        } else {
            List<String> orderByColumn = parseSQL.getOrderByColumn();
            if ((orderByColumn != null && !orderByColumn.isEmpty()) || parseSQL.getSortMethod() != null) {
                throw new TDHSSQLException("can't support orderBy without hint!", parseSQL.getSql());
            }
            OperationStruct firstKey = operation.get(0);
            keys.add(firstKey);
            String firstField = StringUtil.escapeField(firstKey.getColumnName());
            if (firstField == null) {
                throw new TDHSSQLException("error field [" + firstKey.getColumnName() + "]", parseSQL.getSql());
            }
            Where where = query.where().fields(firstField);
            processKey(parseSQL, keys, where);

        }

        for (OperationStruct o : operation) {
            if (!keys.contains(o)) {
                String op = o.getOper();
                if ("=".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).equal(v);
                } else if ("!=".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).not(v);
                } else if (">".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).greaterThan(v);
                } else if (">=".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).greaterEqual(v);
                } else if ("<".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).lessThan(v);
                } else if ("<=".equalsIgnoreCase(op)) {
                    PerprocessFilter perprocessFilter = new PerprocessFilter(parseSQL, o).invoke();
                    String f = perprocessFilter.getF();
                    String v = perprocessFilter.getV();
                    query.and().field(f).lessEqual(v);
                } else if ("in".equalsIgnoreCase(op)) {
                    throw new TDHSSQLException("can't support IN in this postion!", parseSQL.getSql());
                } else {
                    throw new TDHSSQLException("error operation!", parseSQL.getSql());
                }

            }
        }

        int limit = parseSQL.getLimit();
        int start = parseSQL.getLimitOffset();
        if (limit < 0 || start < 0) {
            throw new TDHSSQLException("limit can't be negative!", parseSQL.getSql());
        }
        query.limit(start, limit);
        return query;
    }

    private void processKey(ParseSQL parseSQL, List<OperationStruct> keys, Where where) throws TDHSSQLException {
        String firstKeyOper = keys.get(0).getOper();
        if ("=".equalsIgnoreCase(firstKeyOper)) {
            if (OrderByType.DESC.equals(parseSQL.getSortMethod())) {
                where.descEqual(parseKeys(parseSQL, keys));
            } else {
                where.equal(parseKeys(parseSQL, keys));
            }
        } else if (">".equalsIgnoreCase(firstKeyOper)) {
            where.greaterThan(parseKeys(parseSQL, keys));
        } else if (">=".equalsIgnoreCase(firstKeyOper)) {
            where.greaterEqual(parseKeys(parseSQL, keys));
        } else if ("<".equalsIgnoreCase(firstKeyOper)) {
            where.lessThan(parseKeys(parseSQL, keys));
        } else if ("<=".equalsIgnoreCase(firstKeyOper)) {
            where.lessEqual(parseKeys(parseSQL, keys));
        } else if ("in".equalsIgnoreCase(firstKeyOper)) {
            String[] in = StringUtil.escapeIn(keys.get(0).getValue());
            if (in == null || in.length == 0) {
                throw new TDHSSQLException("don't have in values!", parseSQL.getSql());
            }
            String[][] inv = new String[in.length][1];
            int i = 0;
            for (String[] a : inv) {
                a[0] = in[i++];
            }
            where.in(inv);
        } else {
            throw new TDHSSQLException("error operation!", parseSQL.getSql());
        }
    }

    private String[] parseKeys(ParseSQL parseSQL, List<OperationStruct> keys) throws TDHSSQLException {
        String kk[] = new String[keys.size()];
        int i = 0;
        for (OperationStruct os : keys) {
            String s = StringUtil.escapeValue(os.getValue());
            if (s == null) {
                throw new TDHSSQLException("error value in index!", parseSQL.getSql());
            }
            kk[i++] = s;
        }
        return kk;
    }

    private void doDelete(com.taobao.tdhs.client.statement.Statement s, ParseSQL parseSQL, String tableName,
                          String dbName) throws SQLException {
        Query query = preprocessGet(s, parseSQL, tableName, dbName);
        TDHSResponse response = null;
        try {
            response = query.delete();
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
        processResponse(response, true);
    }

    private void doUpdate(com.taobao.tdhs.client.statement.Statement s, ParseSQL parseSQL, String tableName,
                          String dbName) throws SQLException {
        Query query = preprocessGet(s, parseSQL, tableName, dbName);
        List<Entry<String, String>> updateEntries = parseSQL.getUpdateEntries();
        if (updateEntries == null || updateEntries.isEmpty()) {
            throw new TDHSSQLException("no value to update!", parseSQL.getSql());
        }

        for (Entry<String, String> e : updateEntries) {
            if (StringUtils.isBlank(e.getKey()) || StringUtils.isBlank(e.getValue())) {
                throw new TDHSSQLException("insert column and values can't be empty!", parseSQL.getSql());
            }
            String field = StringUtil.escapeField(StringUtils.trim(e.getKey()));
            if (field == null) {
                throw new TDHSSQLException("insert column is error!", parseSQL.getSql());
            }
            String value = StringUtils.trim(e.getValue());
            if (StringUtils.equalsIgnoreCase("null", value)) {
                query.set().field(field).setNull();
            } else if (StringUtils.equalsIgnoreCase("now()", value)) {
                query.set().field(field).setNow();
            } else {
                if (StringUtils.startsWith(value, field)) {
                    value = value.substring(field.length());
                    value = StringUtils.trim(value);
                    if (StringUtils.startsWith(value, "+")) {
                        value = value.substring(1);
                        value = StringUtils.trim(value);
                        if (StringUtil.isLong(value)) {
                            query.set().field(field).add(Long.valueOf(value));
                        } else {
                            throw new TDHSSQLException("update value is error ,is not long", parseSQL.getSql());
                        }
                    } else if (StringUtils.startsWith(value, "-")) {
                        value = value.substring(1);
                        value = StringUtils.trim(value);
                        if (StringUtil.isLong(value)) {
                            query.set().field(field).sub(Long.valueOf(value));
                        } else {
                            throw new TDHSSQLException("update value is error ,is not long", parseSQL.getSql());
                        }

                    } else {
                        throw new TDHSSQLException("update value is error maybe can't support!", parseSQL.getSql());
                    }
                } else {
                    value = StringUtil.escapeValue(value);
                    if (value == null) {
                        throw new TDHSSQLException("update value is error!", parseSQL.getSql());
                    }
                    query.set().field(field).set(value);
                }
            }
        }
        TDHSResponse response = null;
        try {
            response = query.update();
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
        processResponse(response, true);
    }

    private void doInsert(com.taobao.tdhs.client.statement.Statement s, ParseSQL parseSQL, String tableName,
                          String dbName) throws SQLException {
        Insert insert = s.insert().use(dbName).from(tableName);
        List<Entry<String, String>> insertEntries = parseSQL.getInsertEntries();
        if (insertEntries == null || insertEntries.isEmpty()) {
            throw new TDHSSQLException("no value to insert!", parseSQL.getSql());
        }
        for (Entry<String, String> e : insertEntries) {
            if (StringUtils.isBlank(e.getKey()) || StringUtils.isBlank(e.getValue())) {
                throw new TDHSSQLException("insert column and values can't be empty!", parseSQL.getSql());
            }
            String field = StringUtil.escapeField(StringUtils.trim(e.getKey()));
            if (field == null) {
                throw new TDHSSQLException("insert column is error!", parseSQL.getSql());
            }
            String value = StringUtils.trim(e.getValue());
            if (StringUtils.equalsIgnoreCase("null", value)) {
                insert.valueSetNull(field);
            } else if (StringUtils.equalsIgnoreCase("now()", value)) {
                insert.valueSetNow(field);
            } else {
                value = StringUtil.escapeValue(value);
                if (value == null) {
                    throw new TDHSSQLException("insert value is error!", parseSQL.getSql());
                }
                insert.value(field, value);
            }
        }
        TDHSResponse response = null;
        try {
            response = insert.insert();
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
        processResponse(response, null, true, true);
    }

    protected void processResponse(TDHSResponse response, boolean isModify) throws SQLException {
        processResponse(response, null, isModify, false);
    }

    protected void processResponse(TDHSResponse response, @Nullable String[] alias, boolean isModify, boolean isInsert)
            throws SQLException {
        if (batchStatement != null) {
            return;
        }
        if (response == null) {
            throw new SQLException("None response!");
        }
        try {
            if (!TDHSResponseEnum.ClientStatus.OK.equals(response.getStatus())) {
                if (TDHSResponseEnum.ClientStatus.DB_ERROR.equals(response.getStatus())) {
                    throw new SQLException(
                            "DB Handler return error code [" + response.getDbErrorCode() + "]");
                } else {
                    throw new SQLException(
                            "return error [" + response.getErrorCode().getErrorMsg() + "]");
                }
            } else {
                if (alias != null) {
                    currentResultSet = response.getResultSet(Arrays.asList(alias));
                } else {
                    currentResultSet = response.getResultSet();
                }
                if (isModify) {
                    updateCount = isInsert ? 1 : ConvertUtil.safeConvertInt(response.getFieldData().get(0).get(0), -1);
                } else {
                    updateCount = -1;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        execute(sql);
        return updateCount;
    }

    protected void checkclose() throws SQLException {
        if (isClosed() || getConnection().isClosed()) {
            throw new SQLException("This statement is closed!");
        }
    }

    protected void reset() throws SQLException {
        currentResultSet = null;
        updateCount = -1;
        clearBatch();
    }

    public void close() throws SQLException {
        close = true;
        reset();
    }

    public int getMaxFieldSize() throws SQLException {
        return TDHSCommon.REQUEST_MAX_FIELD_NUM;
    }

    public void setMaxFieldSize(int max) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getMaxRows() throws SQLException {
        return 0;
    }

    public void setMaxRows(int max) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
    }

    public int getQueryTimeout() throws SQLException {
        return Integer.parseInt(connection.getClientInfo(NonRegisteringDriver.TIME_OUT_PROPERTY_KEY));
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        logger.warn("setQueryTimeout will be ignore!");
//        throw new SQLFeatureNotSupportedException();
    }

    public void cancel() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public void setCursorName(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean execute(String sql) throws SQLException {
        checkclose();
        reset();
        if (StringUtils.isBlank(sql)) {
            throw new SQLException("sql can't be null!");
        }
        PreproccessSQL preproccessSQL = new PreproccessSQL(sql).invoke();
        ParseSQL parseSQL = preproccessSQL.getParseSQL();
        String tableName = preproccessSQL.getTableName();
        String dbName = preproccessSQL.getDbName();
        if (getConnection().isReadOnly() && !SQLType.SELECT.equals(parseSQL.getSqlType())) {
            throw new TDHSSQLException("It is readonly, can't executeSelect " + parseSQL.getSqlType().toString() + " !",
                    sql);
        }
        switch (parseSQL.getSqlType()) {
            case INSERT:
                doInsert(client, parseSQL, tableName, dbName);
                return false;
            case UPDATE:
                doUpdate(client, parseSQL, tableName, dbName);
                return false;
            case DELETE:
                doDelete(client, parseSQL, tableName, dbName);
                return false;
            case SELECT:
                doSelectOrCount(client, parseSQL, tableName, dbName);
                return true;
            default:
                throw new TDHSSQLException("can't parse this SQL!", sql);
        }
    }

    public ResultSet getResultSet() throws SQLException {
        checkclose();
        return currentResultSet;
    }

    public int getUpdateCount() throws SQLException {
        checkclose();
        return updateCount;
    }

    public boolean getMoreResults() throws SQLException {
        return false;
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_FORWARD;
    }

    public void setFetchSize(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    public void addBatch(String sql) throws SQLException {
        checkclose();
        if (StringUtils.isBlank(sql)) {
            throw new SQLException("sql can't be null!");
        }
        if (batchStatement == null) {
            batchStatement = client.createBatchStatement();
        }
        PreproccessSQL preproccessSQL = new PreproccessSQL(sql).invoke();
        ParseSQL parseSQL = preproccessSQL.getParseSQL();
        String tableName = preproccessSQL.getTableName();
        String dbName = preproccessSQL.getDbName();
        switch (parseSQL.getSqlType()) {
            case INSERT:
                doInsert(batchStatement, parseSQL, tableName, dbName);
                break;
            case UPDATE:
                doUpdate(batchStatement, parseSQL, tableName, dbName);
                break;
            case DELETE:
                doDelete(batchStatement, parseSQL, tableName, dbName);
                break;
            case SELECT:
                throw new TDHSSQLException("add batch not support select!", sql);
            default:
                throw new TDHSSQLException("can't parse this SQL!", sql);
        }
    }

    public void clearBatch() throws SQLException {
        batchStatement = null;
    }

    public int[] executeBatch() throws SQLException {
        checkclose();
        BatchStatement b = batchStatement;
        reset();

        TDHSResponse[] responses = null;
        try {
            responses = b.commit();
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
        int[] result = new int[responses.length];
        StringBuilder expString = new StringBuilder();
        boolean success = true;
        int i = 0;
        try {
            for (TDHSResponse r : responses) {
                if (!r.getStatus().equals(TDHSResponseEnum.ClientStatus.OK)) {
                    success = false;
                    expString.append(r).append("|");
                } else {
                    result[i++] = ConvertUtil.safeConvertInt(r.getFieldData().get(0).get(0), -1);
                }
            }
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
        if (!success) {
            throw new SQLException(expString.toString());
        }
        return result;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public boolean getMoreResults(int current) throws SQLException {
        checkclose();
        return false;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
            throw new SQLFeatureNotSupportedException();
        }
        return executeUpdate(sql);
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        if (columnIndexes != null && columnIndexes.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return executeUpdate(sql);
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return executeUpdate(sql);
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
            throw new SQLFeatureNotSupportedException();
        }
        return execute(sql);
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        if (columnIndexes != null && columnIndexes.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return execute(sql);
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return execute(sql);
    }

    public int getResultSetHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public boolean isClosed() throws SQLException {
        return this.close || getConnection().isClosed();
    }

    public void setPoolable(boolean poolable) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isPoolable() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            // This works for classes that aren't actually wrapping
            // anything
            return iface.cast(this);
        } catch (ClassCastException cce) {
            throw new SQLException(cce);
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    protected class PreproccessSQL {
        private String sql;
        private ParseSQL parseSQL;
        private String tableName;
        private String dbName;

        public PreproccessSQL(@NotNull String sql) {
            this.sql = sql;
        }

        public ParseSQL getParseSQL() {
            return parseSQL;
        }

        public String getTableName() {
            return tableName;
        }

        public String getDbName() {
            return dbName;
        }

        public PreproccessSQL invoke() throws SQLException {
            parseSQL = parseSQL(sql);
            tableName = parseSQL.getTableName();
            dbName = db;
            if (StringUtils.contains(tableName, ".")) {
                String[] strings = StringUtils.split(tableName, ".");
                if (strings.length == 2) {
                    dbName = strings[0];
                    tableName = strings[1];
                } else if (strings.length == 1) {
                    tableName = strings[0];
                } else {
                    throw new TDHSSQLException("can't parse table!", sql);
                }
            }
            if (StringUtils.isBlank(dbName) || StringUtils.isBlank(tableName)) {
                throw new TDHSSQLException("can't parse table!", sql);
            }
            return this;
        }

        private ParseSQL parseSQL(@NotNull String sql) throws SQLException {
            ParseSQL parseSQL = new ParseSQL(sql);
            parseSQL.sqlDispatch();
            if (StringUtils.isNotBlank(parseSQL.getErrmsg())) {
                throw new TDHSSQLException("message:" + parseSQL.getErrmsg(), sql);
            }
            if (StringUtils.isBlank(parseSQL.getTableName())) {
                throw new TDHSSQLException("don't have table!", sql);
            }
            return parseSQL;
        }
    }

    private class PerprocessFilter {
        private ParseSQL parseSQL;
        private OperationStruct o;
        private String f;
        private String v;

        public PerprocessFilter(ParseSQL parseSQL, OperationStruct o) {
            this.parseSQL = parseSQL;
            this.o = o;
        }

        public String getF() {
            return f;
        }

        public String getV() {
            return v;
        }

        public PerprocessFilter invoke() throws TDHSSQLException {
            f = StringUtil.escapeField(o.getColumnName());
            if (f == null) {
                throw new TDHSSQLException("error column [" + o.getColumnName() + "]", parseSQL.getSql());
            }
            v = StringUtil.escapeValue(o.getValue());
            if (v == null) {
                throw new TDHSSQLException("error value [" + o.getValue() + "]", parseSQL.getSql());
            }
            return this;
        }
    }
}
