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
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-20 上午10:50
 */
public class TDHSPreparedStatement extends TDHSStatement implements PreparedStatement {
    private final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String sql;

    private final int parameterNumber;

    private final String[] sqlSplited;

    private final Map<Integer, String> parameters = new HashMap<Integer, String>();

    public TDHSPreparedStatement(Connection connection, TDHSClient client, String db, String sql) throws SQLException {
        super(connection, client, db);
        if (StringUtils.isBlank(sql)) {
            throw new SQLException("sql can't be null");
        }
        this.sql = StringUtils.trim(sql);
        this.sqlSplited = StringUtils.splitPreserveAllTokens(this.sql, "?");
        this.parameterNumber = sqlSplited.length - 1;
    }

    private String mergeSQL() throws SQLException {
        if (parameters.size() < parameterNumber) {
            throw new SQLException("Don't have enough parameter!");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= parameterNumber; i++) {
            sb.append(sqlSplited[i - 1]).append(parameters.get(i));
        }
        sb.append(sqlSplited[parameterNumber]);
        return sb.toString();
    }

    public ResultSet executeQuery() throws SQLException {
        try {
            return executeQuery(mergeSQL());
        } finally {
            clearParameters();
        }
    }

    public int executeUpdate() throws SQLException {
        try {
            return executeUpdate(mergeSQL());
        } finally {
            clearParameters();
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        setDString(parameterIndex, "null");
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setDString(parameterIndex, x ? "1" : "0");
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        setDString(parameterIndex, String.valueOf(x));
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        setDString(parameterIndex, x == null ? null : x.toString());
    }

    public void setDString(int parameterIndex, @Nullable String x) throws SQLException {
        checkclose();
        if (parameterIndex < 1 || parameterIndex > this.parameterNumber) {
            throw new SQLException("parameterIndex is out of range,parameterIndex is " + parameterIndex);
        }
        parameters.put(parameterIndex, x);
    }

    public void setString(int parameterIndex, @Nullable String x) throws SQLException {
        if (StringUtils.isNotBlank(x)) {
            x = StringUtils.replace(x, "\\", "\\\\");
            x = StringUtils.replace(x, "\'", "\\\'");
            x = StringUtils.replace(x, "\"", "\\\"");
            x = "\'" + x + "\'";
        }
        setDString(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        checkclose();
        if (parameterIndex < 1 || parameterIndex > this.parameterNumber) {
            throw new SQLException("parameterIndex is out of range,parameterIndex is " + parameterIndex);
        }
        setString(parameterIndex, x == null ? null : BYTE_PARAMETER_PREFIX + parameterIndex);
        super.byteParameters.put(parameterIndex, x);
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        setString(parameterIndex, x == null ? null : DEFAULT_DATE_FORMAT.format(x));
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        setString(parameterIndex, x == null ? null : DEFAULT_DATE_FORMAT.format(x));
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setString(parameterIndex, x == null ? null : DEFAULT_DATE_FORMAT.format(x));
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 0);
            return;
        }
        byte[] b = new byte[length];
        int read;
        try {
            if ((read = x.read(b)) != length) {
                throw new SQLException("AsciiStream read length:" + read);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        setBytes(parameterIndex, b);
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        setAsciiStream(parameterIndex, x, length);
    }

    public void clearParameters() throws SQLException {
        byteParameters.clear();
        parameters.clear();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public boolean execute() throws SQLException {
        try {
            return execute(mergeSQL());
        } finally {
            clearParameters();
        }
    }

    public void addBatch() throws SQLException {
        try {
            addBatch(mergeSQL());
        } finally {
            clearParameters();
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        if (reader == null) {
            setNull(parameterIndex, 0);
            return;
        }
        char[] c = new char[length];
        int read;
        try {
            if ((read = reader.read(c)) != length) {
                throw new SQLException("CharacterStream read length:" + read);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        setString(parameterIndex, new String(c));
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        setBlob(parameterIndex, x.getBinaryStream());
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        DateFormat dateFormat = getDateFormat(cal);
        setString(parameterIndex, x == null ? null : dateFormat.format(x));
    }

    private DateFormat getDateFormat(Calendar cal) {
        DateFormat dateFormat = (DateFormat) DEFAULT_DATE_FORMAT.clone();
        if (cal != null) {
            TimeZone timeZone = cal.getTimeZone();
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat;
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        DateFormat dateFormat = getDateFormat(cal);
        setString(parameterIndex, x == null ? null : dateFormat.format(x));
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        DateFormat dateFormat = getDateFormat(cal);
        setString(parameterIndex, x == null ? null : dateFormat.format(x));
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        setNull(parameterIndex, sqlType);
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        setString(parameterIndex, x == null ? null : x.toString());
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        setAsciiStream(parameterIndex, inputStream, length);
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setAsciiStream(parameterIndex, x, (int) length);
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setBinaryStream(parameterIndex, x, (int) length);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, (int) length);
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            setAsciiStream(parameterIndex, x, x == null ? 0 : x.available());
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            setBinaryStream(parameterIndex, x, x == null ? 0 : x.available());
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        setAsciiStream(parameterIndex, inputStream);
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
