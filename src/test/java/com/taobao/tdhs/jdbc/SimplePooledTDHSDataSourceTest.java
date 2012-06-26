package com.taobao.tdhs.jdbc;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-26 下午5:28
 */
public class SimplePooledTDHSDataSourceTest {
    private static int tdhsPort;
    private static String host;


    @BeforeClass
    public static void _init() throws IOException {
        Properties global = new Properties();
        InputStream is = null;
        try {
            is = ClassLoader.getSystemResourceAsStream("global.properties");
            global.load(is);

            tdhsPort = Integer.parseInt(global.getProperty("server.port"));
            host = global.getProperty("server.host");
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }


    @Test
    public void testGetConnection() throws Exception {
        String url = "jdbc:tdhs://" + host + ":" + tdhsPort + "/jdbc_test";
        SimplePooledTDHSDataSource source = new SimplePooledTDHSDataSource(url);

        Field fieldUrl = SimplePooledTDHSDataSource.class.getDeclaredField("url");
        fieldUrl.setAccessible(true);
        Assert.assertEquals(url, fieldUrl.get(source));

        Field fieldCount = SimplePooledTDHSDataSource.class.getDeclaredField("count");
        fieldCount.setAccessible(true);

        Field fieldLastOne = SimplePooledTDHSDataSource.class.getDeclaredField("lastOne");
        fieldLastOne.setAccessible(true);

        Assert.assertEquals(0, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNull(fieldLastOne.get(source));

        Connection connection = source.getConnection();
        Assert.assertEquals(1, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNull(fieldLastOne.get(source));

        connection.close();
        Assert.assertEquals(0, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNotNull(fieldLastOne.get(source));

        connection = source.getConnection();
        Assert.assertEquals(1, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNull(fieldLastOne.get(source));

        connection.close();
        Assert.assertEquals(0, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNotNull(fieldLastOne.get(source));

        Connection connection1 = source.getConnection();

        Assert.assertEquals(1, ((AtomicLong) fieldCount.get(source)).get());
        Assert.assertNull(fieldLastOne.get(source));
        Assert.assertEquals(connection, connection1);

    }
}
