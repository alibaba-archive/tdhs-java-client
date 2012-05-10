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

package com.taobao.tdhs.jdbc.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-9 下午1:53
 */
public class StringUtilTest {
    @Test
    public void testEscapeField() throws Exception {
        Assert.assertEquals("id", StringUtil.escapeField("id"));
        Assert.assertEquals("id", StringUtil.escapeField("`id`"));
        Assert.assertEquals("i\"d", StringUtil.escapeField("`i\"d`"));
        Assert.assertEquals("", StringUtil.escapeField("``"));
        Assert.assertEquals(null, StringUtil.escapeField("\"id\""));
        Assert.assertEquals(null, StringUtil.escapeField("\'id\'"));
        Assert.assertEquals(null, StringUtil.escapeField("`"));
        Assert.assertEquals(null, StringUtil.escapeField("`id"));
        Assert.assertEquals(null, StringUtil.escapeField("id`"));
    }

    @Test
    public void testEscapeValue() throws Exception {
        Assert.assertEquals("123", StringUtil.escapeValue("123"));
        Assert.assertEquals("123", StringUtil.escapeValue("'123'"));
        Assert.assertEquals("123", StringUtil.escapeValue("\"123\""));
        Assert.assertEquals(null, StringUtil.escapeValue("`123\""));
        Assert.assertEquals("", StringUtil.escapeValue("\"\""));
        Assert.assertEquals(null, StringUtil.escapeValue("\""));
        Assert.assertEquals(null, StringUtil.escapeValue("\'"));
        Assert.assertEquals(null, StringUtil.escapeValue("\'123\""));
        Assert.assertEquals(null, StringUtil.escapeValue("  \"123\'"));
        Assert.assertEquals(null, StringUtil.escapeValue("  \"123"));
        Assert.assertEquals(null, StringUtil.escapeValue("  \'123"));
        Assert.assertEquals(null, StringUtil.escapeValue("  123\'"));
        Assert.assertEquals(null, StringUtil.escapeValue("  123\""));
    }

    @Test
    public void testEscapeIn() throws Exception {
        Assert.assertArrayEquals(null, StringUtil.escapeIn("123"));
        Assert.assertArrayEquals(null, StringUtil.escapeIn("(123"));
        Assert.assertArrayEquals(null, StringUtil.escapeIn("123)"));
        Assert.assertArrayEquals(null, StringUtil.escapeIn("(\"123)"));
        Assert.assertArrayEquals(null, StringUtil.escapeIn("(`123)"));
        Assert.assertArrayEquals(new String[]{}, StringUtil.escapeIn("()"));
        Assert.assertArrayEquals(new String[]{"123"}, StringUtil.escapeIn("(123)"));
        Assert.assertArrayEquals(new String[]{"123"}, StringUtil.escapeIn("(\"123\")"));
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, StringUtil.escapeIn("(\"1\",2,'3')"));
    }
}
