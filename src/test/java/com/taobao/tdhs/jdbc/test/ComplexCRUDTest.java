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

import au.com.bytecode.opencsv.CSVReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-21 上午10:36
 */
public class ComplexCRUDTest extends TestBase {

    private static DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static List<String[]> DATA;

    @BeforeClass
    public static void init() throws ClassNotFoundException, SQLException, IOException {
        truncate("orders");
        CSVReader reader = new CSVReader(
                new InputStreamReader(ClassLoader.getSystemResourceAsStream("order.csv")));
        DATA = reader.readAll();
        DATA.remove(0);     //remove header
    }

    public List<String[]> executeSelect(Connection connection, String sql) throws SQLException {
        List<String[]> ret = new ArrayList<String[]>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute(sql);
            assertTrue(r);
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                String[] row = new String[8];
                row[0] = resultSet.getString(1);
                row[1] = resultSet.getString(2);
                row[2] = resultSet.getString(3);
                row[3] = resultSet.getString(4);
                row[4] = resultSet.getString(5);
                row[5] = resultSet.getString(6);
                row[6] = resultSet.getString(7).substring(0, 19);
                row[7] = resultSet.getString(8).substring(0, 19);
                ret.add(row);
            }
            return ret;
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

    private void compareRecord(List<String[]> mysqlRecord, List<String[]> tdhsRecord) {
        assertEquals(mysqlRecord.size(), tdhsRecord.size());
        for (int i = 0; i < mysqlRecord.size(); i++) {
            for (int j = 0; j < 8; j++) {
                assertEquals(mysqlRecord.get(i)[j], tdhsRecord.get(i)[j]);
            }
        }
    }


    @Test
    public void testInsert() throws ClassNotFoundException, SQLException, ParseException {
        Connection connection = getTDHSConnection();
        PreparedStatement statement =
                connection.prepareStatement(
                        "insert into orders(user_id,auction_id,auction_name,number,deleted,created,last_modify) values (?,?,?,?,?,?,now())");
        for (String[] o : DATA) {
            statement.setInt(1, Integer.valueOf(o[0]));
            statement.setInt(2, Integer.valueOf(o[1]));
            statement.setString(3, o[2]);
            statement.setInt(4, Integer.valueOf(o[3]));
            statement.setInt(5, Integer.valueOf(o[4]));
            statement.setDate(6, new Date(DATA_FORMAT.parse(o[5]).getTime()));
            int r = statement.executeUpdate();
            assertEquals(1, r);
        }
        statement.close();
        connection.close();
    }

    @Test
    public void testInsertDone() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>0";
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        assertEquals(DATA.size(), tdhsRecord.size());
        for (int i = 0; i < DATA.size(); i++) {
            for (int j = 1; j < 7; j++) {
                assertEquals(DATA.get(i)[j - 1], tdhsRecord.get(i)[j]);
            }
        }
    }

    @Test
    public void testSelect1() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>0";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }


    @Test
    public void testSelect2() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect3() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>2";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect4() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 and user_id = 1";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }


    @Test
    public void testSelect5() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 and user_id = 1 and auction_id = 1";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect6() throws ClassNotFoundException, SQLException {
        String sql =
                "select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where user_id =1 order by auction_id;";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect7() throws ClassNotFoundException, SQLException {
        String sql =
                "select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where user_id =1 order by auction_id desc;";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect8() throws ClassNotFoundException, SQLException {
        String sql =
                "select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where user_id =1 and auction_name!=\"Book\" order by auction_id desc;";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testSelect9() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 and (user_id = 1 and auction_id = 1)";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }


    @Test
    public void testdelete() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        Statement statement = connection.createStatement();
        int i = statement.executeUpdate("delete from orders where user_id =1 and auction_name!=\"Book\"");
        assertEquals(3, i);
        statement.close();
        connection.close();
    }

    @Test
    public void testDeleteDone() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>0";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException {
        Connection connection = getTDHSConnection();
        Statement statement = connection.createStatement();
        int i = statement
                .executeUpdate(
                        "update orders set number = number +1 , last_modify=now() where user_id =2 and auction_name!=\"Book\"");
        assertEquals(2, i);
        statement.close();
        connection.close();
    }

    @Test
    public void testUpdateDone() throws ClassNotFoundException, SQLException {
        String sql =
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>0";
        List<String[]> mysqlRecord = executeSelect(getMySQLConnection(), sql);
        List<String[]> tdhsRecord = executeSelect(getTDHSConnection(), sql);
        compareRecord(mysqlRecord, tdhsRecord);
    }

}
