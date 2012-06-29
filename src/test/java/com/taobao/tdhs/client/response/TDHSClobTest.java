package com.taobao.tdhs.client.response;

import junit.framework.Assert;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-29 上午11:54
 */
public class TDHSClobTest {
    private String value = "1234567890";

    private TDHSClob clob = new TDHSClob(value);

    @Test
    public void testLength() throws Exception {
        Assert.assertEquals(value.length(), clob.length());
    }

    @Test
    public void testGetSubString() throws Exception {
        Assert.assertEquals("123", clob.getSubString(1, 3));
        Assert.assertEquals("2345", clob.getSubString(2, 4));

    }

    @Test
    public void testPosition1() throws Exception {
        Assert.assertEquals(2, clob.position("234", 1));
    }

    @Test
    public void testPosition2() throws Exception {
        Assert.assertEquals(3, clob.position(new TDHSClob("34"), 1));

    }

    @Test(expected = SQLException.class)
    public void testPositionError() throws Exception {
        Assert.assertEquals(2, clob.position("234", 0));
    }
}
