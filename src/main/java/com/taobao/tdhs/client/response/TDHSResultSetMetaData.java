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

package com.taobao.tdhs.client.response;

import com.taobao.tdhs.client.util.MysqlUtil;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 上午11:27
 */
public class TDHSResultSetMetaData implements ResultSetMetaData {
    private final List<TDHSResponseEnum.IFieldType> fieldTypes;

    private final List<String> alias;

    private final List<String> fieldName;

    private final String table;

    private final String db;

    public TDHSResultSetMetaData(List<TDHSResponseEnum.IFieldType> fieldTypes, List<String> alias,
                                 TDHSMetaData metaData) {
        this.fieldTypes = fieldTypes;
        this.alias = alias;
        this.fieldName = metaData.getFieldNames();
        this.table = metaData.getTable();
        this.db = metaData.getDb();
    }

    private TDHSResponseEnum.IFieldType getFieldType(int column) throws SQLException {
        if (fieldTypes == null || column <= 0 || column > fieldTypes.size()) {
            throw new SQLException("Invaild column:" + column);
        }
        return fieldTypes.get(column - 1);
    }

    private String getAlias(int column) throws SQLException {
        if (alias == null || column <= 0 || column > alias.size()) {
            throw new SQLException("Invaild column:" + column);
        }
        return alias.get(column - 1);
    }

    private String getFieldName(int column) throws SQLException {
        if (fieldName == null || column <= 0 || column > fieldName.size()) {
            throw new SQLException("Invaild column:" + column);
        }
        return fieldName.get(column - 1);
    }

    public int getColumnCount() throws SQLException {
        if (fieldTypes == null) {
            throw new SQLException("no field!");
        }
        return fieldTypes.size();
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        TDHSResponseEnum.IFieldType fieldType = getFieldType(column);
        int javaType = MysqlUtil.mysqlToJavaType(fieldType.getType());
        switch (javaType) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return false;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            default:
                return true;
        }
    }

    public boolean isSearchable(int column) throws SQLException {
        return true;
    }

    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    public int isNullable(int column) throws SQLException {
        return columnNullableUnknown;
    }

    public boolean isSigned(int column) throws SQLException {
        TDHSResponseEnum.IFieldType fieldType = getFieldType(column);
        int javaType = MysqlUtil.mysqlToJavaType(fieldType.getType());
        switch (javaType) {
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return true;

            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return false;
            default:
                return false;
        }
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        return Integer.MAX_VALUE;
    }

    public String getColumnLabel(int column) throws SQLException {
        return getAlias(column);
    }

    public String getColumnName(int column) throws SQLException {
        return getFieldName(column);
    }

    public String getSchemaName(int column) throws SQLException {
        return "";
    }

    public int getPrecision(int column) throws SQLException {
        return Integer.MAX_VALUE;
    }

    public int getScale(int column) throws SQLException {
        return Integer.MAX_VALUE;
    }

    public String getTableName(int column) throws SQLException {
        return table;
    }

    public String getCatalogName(int column) throws SQLException {
        return db;
    }

    public int getColumnType(int column) throws SQLException {
        TDHSResponseEnum.IFieldType fieldType = getFieldType(column);
        return MysqlUtil.mysqlToJavaType(fieldType.getType());
    }

    public String getColumnTypeName(int column) throws SQLException {
        TDHSResponseEnum.IFieldType fieldType = getFieldType(column);
        int javaType = MysqlUtil.mysqlToJavaType(fieldType.getType());
        return MysqlUtil.typeToName(javaType);
    }

    public boolean isReadOnly(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isWritable(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public String getColumnClassName(int column) throws SQLException {
        TDHSResponseEnum.IFieldType fieldType = getFieldType(column);
        int javaType = MysqlUtil.mysqlToJavaType(fieldType.getType());
        return MysqlUtil.getClassNameForJavaType(javaType, fieldType);
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
}
