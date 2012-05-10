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

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-20 下午3:47
 */
public class SimpleSelectJDBCTest extends TestBase {

    private static Object[][] data =
            {{1, "Kevin", 30, "Coder"}, {2, "Vivian", 30, "Wife"}, {3, "Kitty", 2, "Daughter"}};

    public static void executeSelect(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute("select p.id,p.name,p.age,p.memo as `m` from person as p where id>0");
            Assert.assertTrue(r);
            resultSet = statement.getResultSet();
            int size = 0;
            while (resultSet.next()) {
                Assert.assertEquals(data[size][0], resultSet.getInt(1));
                Assert.assertEquals(data[size][1], resultSet.getString(2));
                Assert.assertEquals(data[size][2], resultSet.getInt("age"));
                Assert.assertEquals(data[size][3], resultSet.getString("m"));
                size++;
            }
            Assert.assertEquals(data.length, size);
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

    public static void executeCount(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute("select count(*) as c from person where id>0");
            Assert.assertTrue(r);
            resultSet = statement.getResultSet();
            int size = 0;
            while (resultSet.next()) {
                Assert.assertEquals(data.length, resultSet.getInt(1));
                Assert.assertEquals(data.length, resultSet.getInt("c"));
                size++;
            }
            Assert.assertEquals(1, size);
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
    public void testMySQLGetData() throws ClassNotFoundException, SQLException {
        executeSelect(getMySQLConnection());
    }

    @Test
    public void testTDHSGetData() throws ClassNotFoundException, SQLException {
        executeSelect(getTDHSConnection());
    }

    @Test
    public void testMySQLGetCount() throws ClassNotFoundException, SQLException {
        executeCount(getMySQLConnection());
    }

    @Test
    public void testTDHSGetCount() throws ClassNotFoundException, SQLException {
        executeCount(getTDHSConnection());
    }
}
