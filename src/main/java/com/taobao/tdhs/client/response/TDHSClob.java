package com.taobao.tdhs.client.response;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-29 上午11:13
 */
public class TDHSClob implements Clob {

    private final String value;

    /**
     * Constructor TDHSClob creates a new TDHSClob instance.
     *
     * @param value of type String
     */
    public TDHSClob(String value) {
        this.value = value;
    }


    /**
     * Method length ...
     *
     * @return long
     *
     * @throws SQLException when
     */
    public long length() throws SQLException {
        return value.length();
    }

    /**
     * Method getSubString ...
     *
     * @param pos    of type long
     * @param length of type int
     *
     * @return String
     *
     * @throws SQLException when
     */
    public String getSubString(long pos, int length) throws SQLException {
        try {
            return value.substring((int) pos, (int) pos + length);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    /**
     * Method getCharacterStream returns the characterStream of this TDHSClob object.
     *
     * @return the characterStream (type Reader) of this TDHSClob object.
     *
     * @throws SQLException when
     */
    public Reader getCharacterStream() throws SQLException {
        if (value == null) {
            return new StringReader("");
        }
        return new StringReader(value);
    }

    /**
     * Method getAsciiStream returns the asciiStream of this TDHSClob object.
     *
     * @return the asciiStream (type InputStream) of this TDHSClob object.
     *
     * @throws SQLException when
     */
    public InputStream getAsciiStream() throws SQLException {
        if (value == null) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return new ByteArrayInputStream(value.getBytes());
    }

    /**
     * Method position ...
     *
     * @param searchstr of type String
     * @param start     of type long
     *
     * @return long
     *
     * @throws SQLException when
     */
    public long position(String searchstr, long start) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method position ...
     *
     * @param searchstr of type Clob
     * @param start     of type long
     *
     * @return long
     *
     * @throws SQLException when
     */
    public long position(Clob searchstr, long start) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method setString ...
     *
     * @param pos of type long
     * @param str of type String
     *
     * @return int
     *
     * @throws SQLException when
     */
    public int setString(long pos, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method setString ...
     *
     * @param pos    of type long
     * @param str    of type String
     * @param offset of type int
     * @param len    of type int
     *
     * @return int
     *
     * @throws SQLException when
     */
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method setAsciiStream ...
     *
     * @param pos of type long
     *
     * @return OutputStream
     *
     * @throws SQLException when
     */
    public OutputStream setAsciiStream(long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method setCharacterStream ...
     *
     * @param pos of type long
     *
     * @return Writer
     *
     * @throws SQLException when
     */
    public Writer setCharacterStream(long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method truncate ...
     *
     * @param len of type long
     *
     * @throws SQLException when
     */
    public void truncate(long len) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Method free ...
     *
     * @throws SQLException when
     */
    public void free() throws SQLException {
    }

    /**
     * Method getCharacterStream ...
     *
     * @param pos    of type long
     * @param length of type long
     *
     * @return Reader
     *
     * @throws SQLException when
     */
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        return new StringReader(getSubString(pos, (int) length));
    }
}
