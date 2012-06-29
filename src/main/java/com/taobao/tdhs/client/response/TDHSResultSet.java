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

import com.taobao.tdhs.client.util.ConvertUtil;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-1-13 下午1:23
 */
public class TDHSResultSet implements ResultSet {

    private final Map<String, Integer> fieldMap = new HashMap<String, Integer>();

    private final List<TDHSResponseEnum.FieldType> fieldTypes;

    private final List<List<byte[]>> fieldData;

    private ListIterator<List<byte[]>> iterator;

    private List<byte[]> currentRow;

    private int index = -1;

    private boolean lastWasNull = false;

    private final List<String> alias;

    private final TDHSMetaData metaData;

    private final String charsetName;

    /**
     * Constructor TDHSResultSet creates a new TDHSResultSet instance.
     *
     * @param alias      of type List<String>
     * @param metaData   of type TDHSMetaData
     * @param fieldTypes of type List<FieldType>
     * @param fieldData  of type List<List<byte[]>>
     */
    public TDHSResultSet(List<String> alias, TDHSMetaData metaData, List<TDHSResponseEnum.FieldType> fieldTypes,
                         List<List<byte[]>> fieldData, String charsetName) {
        if (alias != null) {
            int i = 1;
            for (String f : alias) {
                fieldMap.put(f.toLowerCase(), i);
                fieldMap.put(f.toUpperCase(), i);
                fieldMap.put(f, i++);
            }
        }
        this.alias = alias;
        this.metaData = metaData;
        if (fieldTypes != null) {
            this.fieldTypes = fieldTypes;
        } else {
            this.fieldTypes = new ArrayList<TDHSResponseEnum.FieldType>();
        }

        this.fieldData = fieldData;
        if (fieldData != null) {
            this.iterator = fieldData.listIterator();
        }

        this.charsetName = charsetName;
    }

    /**
     * Method next ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean next() throws SQLException {
        if (iterator != null && iterator.hasNext()) {
            currentRow = iterator.next();
            index++;
            return true;
        }
        return false;
    }

    /**
     * Method close ...
     *
     * @throws SQLException when
     */
    public void close() throws SQLException {
        //do nothing
    }

    /**
     * Method wasNull ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean wasNull() throws SQLException {
        return this.lastWasNull;
    }

    /**
     * Method checkRow ...
     *
     * @param columnIndex of type int
     *
     * @throws SQLException when
     */
    private void checkRow(int columnIndex) throws SQLException {
        if (currentRow == null) {
            throw new SQLException("can't find current row");
        }
        if (columnIndex <= 0 || columnIndex > currentRow.size()) {
            throw new SQLException("Invaild column:" + columnIndex);
        }
    }

    /**
     * Method checkType ...
     *
     * @param columnIndex of type int
     *
     * @throws SQLException when
     */
    private void checkType(int columnIndex) throws SQLException {
        if (columnIndex <= 0 || columnIndex > fieldTypes.size()) {
            throw new SQLException("Invaild column:" + columnIndex);
        }
    }

    /**
     * Method getString ...
     *
     * @param columnIndex of type int
     *
     * @return String
     *
     * @throws SQLException when
     */
    public String getString(int columnIndex) throws SQLException {
        String str;
        try {
            str = ConvertUtil.getStringFromByte(this.getBytes(columnIndex), this.charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new SQLException(e);
        }
        this.lastWasNull = (str == null);
        return StringUtils.trim(str);
    }

    /**
     * Method getBoolean ...
     *
     * @param columnIndex of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        return ConvertUtil.getBooleanFromString(this.getString(columnIndex));
    }

    /**
     * Method getByte ...
     *
     * @param columnIndex of type int
     *
     * @return byte
     *
     * @throws SQLException when
     */
    public byte getByte(int columnIndex) throws SQLException {
        return ConvertUtil.getByteFromString(this.getString(columnIndex));
    }


    /**
     * Method getShort ...
     *
     * @param columnIndex of type int
     *
     * @return short
     *
     * @throws SQLException when
     */
    public short getShort(int columnIndex) throws SQLException {
        return ConvertUtil.getShortFromString(this.getString(columnIndex));
    }

    /**
     * Method getInt ...
     *
     * @param columnIndex of type int
     *
     * @return int
     *
     * @throws SQLException when
     */
    public int getInt(int columnIndex) throws SQLException {
        return ConvertUtil.getIntFromString(this.getString(columnIndex));
    }

    /**
     * Method getLong ...
     *
     * @param columnIndex of type int
     *
     * @return long
     *
     * @throws SQLException when
     */
    public long getLong(int columnIndex) throws SQLException {
        return ConvertUtil.getLongFromString(this.getString(columnIndex));
    }

    /**
     * Method getFloat ...
     *
     * @param columnIndex of type int
     *
     * @return float
     *
     * @throws SQLException when
     */
    public float getFloat(int columnIndex) throws SQLException {
        return ConvertUtil.getFloatFromString(this.getString(columnIndex));
    }

    /**
     * Method getDouble ...
     *
     * @param columnIndex of type int
     *
     * @return double
     *
     * @throws SQLException when
     */
    public double getDouble(int columnIndex) throws SQLException {
        return ConvertUtil.getDoubleFromString(this.getString(columnIndex));
    }

    /**
     * Method getBigDecimal ...
     *
     * @param columnIndex of type int
     * @param scale       of type int
     *
     * @return BigDecimal
     *
     * @throws SQLException when
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return ConvertUtil.getBigDecimalFromString(this.getString(columnIndex), scale);
    }

    /**
     * Method getBytes ...
     *
     * @param columnIndex of type int
     *
     * @return byte[]
     *
     * @throws SQLException when
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        this.checkRow(columnIndex);
        return currentRow.get(columnIndex - 1);
    }

    /**
     * Method getDate ...
     *
     * @param columnIndex of type int
     *
     * @return Date
     *
     * @throws SQLException when
     */
    public Date getDate(int columnIndex) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), null);
        return time == null ? null : new Date(time);
    }

    /**
     * Method getTime ...
     *
     * @param columnIndex of type int
     *
     * @return Time
     *
     * @throws SQLException when
     */
    public Time getTime(int columnIndex) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), null);
        return time == null ? null : new Time(time);
    }

    /**
     * Method getTimestamp ...
     *
     * @param columnIndex of type int
     *
     * @return Timestamp
     *
     * @throws SQLException when
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), null);
        return time == null ? null : new Timestamp(time);
    }

    /**
     * Method getAsciiStream ...
     *
     * @param columnIndex of type int
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        byte[] b = this.getBytes(columnIndex);
        return b == null ? null : new ByteArrayInputStream(b);
    }

    /**
     * Method getUnicodeStream ...
     *
     * @param columnIndex of type int
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getBinaryStream ...
     *
     * @param columnIndex of type int
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return this.getAsciiStream(columnIndex);
    }

    /**
     * Method getString ...
     *
     * @param columnLabel of type String
     *
     * @return String
     *
     * @throws SQLException when
     */
    public String getString(String columnLabel) throws SQLException {
        return this.getString(this.findColumn(columnLabel));
    }

    /**
     * Method getBoolean ...
     *
     * @param columnLabel of type String
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean getBoolean(String columnLabel) throws SQLException {
        return this.getBoolean(this.findColumn(columnLabel));
    }

    /**
     * Method getByte ...
     *
     * @param columnLabel of type String
     *
     * @return byte
     *
     * @throws SQLException when
     */
    public byte getByte(String columnLabel) throws SQLException {
        return this.getByte(this.findColumn(columnLabel));
    }

    /**
     * Method getShort ...
     *
     * @param columnLabel of type String
     *
     * @return short
     *
     * @throws SQLException when
     */
    public short getShort(String columnLabel) throws SQLException {
        return this.getShort(this.findColumn(columnLabel));
    }

    /**
     * Method getInt ...
     *
     * @param columnLabel of type String
     *
     * @return int
     *
     * @throws SQLException when
     */
    public int getInt(String columnLabel) throws SQLException {
        return this.getInt(this.findColumn(columnLabel));
    }

    /**
     * Method getLong ...
     *
     * @param columnLabel of type String
     *
     * @return long
     *
     * @throws SQLException when
     */
    public long getLong(String columnLabel) throws SQLException {
        return this.getLong(this.findColumn(columnLabel));
    }

    /**
     * Method getFloat ...
     *
     * @param columnLabel of type String
     *
     * @return float
     *
     * @throws SQLException when
     */
    public float getFloat(String columnLabel) throws SQLException {
        return this.getFloat(this.findColumn(columnLabel));
    }

    /**
     * Method getDouble ...
     *
     * @param columnLabel of type String
     *
     * @return double
     *
     * @throws SQLException when
     */
    public double getDouble(String columnLabel) throws SQLException {
        return this.getDouble(this.findColumn(columnLabel));
    }

    /**
     * Method getBigDecimal ...
     *
     * @param columnLabel of type String
     * @param scale       of type int
     *
     * @return BigDecimal
     *
     * @throws SQLException when
     */
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel), scale);
    }

    /**
     * Method getBytes ...
     *
     * @param columnLabel of type String
     *
     * @return byte[]
     *
     * @throws SQLException when
     */
    public byte[] getBytes(String columnLabel) throws SQLException {
        return this.getBytes(this.findColumn(columnLabel));
    }

    /**
     * Method getDate ...
     *
     * @param columnLabel of type String
     *
     * @return Date
     *
     * @throws SQLException when
     */
    public Date getDate(String columnLabel) throws SQLException {
        return this.getDate(this.findColumn(columnLabel));
    }

    /**
     * Method getTime ...
     *
     * @param columnLabel of type String
     *
     * @return Time
     *
     * @throws SQLException when
     */
    public Time getTime(String columnLabel) throws SQLException {
        return this.getTime(this.findColumn(columnLabel));
    }

    /**
     * Method getTimestamp ...
     *
     * @param columnLabel of type String
     *
     * @return Timestamp
     *
     * @throws SQLException when
     */
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return this.getTimestamp(this.findColumn(columnLabel));
    }

    /**
     * Method getAsciiStream ...
     *
     * @param columnLabel of type String
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return this.getAsciiStream(this.findColumn(columnLabel));
    }

    /**
     * Method getUnicodeStream ...
     *
     * @param columnLabel of type String
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return this.getUnicodeStream(this.findColumn(columnLabel));
    }

    /**
     * Method getBinaryStream ...
     *
     * @param columnLabel of type String
     *
     * @return InputStream
     *
     * @throws SQLException when
     */
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return this.getBinaryStream(this.findColumn(columnLabel));
    }

    /**
     * Method getWarnings returns the warnings of this TDHSResultSet object.
     *
     * @return the warnings (type SQLWarning) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /**
     * Method clearWarnings ...
     *
     * @throws SQLException when
     */
    public void clearWarnings() throws SQLException {
    }

    /**
     * Method getCursorName returns the cursorName of this TDHSResultSet object.
     *
     * @return the cursorName (type String) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public String getCursorName() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getMetaData returns the metaData of this TDHSResultSet object.
     *
     * @return the metaData (type ResultSetMetaData) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return new TDHSResultSetMetaData(fieldTypes, alias, metaData);
    }

    /**
     * Method getObject ...
     *
     * @param columnIndex of type int
     *
     * @return Object
     *
     * @throws SQLException when
     */
    public Object getObject(int columnIndex) throws SQLException {
        this.checkRow(columnIndex);
        this.checkType(columnIndex);
        TDHSResponseEnum.FieldType fieldType = fieldTypes.get(columnIndex - 1);
        switch (fieldType) {
            case MYSQL_TYPE_DECIMAL:
                return this.getBigDecimal(columnIndex);
            case MYSQL_TYPE_TINY:
                return this.getByte(columnIndex);
            case MYSQL_TYPE_SHORT:
                return this.getShort(columnIndex);
            case MYSQL_TYPE_LONG:
                return this.getLong(columnIndex);
            case MYSQL_TYPE_FLOAT:
                return this.getFloat(columnIndex);
            case MYSQL_TYPE_DOUBLE:
                return this.getDouble(columnIndex);
            case MYSQL_TYPE_NULL:
                return null;
            case MYSQL_TYPE_TIMESTAMP:
                return this.getTimestamp(columnIndex);
            case MYSQL_TYPE_LONGLONG:
                return this.getBigDecimal(columnIndex);
            case MYSQL_TYPE_INT24:
                return this.getBigDecimal(columnIndex);
            case MYSQL_TYPE_DATE:
                return this.getDate(columnIndex);
            case MYSQL_TYPE_TIME:
                return this.getTime(columnIndex);
            case MYSQL_TYPE_DATETIME:
                return this.getDate(columnIndex);
            case MYSQL_TYPE_YEAR:
                return this.getDate(columnIndex);
            case MYSQL_TYPE_NEWDATE:
                return this.getDate(columnIndex);
            case MYSQL_TYPE_VARCHAR:
                return this.getString(columnIndex);
            case MYSQL_TYPE_BIT:
                return this.getBoolean(columnIndex);
            case MYSQL_TYPE_NEWDECIMAL:
                return this.getBigDecimal(columnIndex);
            case MYSQL_TYPE_ENUM:
                return this.getString(columnIndex);
            case MYSQL_TYPE_SET:
                return this.getString(columnIndex);
            case MYSQL_TYPE_TINY_BLOB:
                return this.getBytes(columnIndex);
            case MYSQL_TYPE_MEDIUM_BLOB:
                return this.getBytes(columnIndex);
            case MYSQL_TYPE_LONG_BLOB:
                return this.getBytes(columnIndex);
            case MYSQL_TYPE_BLOB:
                return this.getBytes(columnIndex);
            case MYSQL_TYPE_VAR_STRING:
                return this.getString(columnIndex);
            case MYSQL_TYPE_STRING:
                return this.getString(columnIndex);
            case MYSQL_TYPE_GEOMETRY:
                return this.getString(columnIndex);
        }
        return null;
    }

    /**
     * Method getObject ...
     *
     * @param columnLabel of type String
     *
     * @return Object
     *
     * @throws SQLException when
     */
    public Object getObject(String columnLabel) throws SQLException {
        return this.getObject(this.findColumn(columnLabel));
    }

    /**
     * Method findColumn ...
     *
     * @param columnLabel of type String
     *
     * @return int
     *
     * @throws SQLException when
     */
    public int findColumn(String columnLabel) throws SQLException {
        if (!fieldMap.containsKey(columnLabel)) {
            throw new SQLException("columnLabel " + columnLabel
                    + " is not in result set");
        }
        return fieldMap.get(columnLabel);
    }

    /**
     * Method getCharacterStream ...
     *
     * @param columnIndex of type int
     *
     * @return Reader
     *
     * @throws SQLException when
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        String stringVal = this.getString(columnIndex);
        if (stringVal == null) {
            return null;
        }
        return new StringReader(stringVal);
    }

    /**
     * Method getCharacterStream ...
     *
     * @param columnLabel of type String
     *
     * @return Reader
     *
     * @throws SQLException when
     */
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return this.getCharacterStream(this.findColumn(columnLabel));
    }

    /**
     * Method getBigDecimal ...
     *
     * @param columnIndex of type int
     *
     * @return BigDecimal
     *
     * @throws SQLException when
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return ConvertUtil.getBigDecimalFromString(this.getString(columnIndex));
    }

    /**
     * Method getBigDecimal ...
     *
     * @param columnLabel of type String
     *
     * @return BigDecimal
     *
     * @throws SQLException when
     */
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel));
    }

    /**
     * Method isBeforeFirst returns the beforeFirst of this TDHSResultSet object.
     *
     * @return the beforeFirst (type boolean) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public boolean isBeforeFirst() throws SQLException {
        return index < 0;
    }

    /**
     * Method isAfterLast returns the afterLast of this TDHSResultSet object.
     *
     * @return the afterLast (type boolean) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public boolean isAfterLast() throws SQLException {
        return fieldData != null && index >= fieldData.size();
    }

    /**
     * Method isFirst returns the first of this TDHSResultSet object.
     *
     * @return the first (type boolean) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public boolean isFirst() throws SQLException {
        return index == 0;
    }

    /**
     * Method isLast returns the last of this TDHSResultSet object.
     *
     * @return the last (type boolean) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public boolean isLast() throws SQLException {
        return fieldData != null && index == fieldData.size() - 1;
    }

    /**
     * Method beforeFirst ...
     *
     * @throws SQLException when
     */
    public void beforeFirst() throws SQLException {
        currentRow = null;
        index = -1;
        if (fieldData != null) {
            this.iterator = fieldData.listIterator();
        }
    }

    /**
     * Method afterLast ...
     *
     * @throws SQLException when
     */
    public void afterLast() throws SQLException {
        currentRow = null;
        index = fieldData != null ? fieldData.size() : -1;
        this.iterator = null;
    }

    /**
     * Method first ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean first() throws SQLException {
        beforeFirst();
        return next();
    }

    /**
     * Method last ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean last() throws SQLException {
        if (fieldData != null && !fieldData.isEmpty()) {
            index = fieldData.size() - 1;
            currentRow = fieldData.get(index);
            iterator = null; //it is last,didn't need  iterator
            return true;

        }
        return false;
    }

    /**
     * Method getRow returns the row of this TDHSResultSet object.
     *
     * @return the row (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getRow() throws SQLException {
        return index + 1;
    }

    /**
     * Method absolute ...
     *
     * @param row of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean absolute(int row) throws SQLException {
        if (fieldData == null || row <= 0 || row > fieldData.size()) {
            throw new SQLException("Invaild row:" + row);
        }
        if (fieldData != null && !fieldData.isEmpty()) {
            index = row - 1;
            currentRow = fieldData.get(index);
            iterator = fieldData.listIterator(index);
            return true;

        }
        return false;
    }

    /**
     * Method relative ...
     *
     * @param rows of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean relative(int rows) throws SQLException {
        return absolute(index + rows);
    }

    /**
     * Method previous ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean previous() throws SQLException {
        if (iterator != null && iterator.hasPrevious()) {
            currentRow = iterator.previous();
            index--;
            return true;
        }
        return false;
    }

    /**
     * Method setFetchDirection sets the fetchDirection of this TDHSResultSet object.
     *
     * @param direction the fetchDirection of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getFetchDirection returns the fetchDirection of this TDHSResultSet object.
     *
     * @return the fetchDirection (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getFetchDirection() throws SQLException {
        return FETCH_FORWARD;
    }

    /**
     * Method setFetchSize sets the fetchSize of this TDHSResultSet object.
     *
     * @param rows the fetchSize of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public void setFetchSize(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getFetchSize returns the fetchSize of this TDHSResultSet object.
     *
     * @return the fetchSize (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getType returns the type of this TDHSResultSet object.
     *
     * @return the type (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getType() throws SQLException {
        return TYPE_FORWARD_ONLY;
    }

    /**
     * Method getConcurrency returns the concurrency of this TDHSResultSet object.
     *
     * @return the concurrency (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getConcurrency() throws SQLException {
        return CONCUR_READ_ONLY;
    }

    /**
     * Method rowUpdated ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowUpdated() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method rowInserted ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowInserted() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method rowDeleted ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowDeleted() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNull ...
     *
     * @param columnIndex of type int
     *
     * @throws SQLException when
     */
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBoolean ...
     *
     * @param columnIndex of type int
     * @param x           of type boolean
     *
     * @throws SQLException when
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateByte ...
     *
     * @param columnIndex of type int
     * @param x           of type byte
     *
     * @throws SQLException when
     */
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateShort ...
     *
     * @param columnIndex of type int
     * @param x           of type short
     *
     * @throws SQLException when
     */
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateInt ...
     *
     * @param columnIndex of type int
     * @param x           of type int
     *
     * @throws SQLException when
     */
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateLong ...
     *
     * @param columnIndex of type int
     * @param x           of type long
     *
     * @throws SQLException when
     */
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateFloat ...
     *
     * @param columnIndex of type int
     * @param x           of type float
     *
     * @throws SQLException when
     */
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateDouble ...
     *
     * @param columnIndex of type int
     * @param x           of type double
     *
     * @throws SQLException when
     */
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBigDecimal ...
     *
     * @param columnIndex of type int
     * @param x           of type BigDecimal
     *
     * @throws SQLException when
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateString ...
     *
     * @param columnIndex of type int
     * @param x           of type String
     *
     * @throws SQLException when
     */
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBytes ...
     *
     * @param columnIndex of type int
     * @param x           of type byte[]
     *
     * @throws SQLException when
     */
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateDate ...
     *
     * @param columnIndex of type int
     * @param x           of type Date
     *
     * @throws SQLException when
     */
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateTime ...
     *
     * @param columnIndex of type int
     * @param x           of type Time
     *
     * @throws SQLException when
     */
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateTimestamp ...
     *
     * @param columnIndex of type int
     * @param x           of type Timestamp
     *
     * @throws SQLException when
     */
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnIndex of type int
     * @param x           of type Reader
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateObject ...
     *
     * @param columnIndex   of type int
     * @param x             of type Object
     * @param scaleOrLength of type int
     *
     * @throws SQLException when
     */
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateObject ...
     *
     * @param columnIndex of type int
     * @param x           of type Object
     *
     * @throws SQLException when
     */
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNull ...
     *
     * @param columnLabel of type String
     *
     * @throws SQLException when
     */
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBoolean ...
     *
     * @param columnLabel of type String
     * @param x           of type boolean
     *
     * @throws SQLException when
     */
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateByte ...
     *
     * @param columnLabel of type String
     * @param x           of type byte
     *
     * @throws SQLException when
     */
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateShort ...
     *
     * @param columnLabel of type String
     * @param x           of type short
     *
     * @throws SQLException when
     */
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateInt ...
     *
     * @param columnLabel of type String
     * @param x           of type int
     *
     * @throws SQLException when
     */
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateLong ...
     *
     * @param columnLabel of type String
     * @param x           of type long
     *
     * @throws SQLException when
     */
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateFloat ...
     *
     * @param columnLabel of type String
     * @param x           of type float
     *
     * @throws SQLException when
     */
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateDouble ...
     *
     * @param columnLabel of type String
     * @param x           of type double
     *
     * @throws SQLException when
     */
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBigDecimal ...
     *
     * @param columnLabel of type String
     * @param x           of type BigDecimal
     *
     * @throws SQLException when
     */
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateString ...
     *
     * @param columnLabel of type String
     * @param x           of type String
     *
     * @throws SQLException when
     */
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBytes ...
     *
     * @param columnLabel of type String
     * @param x           of type byte[]
     *
     * @throws SQLException when
     */
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateDate ...
     *
     * @param columnLabel of type String
     * @param x           of type Date
     *
     * @throws SQLException when
     */
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateTime ...
     *
     * @param columnLabel of type String
     * @param x           of type Time
     *
     * @throws SQLException when
     */
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateTimestamp ...
     *
     * @param columnLabel of type String
     * @param x           of type Timestamp
     *
     * @throws SQLException when
     */
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     * @param length      of type int
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateObject ...
     *
     * @param columnLabel   of type String
     * @param x             of type Object
     * @param scaleOrLength of type int
     *
     * @throws SQLException when
     */
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateObject ...
     *
     * @param columnLabel of type String
     * @param x           of type Object
     *
     * @throws SQLException when
     */
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method insertRow ...
     *
     * @throws SQLException when
     */
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateRow ...
     *
     * @throws SQLException when
     */
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method deleteRow ...
     *
     * @throws SQLException when
     */
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method refreshRow ...
     *
     * @throws SQLException when
     */
    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method cancelRowUpdates ...
     *
     * @throws SQLException when
     */
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method moveToInsertRow ...
     *
     * @throws SQLException when
     */
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method moveToCurrentRow ...
     *
     * @throws SQLException when
     */
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getStatement returns the statement of this TDHSResultSet object.
     *
     * @return the statement (type Statement) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public Statement getStatement() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getObject ...
     *
     * @param columnIndex of type int
     * @param map         of type Map<String, Class<?>>
     *
     * @return Object
     *
     * @throws SQLException when
     */
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getRef ...
     *
     * @param columnIndex of type int
     *
     * @return Ref
     *
     * @throws SQLException when
     */
    public Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getBlob ...
     *
     * @param columnIndex of type int
     *
     * @return Blob
     *
     * @throws SQLException when
     */
    public Blob getBlob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getClob ...
     *
     * @param columnIndex of type int
     *
     * @return Clob
     *
     * @throws SQLException when
     */
    public Clob getClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getArray ...
     *
     * @param columnIndex of type int
     *
     * @return Array
     *
     * @throws SQLException when
     */
    public Array getArray(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getObject ...
     *
     * @param columnLabel of type String
     * @param map         of type Map<String, Class<?>>
     *
     * @return Object
     *
     * @throws SQLException when
     */
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getRef ...
     *
     * @param columnLabel of type String
     *
     * @return Ref
     *
     * @throws SQLException when
     */
    public Ref getRef(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getBlob ...
     *
     * @param columnLabel of type String
     *
     * @return Blob
     *
     * @throws SQLException when
     */
    public Blob getBlob(String columnLabel) throws SQLException {
        return getBlob(this.findColumn(columnLabel));
    }

    /**
     * Method getClob ...
     *
     * @param columnLabel of type String
     *
     * @return Clob
     *
     * @throws SQLException when
     */
    public Clob getClob(String columnLabel) throws SQLException {
        return getClob(this.findColumn(columnLabel));
    }

    /**
     * Method getArray ...
     *
     * @param columnLabel of type String
     *
     * @return Array
     *
     * @throws SQLException when
     */
    public Array getArray(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getDate ...
     *
     * @param columnIndex of type int
     * @param cal         of type Calendar
     *
     * @return Date
     *
     * @throws SQLException when
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), cal);
        return time == null ? null : new Date(time);
    }

    /**
     * Method getDate ...
     *
     * @param columnLabel of type String
     * @param cal         of type Calendar
     *
     * @return Date
     *
     * @throws SQLException when
     */
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(this.findColumn(columnLabel)), cal);
        return time == null ? null : new Date(time);
    }

    /**
     * Method getTime ...
     *
     * @param columnIndex of type int
     * @param cal         of type Calendar
     *
     * @return Time
     *
     * @throws SQLException when
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), cal);
        return time == null ? null : new Time(time);
    }

    /**
     * Method getTime ...
     *
     * @param columnLabel of type String
     * @param cal         of type Calendar
     *
     * @return Time
     *
     * @throws SQLException when
     */
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(this.findColumn(columnLabel)), cal);
        return time == null ? null : new Time(time);
    }

    /**
     * Method getTimestamp ...
     *
     * @param columnIndex of type int
     * @param cal         of type Calendar
     *
     * @return Timestamp
     *
     * @throws SQLException when
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(columnIndex), cal);
        return time == null ? null : new Timestamp(time);
    }

    /**
     * Method getTimestamp ...
     *
     * @param columnLabel of type String
     * @param cal         of type Calendar
     *
     * @return Timestamp
     *
     * @throws SQLException when
     */
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        Long time = ConvertUtil.getTimeFromString(this.getString(this.findColumn(columnLabel)), cal);
        return time == null ? null : new Timestamp(time);
    }

    /**
     * Method getURL ...
     *
     * @param columnIndex of type int
     *
     * @return URL
     *
     * @throws SQLException when
     */
    public URL getURL(int columnIndex) throws SQLException {
        try {
            return new URL(this.getString(columnIndex));
        } catch (MalformedURLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Method getURL ...
     *
     * @param columnLabel of type String
     *
     * @return URL
     *
     * @throws SQLException when
     */
    public URL getURL(String columnLabel) throws SQLException {
        return this.getURL(this.findColumn(columnLabel));
    }

    /**
     * Method updateRef ...
     *
     * @param columnIndex of type int
     * @param x           of type Ref
     *
     * @throws SQLException when
     */
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateRef ...
     *
     * @param columnLabel of type String
     * @param x           of type Ref
     *
     * @throws SQLException when
     */
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnIndex of type int
     * @param x           of type Blob
     *
     * @throws SQLException when
     */
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnLabel of type String
     * @param x           of type Blob
     *
     * @throws SQLException when
     */
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnIndex of type int
     * @param x           of type Clob
     *
     * @throws SQLException when
     */
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnLabel of type String
     * @param x           of type Clob
     *
     * @throws SQLException when
     */
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateArray ...
     *
     * @param columnIndex of type int
     * @param x           of type Array
     *
     * @throws SQLException when
     */
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateArray ...
     *
     * @param columnLabel of type String
     * @param x           of type Array
     *
     * @throws SQLException when
     */
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getRowId ...
     *
     * @param columnIndex of type int
     *
     * @return RowId
     *
     * @throws SQLException when
     */
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getRowId ...
     *
     * @param columnLabel of type String
     *
     * @return RowId
     *
     * @throws SQLException when
     */
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateRowId ...
     *
     * @param columnIndex of type int
     * @param x           of type RowId
     *
     * @throws SQLException when
     */
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateRowId ...
     *
     * @param columnLabel of type String
     * @param x           of type RowId
     *
     * @throws SQLException when
     */
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getHoldability returns the holdability of this TDHSResultSet object.
     *
     * @return the holdability (type int) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method isClosed returns the closed of this TDHSResultSet object.
     *
     * @return the closed (type boolean) of this TDHSResultSet object.
     *
     * @throws SQLException when
     */
    public boolean isClosed() throws SQLException {
        return true;
    }

    /**
     * Method updateNString ...
     *
     * @param columnIndex of type int
     * @param nString     of type String
     *
     * @throws SQLException when
     */
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNString ...
     *
     * @param columnLabel of type String
     * @param nString     of type String
     *
     * @throws SQLException when
     */
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnIndex of type int
     * @param nClob       of type NClob
     *
     * @throws SQLException when
     */
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnLabel of type String
     * @param nClob       of type NClob
     *
     * @throws SQLException when
     */
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNClob ...
     *
     * @param columnIndex of type int
     *
     * @return NClob
     *
     * @throws SQLException when
     */
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNClob ...
     *
     * @param columnLabel of type String
     *
     * @return NClob
     *
     * @throws SQLException when
     */
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getSQLXML ...
     *
     * @param columnIndex of type int
     *
     * @return SQLXML
     *
     * @throws SQLException when
     */
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getSQLXML ...
     *
     * @param columnLabel of type String
     *
     * @return SQLXML
     *
     * @throws SQLException when
     */
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateSQLXML ...
     *
     * @param columnIndex of type int
     * @param xmlObject   of type SQLXML
     *
     * @throws SQLException when
     */
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateSQLXML ...
     *
     * @param columnLabel of type String
     * @param xmlObject   of type SQLXML
     *
     * @throws SQLException when
     */
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNString ...
     *
     * @param columnIndex of type int
     *
     * @return String
     *
     * @throws SQLException when
     */
    public String getNString(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNString ...
     *
     * @param columnLabel of type String
     *
     * @return String
     *
     * @throws SQLException when
     */
    public String getNString(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNCharacterStream ...
     *
     * @param columnIndex of type int
     *
     * @return Reader
     *
     * @throws SQLException when
     */
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getNCharacterStream ...
     *
     * @param columnLabel of type String
     *
     * @return Reader
     *
     * @throws SQLException when
     */
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNCharacterStream ...
     *
     * @param columnIndex of type int
     * @param x           of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNCharacterStream ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnIndex of type int
     * @param x           of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnIndex of type int
     * @param inputStream of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnLabel of type String
     * @param inputStream of type InputStream
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnIndex of type int
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnIndex of type int
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     * @param length      of type long
     *
     * @throws SQLException when
     */
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNCharacterStream ...
     *
     * @param columnIndex of type int
     * @param x           of type Reader
     *
     * @throws SQLException when
     */
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNCharacterStream ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnIndex of type int
     * @param x           of type InputStream
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnIndex of type int
     * @param x           of type Reader
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateAsciiStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     *
     * @throws SQLException when
     */
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBinaryStream ...
     *
     * @param columnLabel of type String
     * @param x           of type InputStream
     *
     * @throws SQLException when
     */
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateCharacterStream ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnIndex of type int
     * @param inputStream of type InputStream
     *
     * @throws SQLException when
     */
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateBlob ...
     *
     * @param columnLabel of type String
     * @param inputStream of type InputStream
     *
     * @throws SQLException when
     */
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnIndex of type int
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateClob ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnIndex of type int
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method updateNClob ...
     *
     * @param columnLabel of type String
     * @param reader      of type Reader
     *
     * @throws SQLException when
     */
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method getObject ...
     *
     * @param columnIndex of type int
     * @param type        of type Class<T>
     *
     * @return T
     *
     * @throws SQLException when
     */
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        try {
            return (T) getObject(columnIndex);
        } catch (ClassCastException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Method getObject ...
     *
     * @param columnLabel of type String
     * @param type        of type Class<T>
     *
     * @return T
     *
     * @throws SQLException when
     */
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        try {
            return (T) getObject(columnLabel);
        } catch (ClassCastException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Method unwrap ...
     *
     * @param iface of type Class<T>
     *
     * @return T
     *
     * @throws SQLException when
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            // This works for classes that aren't actually wrapping
            // anything
            return iface.cast(this);
        } catch (ClassCastException cce) {
            throw new SQLException(cce);
        }
    }

    /**
     * Method isWrapperFor ...
     *
     * @param iface of type Class<?>
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}
