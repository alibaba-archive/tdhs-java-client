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

package com.taobao.tdhs.jdbc.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-20 下午4:50
 */
public class CRUDTest extends TestBase {
    private static DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Date DATE;

    static {
        try {
            DATE = new Date(DATA_FORMAT.parse("2012-03-20 16:58:00").getTime());
        } catch (ParseException e) {
        }
    }

    private static Object[][] DATA = {{"aa", 1, 1.1f, null, DATE}, {"bb", 2, 2.2f, null, DATE},
            {"cc", 3, 3.3f, null, DATE}};


    @BeforeClass
    public static void cleanTable() throws ClassNotFoundException, SQLException {
        truncate("test");
    }

    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        PreparedStatement statement =
                connection.prepareStatement("insert into test(a,b,c,n,t ,now) values (?,?,?,?,?,now())");
        for (Object[] o : DATA) {
            statement.setString(1, (String) o[0]);
            statement.setInt(2, (Integer) o[1]);
            statement.setFloat(3, (Float) o[2]);
            statement.setNull(4, 0);
            statement.setDate(5, (Date) o[4]);
            int r = statement.executeUpdate();
            assertEquals(1, r);
        }
        statement.close();
        connection.close();
    }

    public static void executeSelect(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute("select a,b,c,n,t as time from test where id>0");
            assertTrue(r);
            resultSet = statement.getResultSet();
            int size = 0;
            while (resultSet.next()) {
                assertEquals(DATA[size][0], resultSet.getString(1));
                assertEquals(DATA[size][1], resultSet.getInt(2));
                assertEquals(DATA[size][2], resultSet.getFloat("c"));
                assertNull(resultSet.getString("n"));
                assertEquals(DATA_FORMAT.format(DATA[size][4]),
                        DATA_FORMAT.format(resultSet.getTimestamp("time")));
                size++;
            }
            assertEquals(DATA.length, size);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }

    @Test
    public void testMySQLGetData1() throws ClassNotFoundException, SQLException {
        executeSelect(getMySQLConnection());
    }

    @Test
    public void testTDHSGetData1() throws ClassNotFoundException, SQLException {
        executeSelect(getTDHSConnection());
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException, InterruptedException {
        Connection connection = getTDHSConnection();
        Statement statement =
                connection.createStatement();
        int i = statement.executeUpdate("update test set a = 'kevin' , now=now() where b>1");
        assertEquals(2, i);
        statement.close();
        connection.close();
        DATA[1][0] = "kevin";
        DATA[2][0] = "kevin";
    }

    @Test
    public void testMySQLGetData2() throws ClassNotFoundException, SQLException {
        executeSelect(getMySQLConnection());
    }

    @Test
    public void testTDHSGetData2() throws ClassNotFoundException, SQLException {
        executeSelect(getTDHSConnection());
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        Statement statement =
                connection.createStatement();
        int i = statement.executeUpdate("delete from test where b = 3");
        assertEquals(1, i);
        statement.close();
        connection.close();
        DATA = Arrays.copyOf(DATA, 2);
    }

    @Test
    public void testMySQLGetData3() throws ClassNotFoundException, SQLException {
        executeSelect(getMySQLConnection());
    }

    @Test
    public void testTDHSGetData3() throws ClassNotFoundException, SQLException {
        executeSelect(getTDHSConnection());
    }

    @Test
    public void testTDHSGetBlob() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute("select a,b,c,n,t as time from test where id>0");
            assertTrue(r);
            resultSet = statement.getResultSet();
            int size = 0;
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob(1);
                String expected = (String) DATA[size][0];
                assertEquals(expected.getBytes().length, blob.length());
                for (int i = 0; i < expected.getBytes().length; i++) {
                    assertEquals(expected.getBytes()[i], blob.getBytes(0, (int) blob.length())[i]);
                }
                size++;
            }
            assertEquals(DATA.length, size);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }

    @Test
    public void testTDHSGetClob() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute("select a,b,c,n,t as time from test where id>0");
            assertTrue(r);
            resultSet = statement.getResultSet();
            int size = 0;
            while (resultSet.next()) {
                Clob clob = resultSet.getClob(1);
                String expected = (String) DATA[size][0];
                assertEquals(expected.getBytes().length, clob.length());
                assertEquals(expected, clob.getSubString(0, (int) clob.length()));
                size++;
            }
            assertEquals(DATA.length, size);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }


}
