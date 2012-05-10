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
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.statement.BatchStatement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-7 下午1:02
 */
public class TDHSConnection implements Connection {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TDHSClientInstance.ClientKey key;
    private boolean closed = false;
    private boolean readOnly = false;
    private String dbName;
    private final Properties prop;
    private boolean autoCommit = true;


    public TDHSConnection(@NotNull TDHSClientInstance.ClientKey key, @NotNull Properties prop) {
        this.key = key;
        this.prop = prop;
        this.dbName = this.prop.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
    }

    private TDHSClient getClient() {
        return key.getClient();
    }

    public Statement createStatement() throws SQLException {
        checkClosed();
        return new TDHSStatement(this, getClient(), dbName);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkClosed();
        return new TDHSPreparedStatement(this, getClient(), dbName, sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public String nativeSQL(String sql) throws SQLException {
        return sql;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();
        this.autoCommit = autoCommit;
    }

    public boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }

    public void commit() throws SQLException {
        checkClosed();
        if (getAutoCommit()) {
            throw new SQLException("can't commit when autoCommit is true!");
        }
    }

    public void rollback() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    private void checkClosed() throws SQLException {
        if (isClosed()) {
            throw new SQLException("This connection is closed!");
        }
    }

    public void close() throws SQLException {
        if (!isClosed()) {
            closed = true;
            TDHSClientInstance.closeConnection(key);
        }
    }

    public boolean isClosed() throws SQLException {
        return closed;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() throws SQLException {
        return this.readOnly;
    }

    public void setCatalog(String catalog) throws SQLException {
        this.dbName = catalog;
    }

    public String getCatalog() throws SQLException {
        return this.dbName;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getTransactionIsolation() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY || resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLFeatureNotSupportedException();
        }
        return createStatement();
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY || resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLFeatureNotSupportedException();
        }
        return prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setHoldability(int holdability) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY || resultSetConcurrency != ResultSet.CONCUR_READ_ONLY ||
                resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLFeatureNotSupportedException();
        }
        return createStatement();
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY || resultSetConcurrency != ResultSet.CONCUR_READ_ONLY ||
                resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLFeatureNotSupportedException();
        }
        return prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
            throw new SQLFeatureNotSupportedException();
        }
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if (columnIndexes != null && columnIndexes.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length > 0) {
            throw new SQLFeatureNotSupportedException();
        }
        return prepareStatement(sql);
    }

    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isValid(int timeout) throws SQLException {
        if (isClosed()) {
            return false;
        }
        TDHSClient client = getClient();
        BatchStatement batchStatement = client.createBatchStatement();
        batchStatement.setTimeOut(timeout);
        try {
            batchStatement.commit();
            return true;
        } catch (TDHSException e) {
            logger.warn("valid failed", e);
            return false;
        }
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new SQLClientInfoException();
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new SQLClientInfoException();
    }

    public String getClientInfo(String name) throws SQLException {
        return prop.getProperty(name);
    }

    public Properties getClientInfo() throws SQLException {
        return prop;
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setSchema(String schema) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public String getSchema() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void abort(Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getNetworkTimeout() throws SQLException {
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
        checkClosed();
        return iface.isInstance(this);
    }
}
