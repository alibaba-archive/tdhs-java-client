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

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-7 上午10:15
 */
public class NonRegisteringDriverTest {


    @Test
    public void testParseURLDone() throws Exception {
        NonRegisteringDriver driver = new NonRegisteringDriver();
        Properties properties = driver.parseURL("jdbc:tdhs://1.2.3.4/testdb?a=1&b=2", null);
        Assert.assertEquals(5, properties.size());
        Assert.assertEquals("1.2.3.4", properties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY));
        Assert.assertEquals("9999", properties.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY));
        Assert.assertEquals("testdb", properties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY));
        Assert.assertEquals("1", properties.getProperty("a"));
        Assert.assertEquals("2", properties.getProperty("b"));
    }

    @Test
    public void testParseURLDoneWithPort() throws Exception {
        NonRegisteringDriver driver = new NonRegisteringDriver();
        Properties properties = driver.parseURL("jdbc:tdhs://1.2.3.4:8888/testdb?a=1&b=2", null);
        Assert.assertEquals(5, properties.size());
        Assert.assertEquals("1.2.3.4", properties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY));
        Assert.assertEquals("8888", properties.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY));
        Assert.assertEquals("testdb", properties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY));
        Assert.assertEquals("1", properties.getProperty("a"));
        Assert.assertEquals("2", properties.getProperty("b"));
    }

    @Test
    public void testParseURLDoneWithPortAndDefault() throws Exception {
        NonRegisteringDriver driver = new NonRegisteringDriver();
        Properties defaultProperties = new Properties();
        defaultProperties.put("c", "3");
        defaultProperties.put("d", "4");
        Properties properties = driver.parseURL("jdbc:tdhs://1.2.3.4:8888/testdb?a=1&b=2", defaultProperties);
        Assert.assertEquals(5, properties.size());
        Assert.assertEquals("1.2.3.4", properties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY));
        Assert.assertEquals("8888", properties.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY));
        Assert.assertEquals("testdb", properties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY));
        Assert.assertEquals("1", properties.getProperty("a"));
        Assert.assertEquals("2", properties.getProperty("b"));
        Assert.assertEquals("3", properties.getProperty("c"));
        Assert.assertEquals("4", properties.getProperty("d"));
    }

    @Test
    public void testParseURLDoneWithNone() throws Exception {
        NonRegisteringDriver driver = new NonRegisteringDriver();
        Properties properties = driver.parseURL("jdbc:tdhs://", null);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("localhost", properties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY));
        Assert.assertEquals("9999", properties.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY));
        Assert.assertEquals(null, properties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY));

    }

    @Test
    public void testParseURLError() throws Exception {
        NonRegisteringDriver driver = new NonRegisteringDriver();
        Properties properties = driver.parseURL(null, null);
        Assert.assertNull(properties);
        properties = driver.parseURL("jdbc:mysql://1.2.3.4:8888/testdb?a=1&b=2", null);
        Assert.assertNull(properties);
    }

}
