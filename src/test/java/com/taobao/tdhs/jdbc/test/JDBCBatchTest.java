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

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-4-12 上午10:20
 */
public class JDBCBatchTest extends TestBase {
    private static DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Date DATE;

    static {
        try {
            DATE = new Date(DATA_FORMAT.parse("2012-03-20 16:58:00").getTime());
        } catch (ParseException e) {
        }
    }

    private static Object[][] DATA_FOR_DONE =
            {{1, "batch_aa", 1, 1.1f, null, DATE}, {2, "batch_bb", 2, 2.2f, null, DATE},
                    {3, "batch_cc", 3, 3.3f, null, DATE}};

    private static Object[][] DATA_FOR_FAILED =
            {{7, "batch_aa", 1, 1.1f, null, DATE}, {8, "batch_bb", 2, 2.2f, null, DATE},
                    {7, "batch_cc", 3, 3.3f, null, DATE}};


    @BeforeClass
    public static void cleanTable() throws ClassNotFoundException, SQLException {
        truncate("test");
    }

    @Test
    public void testBatchDone() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        PreparedStatement statement =
                connection.prepareStatement("insert into test(id,a,b,c,n,t ,now) values (?,?,?,?,?,?,now())");
        for (Object[] o : DATA_FOR_DONE) {
            statement.setInt(1, (Integer) o[0]);
            statement.setString(2, (String) o[1]);
            statement.setInt(3, (Integer) o[2]);
            statement.setFloat(4, (Float) o[3]);
            statement.setNull(5, 0);
            statement.setDate(6, (Date) o[5]);
            statement.addBatch();
        }
        int[] res = statement.executeBatch();
        Assert.assertEquals(DATA_FOR_DONE.length, res.length);
        Assert.assertEquals(0, res[0]);
        Assert.assertEquals(0, res[1]);
        Assert.assertEquals(0, res[2]);


    }

    @Test
    public void testBatchDone2() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        PreparedStatement statement =
                connection.prepareStatement("insert into test(a,b,c,n,t ,now) values (?,?,?,?,?,now())");
        for (Object[] o : DATA_FOR_DONE) {
            statement.setString(1, (String) o[1]);
            statement.setInt(2, (Integer) o[2]);
            statement.setFloat(3, (Float) o[3]);
            statement.setNull(4, 0);
            statement.setDate(5, (Date) o[5]);
            statement.addBatch();
        }
        int[] res = statement.executeBatch();
        Assert.assertEquals(DATA_FOR_DONE.length, res.length);
        Assert.assertEquals(4, res[0]);
        Assert.assertEquals(5, res[1]);
        Assert.assertEquals(6, res[2]);


    }

    @Test(expected = SQLException.class)
    public void testBatchFailed() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        PreparedStatement statement =
                connection.prepareStatement("insert into test(id,a,b,c,n,t ,now) values (?,?,?,?,?,?,now())");
        for (Object[] o : DATA_FOR_FAILED) {
            statement.setInt(1, (Integer) o[0]);
            statement.setString(2, (String) o[1]);
            statement.setInt(3, (Integer) o[2]);
            statement.setFloat(4, (Float) o[3]);
            statement.setNull(5, 0);
            statement.setDate(6, (Date) o[5]);
            statement.addBatch();
        }
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
