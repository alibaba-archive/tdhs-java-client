package com.taobao.tdhs.client.response;

import com.taobao.tdhs.client.util.MysqlUtil;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-9-20 下午5:13
 */
public class TDHSMySQLResultSetWrap implements ResultSet {

    private TDHSResultSet tdhsResultSet;

    /**
     * Constructor TDHSMySQLResultSetWrap creates a new TDHSMySQLResultSetWrap instance.
     *
     * @param tdhsResultSet of type TDHSResultSet
     */
    public TDHSMySQLResultSetWrap(@NotNull TDHSResultSet tdhsResultSet) {
        this.tdhsResultSet = tdhsResultSet;
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
        return tdhsResultSet.absolute(row);
    }

    /**
     * Method afterLast ...
     *
     * @throws SQLException when
     */
    public void afterLast() throws SQLException {
        tdhsResultSet.afterLast();
    }

    /**
     * Method beforeFirst ...
     *
     * @throws SQLException when
     */
    public void beforeFirst() throws SQLException {
        tdhsResultSet.beforeFirst();
    }

    /**
     * Method cancelRowUpdates ...
     *
     * @throws SQLException when
     */
    public void cancelRowUpdates() throws SQLException {
        tdhsResultSet.cancelRowUpdates();
    }

    /**
     * Method clearWarnings ...
     *
     * @throws SQLException when
     */
    public void clearWarnings() throws SQLException {
        tdhsResultSet.clearWarnings();
    }

    /**
     * Method close ...
     *
     * @throws SQLException when
     */
    public void close() throws SQLException {
        tdhsResultSet.close();
    }

    /**
     * Method deleteRow ...
     *
     * @throws SQLException when
     */
    public void deleteRow() throws SQLException {
        tdhsResultSet.deleteRow();
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
        return tdhsResultSet.findColumn(columnLabel);
    }

    /**
     * Method first ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean first() throws SQLException {
        return tdhsResultSet.first();
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
        return tdhsResultSet.getArray(columnIndex);
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
        return tdhsResultSet.getArray(columnLabel);
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
        return tdhsResultSet.getAsciiStream(columnIndex);
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
        return tdhsResultSet.getAsciiStream(columnLabel);
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
        return tdhsResultSet.getBigDecimal(columnIndex);
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
        return tdhsResultSet.getBigDecimal(columnIndex, scale);
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
        return tdhsResultSet.getBigDecimal(columnLabel);
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
        return tdhsResultSet.getBigDecimal(columnLabel, scale);
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
        return tdhsResultSet.getBinaryStream(columnIndex);
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
        return tdhsResultSet.getBinaryStream(columnLabel);
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
        return tdhsResultSet.getBlob(columnIndex);
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
        return tdhsResultSet.getBlob(columnLabel);
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
        return tdhsResultSet.getBoolean(columnIndex);
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
        return tdhsResultSet.getBoolean(columnLabel);
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
        return tdhsResultSet.getByte(columnIndex);
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
        return tdhsResultSet.getByte(columnLabel);
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
        return tdhsResultSet.getBytes(columnIndex);
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
        return tdhsResultSet.getBytes(columnLabel);
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
        return tdhsResultSet.getCharacterStream(columnIndex);
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
        return tdhsResultSet.getCharacterStream(columnLabel);
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
        return tdhsResultSet.getClob(columnIndex);
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
        return tdhsResultSet.getClob(columnLabel);
    }

    /**
     * Method getConcurrency returns the concurrency of this TDHSMySQLResultSetWrap object.
     *
     * @return the concurrency (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getConcurrency() throws SQLException {
        return tdhsResultSet.getConcurrency();
    }

    /**
     * Method getCursorName returns the cursorName of this TDHSMySQLResultSetWrap object.
     *
     * @return the cursorName (type String) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public String getCursorName() throws SQLException {
        return tdhsResultSet.getCursorName();
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
        return tdhsResultSet.getDate(columnIndex);
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
        return tdhsResultSet.getDate(columnIndex, cal);
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
        return tdhsResultSet.getDate(columnLabel);
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
        return tdhsResultSet.getDate(columnLabel, cal);
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
        return tdhsResultSet.getDouble(columnIndex);
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
        return tdhsResultSet.getDouble(columnLabel);
    }

    /**
     * Method getFetchDirection returns the fetchDirection of this TDHSMySQLResultSetWrap object.
     *
     * @return the fetchDirection (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getFetchDirection() throws SQLException {
        return tdhsResultSet.getFetchDirection();
    }

    /**
     * Method getFetchSize returns the fetchSize of this TDHSMySQLResultSetWrap object.
     *
     * @return the fetchSize (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getFetchSize() throws SQLException {
        return tdhsResultSet.getFetchSize();
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
        return tdhsResultSet.getFloat(columnIndex);
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
        return tdhsResultSet.getFloat(columnLabel);
    }

    /**
     * Method getHoldability returns the holdability of this TDHSMySQLResultSetWrap object.
     *
     * @return the holdability (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getHoldability() throws SQLException {
        return tdhsResultSet.getHoldability();
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
        return tdhsResultSet.getInt(columnIndex);
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
        return tdhsResultSet.getInt(columnLabel);
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
        return tdhsResultSet.getLong(columnIndex);
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
        return tdhsResultSet.getLong(columnLabel);
    }

    /**
     * Method getMetaData returns the metaData of this TDHSMySQLResultSetWrap object.
     *
     * @return the metaData (type ResultSetMetaData) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return tdhsResultSet.getMetaData();
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
        return tdhsResultSet.getNCharacterStream(columnIndex);
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
        return tdhsResultSet.getNCharacterStream(columnLabel);
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
        return tdhsResultSet.getNClob(columnIndex);
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
        return tdhsResultSet.getNClob(columnLabel);
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
        return tdhsResultSet.getNString(columnIndex);
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
        return tdhsResultSet.getNString(columnLabel);
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
        tdhsResultSet.checkRow(columnIndex);
        tdhsResultSet.checkType(columnIndex);
        TDHSResponseEnum.IFieldType iFieldType = tdhsResultSet.getFieldTypes().get(columnIndex - 1);
        if (iFieldType instanceof TDHSResponseEnum.FieldType) {
            TDHSResponseEnum.FieldType fieldType = (TDHSResponseEnum.FieldType) iFieldType;
            int sqlType = MysqlUtil.mysqlToJavaType(fieldType.getType());
            switch (sqlType) {
                case Types.BIT:
                case Types.BOOLEAN:
                    if (fieldType.equals(TDHSResponseEnum.FieldType.MYSQL_TYPE_BIT)) {
                        return getByte(columnIndex);
                    }
                    return getBoolean(columnIndex);
                case Types.TINYINT:
                case Types.SMALLINT:
                    return getInt(columnIndex);
                case Types.INTEGER:
                    if (fieldType.equals(TDHSResponseEnum.FieldType.MYSQL_TYPE_INT24)) {
                        return getInt(columnIndex);
                    }
                    if (getLong(columnIndex) <= Integer.MAX_VALUE) {
                        return getInt(columnIndex);
                    }
                    return getLong(columnIndex);
                case Types.BIGINT:
                    BigInteger bigInteger;
                    try {
                        String string = getString(columnIndex);
                        if (string == null) {
                            return null;
                        }
                        bigInteger = new BigInteger(string);
                    } catch (NumberFormatException e) {
                        throw new SQLException(e);
                    }
                    if (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
                        return getLong(columnIndex);
                    }
                    return bigInteger;
                case Types.DECIMAL:
                case Types.NUMERIC:
                    return getBigDecimal(columnIndex);
                case Types.REAL:
                    return getFloat(columnIndex);
                case Types.FLOAT:
                case Types.DOUBLE:
                    return getDouble(columnIndex);
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    return getString(columnIndex);
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                    return getByte(columnIndex);
                case Types.DATE:
                    if (fieldType.equals(TDHSResponseEnum.FieldType.MYSQL_TYPE_YEAR)) {
                        return getShort(columnIndex);
                    }
                    return getDate(columnIndex);
                case Types.TIME:
                    return getTime(columnIndex);
                case Types.TIMESTAMP:
                    return getTimestamp(columnIndex);
                default:
                    return getString(columnIndex);
            }

        }
        return null;

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
        return tdhsResultSet.getObject(columnIndex, map);
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
        return tdhsResultSet.getObject(columnIndex, type);
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
        return tdhsResultSet.getObject(columnLabel);
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
        return tdhsResultSet.getObject(columnLabel, map);
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
        return tdhsResultSet.getObject(columnLabel, type);
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
        return tdhsResultSet.getRef(columnIndex);
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
        return tdhsResultSet.getRef(columnLabel);
    }

    /**
     * Method getRow returns the row of this TDHSMySQLResultSetWrap object.
     *
     * @return the row (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getRow() throws SQLException {
        return tdhsResultSet.getRow();
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
        return tdhsResultSet.getRowId(columnIndex);
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
        return tdhsResultSet.getRowId(columnLabel);
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
        return tdhsResultSet.getShort(columnIndex);
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
        return tdhsResultSet.getShort(columnLabel);
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
        return tdhsResultSet.getSQLXML(columnIndex);
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
        return tdhsResultSet.getSQLXML(columnLabel);
    }

    /**
     * Method getStatement returns the statement of this TDHSMySQLResultSetWrap object.
     *
     * @return the statement (type Statement) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public Statement getStatement() throws SQLException {
        return tdhsResultSet.getStatement();
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
        return tdhsResultSet.getString(columnIndex);
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
        return tdhsResultSet.getString(columnLabel);
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
        return tdhsResultSet.getTime(columnIndex);
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
        return tdhsResultSet.getTime(columnIndex, cal);
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
        return tdhsResultSet.getTime(columnLabel);
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
        return tdhsResultSet.getTime(columnLabel, cal);
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
        return tdhsResultSet.getTimestamp(columnIndex);
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
        return tdhsResultSet.getTimestamp(columnIndex, cal);
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
        return tdhsResultSet.getTimestamp(columnLabel);
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
        return tdhsResultSet.getTimestamp(columnLabel, cal);
    }

    /**
     * Method getType returns the type of this TDHSMySQLResultSetWrap object.
     *
     * @return the type (type int) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public int getType() throws SQLException {
        return tdhsResultSet.getType();
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
        return tdhsResultSet.getUnicodeStream(columnIndex);
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
        return tdhsResultSet.getUnicodeStream(columnLabel);
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
        return tdhsResultSet.getURL(columnIndex);
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
        return tdhsResultSet.getURL(columnLabel);
    }

    /**
     * Method getWarnings returns the warnings of this TDHSMySQLResultSetWrap object.
     *
     * @return the warnings (type SQLWarning) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public SQLWarning getWarnings() throws SQLException {
        return tdhsResultSet.getWarnings();
    }

    /**
     * Method insertRow ...
     *
     * @throws SQLException when
     */
    public void insertRow() throws SQLException {
        tdhsResultSet.insertRow();
    }

    /**
     * Method isAfterLast returns the afterLast of this TDHSMySQLResultSetWrap object.
     *
     * @return the afterLast (type boolean) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public boolean isAfterLast() throws SQLException {
        return tdhsResultSet.isAfterLast();
    }

    /**
     * Method isBeforeFirst returns the beforeFirst of this TDHSMySQLResultSetWrap object.
     *
     * @return the beforeFirst (type boolean) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public boolean isBeforeFirst() throws SQLException {
        return tdhsResultSet.isBeforeFirst();
    }

    /**
     * Method isClosed returns the closed of this TDHSMySQLResultSetWrap object.
     *
     * @return the closed (type boolean) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public boolean isClosed() throws SQLException {
        return tdhsResultSet.isClosed();
    }

    /**
     * Method isFirst returns the first of this TDHSMySQLResultSetWrap object.
     *
     * @return the first (type boolean) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public boolean isFirst() throws SQLException {
        return tdhsResultSet.isFirst();
    }

    /**
     * Method isLast returns the last of this TDHSMySQLResultSetWrap object.
     *
     * @return the last (type boolean) of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public boolean isLast() throws SQLException {
        return tdhsResultSet.isLast();
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
        return tdhsResultSet.isWrapperFor(iface);
    }

    /**
     * Method last ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean last() throws SQLException {
        return tdhsResultSet.last();
    }

    /**
     * Method moveToCurrentRow ...
     *
     * @throws SQLException when
     */
    public void moveToCurrentRow() throws SQLException {
        tdhsResultSet.moveToCurrentRow();
    }

    /**
     * Method moveToInsertRow ...
     *
     * @throws SQLException when
     */
    public void moveToInsertRow() throws SQLException {
        tdhsResultSet.moveToInsertRow();
    }

    /**
     * Method next ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean next() throws SQLException {
        return tdhsResultSet.next();
    }

    /**
     * Method previous ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean previous() throws SQLException {
        return tdhsResultSet.previous();
    }

    /**
     * Method refreshRow ...
     *
     * @throws SQLException when
     */
    public void refreshRow() throws SQLException {
        tdhsResultSet.refreshRow();
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
        return tdhsResultSet.relative(rows);
    }

    /**
     * Method rowDeleted ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowDeleted() throws SQLException {
        return tdhsResultSet.rowDeleted();
    }

    /**
     * Method rowInserted ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowInserted() throws SQLException {
        return tdhsResultSet.rowInserted();
    }

    /**
     * Method rowUpdated ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean rowUpdated() throws SQLException {
        return tdhsResultSet.rowUpdated();
    }

    /**
     * Method setFetchDirection sets the fetchDirection of this TDHSMySQLResultSetWrap object.
     *
     * @param direction the fetchDirection of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public void setFetchDirection(int direction) throws SQLException {
        tdhsResultSet.setFetchDirection(direction);
    }

    /**
     * Method setFetchSize sets the fetchSize of this TDHSMySQLResultSetWrap object.
     *
     * @param rows the fetchSize of this TDHSMySQLResultSetWrap object.
     *
     * @throws SQLException when
     */
    public void setFetchSize(int rows) throws SQLException {
        tdhsResultSet.setFetchSize(rows);
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
        return tdhsResultSet.unwrap(iface);
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
        tdhsResultSet.updateArray(columnIndex, x);
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
        tdhsResultSet.updateArray(columnLabel, x);
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
        tdhsResultSet.updateAsciiStream(columnIndex, x);
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
        tdhsResultSet.updateAsciiStream(columnIndex, x, length);
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
        tdhsResultSet.updateAsciiStream(columnIndex, x, length);
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
        tdhsResultSet.updateAsciiStream(columnLabel, x);
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
        tdhsResultSet.updateAsciiStream(columnLabel, x, length);
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
        tdhsResultSet.updateAsciiStream(columnLabel, x, length);
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
        tdhsResultSet.updateBigDecimal(columnIndex, x);
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
        tdhsResultSet.updateBigDecimal(columnLabel, x);
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
        tdhsResultSet.updateBinaryStream(columnIndex, x);
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
        tdhsResultSet.updateBinaryStream(columnIndex, x, length);
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
        tdhsResultSet.updateBinaryStream(columnIndex, x, length);
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
        tdhsResultSet.updateBinaryStream(columnLabel, x);
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
        tdhsResultSet.updateBinaryStream(columnLabel, x, length);
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
        tdhsResultSet.updateBinaryStream(columnLabel, x, length);
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
        tdhsResultSet.updateBlob(columnIndex, inputStream);
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
        tdhsResultSet.updateBlob(columnIndex, inputStream, length);
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
        tdhsResultSet.updateBlob(columnIndex, x);
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
        tdhsResultSet.updateBlob(columnLabel, inputStream);
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
        tdhsResultSet.updateBlob(columnLabel, inputStream, length);
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
        tdhsResultSet.updateBlob(columnLabel, x);
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
        tdhsResultSet.updateBoolean(columnIndex, x);
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
        tdhsResultSet.updateBoolean(columnLabel, x);
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
        tdhsResultSet.updateByte(columnIndex, x);
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
        tdhsResultSet.updateByte(columnLabel, x);
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
        tdhsResultSet.updateBytes(columnIndex, x);
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
        tdhsResultSet.updateBytes(columnLabel, x);
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
        tdhsResultSet.updateCharacterStream(columnIndex, x);
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
        tdhsResultSet.updateCharacterStream(columnIndex, x, length);
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
        tdhsResultSet.updateCharacterStream(columnIndex, x, length);
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
        tdhsResultSet.updateCharacterStream(columnLabel, reader);
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
        tdhsResultSet.updateCharacterStream(columnLabel, reader, length);
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
        tdhsResultSet.updateCharacterStream(columnLabel, reader, length);
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
        tdhsResultSet.updateClob(columnIndex, reader);
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
        tdhsResultSet.updateClob(columnIndex, reader, length);
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
        tdhsResultSet.updateClob(columnIndex, x);
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
        tdhsResultSet.updateClob(columnLabel, reader);
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
        tdhsResultSet.updateClob(columnLabel, reader, length);
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
        tdhsResultSet.updateClob(columnLabel, x);
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
        tdhsResultSet.updateDate(columnIndex, x);
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
        tdhsResultSet.updateDate(columnLabel, x);
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
        tdhsResultSet.updateDouble(columnIndex, x);
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
        tdhsResultSet.updateDouble(columnLabel, x);
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
        tdhsResultSet.updateFloat(columnIndex, x);
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
        tdhsResultSet.updateFloat(columnLabel, x);
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
        tdhsResultSet.updateInt(columnIndex, x);
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
        tdhsResultSet.updateInt(columnLabel, x);
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
        tdhsResultSet.updateLong(columnIndex, x);
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
        tdhsResultSet.updateLong(columnLabel, x);
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
        tdhsResultSet.updateNCharacterStream(columnIndex, x);
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
        tdhsResultSet.updateNCharacterStream(columnIndex, x, length);
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
        tdhsResultSet.updateNCharacterStream(columnLabel, reader);
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
        tdhsResultSet.updateNCharacterStream(columnLabel, reader, length);
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
        tdhsResultSet.updateNClob(columnIndex, nClob);
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
        tdhsResultSet.updateNClob(columnIndex, reader);
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
        tdhsResultSet.updateNClob(columnIndex, reader, length);
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
        tdhsResultSet.updateNClob(columnLabel, nClob);
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
        tdhsResultSet.updateNClob(columnLabel, reader);
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
        tdhsResultSet.updateNClob(columnLabel, reader, length);
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
        tdhsResultSet.updateNString(columnIndex, nString);
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
        tdhsResultSet.updateNString(columnLabel, nString);
    }

    /**
     * Method updateNull ...
     *
     * @param columnIndex of type int
     *
     * @throws SQLException when
     */
    public void updateNull(int columnIndex) throws SQLException {
        tdhsResultSet.updateNull(columnIndex);
    }

    /**
     * Method updateNull ...
     *
     * @param columnLabel of type String
     *
     * @throws SQLException when
     */
    public void updateNull(String columnLabel) throws SQLException {
        tdhsResultSet.updateNull(columnLabel);
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
        tdhsResultSet.updateObject(columnIndex, x);
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
        tdhsResultSet.updateObject(columnIndex, x, scaleOrLength);
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
        tdhsResultSet.updateObject(columnLabel, x);
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
        tdhsResultSet.updateObject(columnLabel, x, scaleOrLength);
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
        tdhsResultSet.updateRef(columnIndex, x);
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
        tdhsResultSet.updateRef(columnLabel, x);
    }

    /**
     * Method updateRow ...
     *
     * @throws SQLException when
     */
    public void updateRow() throws SQLException {
        tdhsResultSet.updateRow();
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
        tdhsResultSet.updateRowId(columnIndex, x);
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
        tdhsResultSet.updateRowId(columnLabel, x);
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
        tdhsResultSet.updateShort(columnIndex, x);
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
        tdhsResultSet.updateShort(columnLabel, x);
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
        tdhsResultSet.updateSQLXML(columnIndex, xmlObject);
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
        tdhsResultSet.updateSQLXML(columnLabel, xmlObject);
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
        tdhsResultSet.updateString(columnIndex, x);
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
        tdhsResultSet.updateString(columnLabel, x);
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
        tdhsResultSet.updateTime(columnIndex, x);
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
        tdhsResultSet.updateTime(columnLabel, x);
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
        tdhsResultSet.updateTimestamp(columnIndex, x);
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
        tdhsResultSet.updateTimestamp(columnLabel, x);
    }

    /**
     * Method wasNull ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean wasNull() throws SQLException {
        return tdhsResultSet.wasNull();
    }
}
