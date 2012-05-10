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
import org.junit.Ignore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-20 下午4:50
 */
@Ignore
public class TestBase {

    private static Properties global;

    private static int mysqlPort;

    private static int tdhsPort;

    private static String host;

    @BeforeClass
    public static void _init() throws IOException {
        if (global == null) {
            global = new Properties();
            InputStream is = null;
            try {
                is = ClassLoader.getSystemResourceAsStream("global.properties");
                global.load(is);

                mysqlPort = Integer.parseInt(global.getProperty("mysql.port"));
                tdhsPort = Integer.parseInt(global.getProperty("server.port"));
                host = global.getProperty("server.host");
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    public static Connection getMySQLConnection() throws SQLException,
            java.lang.ClassNotFoundException {
        String url = "jdbc:mysql://" + host + ":" + mysqlPort + "/jdbc_test";
        Class.forName("com.mysql.jdbc.Driver");
        String userName = "test";
        String password = "test";
        return DriverManager.getConnection(url, userName, password);
    }

    public static Connection getTDHSConnection() throws SQLException,
            java.lang.ClassNotFoundException {
        String url = "jdbc:tdhs://" + host + ":" + tdhsPort + "/jdbc_test";
        Class.forName("com.taobao.tdhs.jdbc.Driver");
        return DriverManager.getConnection(url, null, null);
    }

    public static void truncate(String table) throws ClassNotFoundException, SQLException {
        Connection connection = getMySQLConnection();
        Statement statement = connection.createStatement();
        statement.execute("set global tdh_socket_cache_table_on  =0;");  //percona 5.5的truncate会重置自增id导致会lock表的meta
        statement.execute("truncate table " + table + ";");
        statement.execute("set global tdh_socket_cache_table_on  =1;");
        statement.close();
        connection.close();
    }
}
