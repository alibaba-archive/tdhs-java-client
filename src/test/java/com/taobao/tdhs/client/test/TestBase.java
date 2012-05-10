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

package com.taobao.tdhs.client.test;

import com.taobao.tdhs.client.TDHSClient;
import com.taobao.tdhs.client.TDHSClientImpl;
import com.taobao.tdhs.client.exception.TDHSException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

@Ignore
public class TestBase {
    protected static TDHSClient client;
    protected static String db;
    protected static String table;
    protected static String index;
    protected static String[] fields;

    private static Properties global;

    @BeforeClass
    public static void init() throws IOException {
        if (global == null || client == null) {
            global = new Properties();
            InputStream is = null;
            try {
                is = ClassLoader.getSystemResourceAsStream("global.properties");
                global.load(is);

                db = global.getProperty("db.name");
                table = global.getProperty("table.name");
                index = global.getProperty("index.name");
                fields = global.getProperty("field.name").split(",");
            } finally {
                if (is != null) {
                    is.close();
                }
            }

            try {
                client = new TDHSClientImpl(new InetSocketAddress(global.getProperty("server.host"),
                        Integer.parseInt(global.getProperty("server.port", "9999"))), 1, 3000, false, 3000);
            } catch (TDHSException e) {
                Assert.fail("Init exception:" + e.getMessage());
            }
        }
    }

    @AfterClass
    public static void destory() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }
}
