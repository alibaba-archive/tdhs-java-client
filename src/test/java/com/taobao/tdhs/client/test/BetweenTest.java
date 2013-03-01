package com.taobao.tdhs.client.test;

import com.taobao.tdhs.client.TDHSClient;
import com.taobao.tdhs.client.TDHSClientImpl;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 13-3-1 上午10:35
 */
public class BetweenTest {

    @Test
    public void testBetween() throws TDHSException {
        TDHSClient client = new TDHSClientImpl(new InetSocketAddress("t-wentong-u.local", 9999), 1);

        TDHSResponse r = client.query().use("test").select("id", "v", "i").from("test").where().fields("i")
                .between(new String[]{"2"},
                        new String[]{"5"}).and().field("i").not("2").and().field("i").not("5").get();

        List<List<String>> fieldData = r.getFieldData();
        for (List<String> fd : fieldData) {
            for (String v : fd) {
                System.out.print(v + " ");
            }
            System.out.print("\n");
        }
        client.shutdown();
    }
}
