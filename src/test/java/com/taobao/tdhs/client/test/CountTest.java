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

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.Get;
import com.taobao.tdhs.client.request.TableInfo;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-28 下午5:41
 */
public class CountTest extends TestBase {

    private final String db = "unit_test";
    private final String table = "unit_test_data";
    private final String[] fields = {"id", "name", "level"};

    @Test
    public void testTenThousand() throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, null, fields);
        Get get = new Get(tableInfo, new String[][]{{"-1"}},
                TDHSCommon.FindFlag.TDHS_GE, 0, 10000);
        TDHSResponse r = client.count(get);
        Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        Assert.assertEquals(1, r.getFieldData().size());
        Assert.assertEquals("10000", r.getFieldData().get(0).get(0));
    }

    @Test
    public void testHundredThousand() throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, null,
                new String[]{fields[0]});
        Get get = new Get(tableInfo, new String[][]{{"-1"}},
                TDHSCommon.FindFlag.TDHS_GE, 0, 100000);
        TDHSResponse r = client.count(get);
        Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        Assert.assertEquals(1, r.getFieldData().size());
        Assert.assertEquals("100000", r.getFieldData().get(0).get(0));
    }

    @Test
    public void testCustom() throws TDHSException {
        TDHSResponse r = client.query().use(db).from(table).where().lessEqual("9898").count();
        Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        Assert.assertEquals(1, r.getFieldData().size());
        Assert.assertEquals("9898", r.getFieldData().get(0).get(0));
    }

    @Test
    public void testAllCustom() throws TDHSException {
        TDHSResponse r = client.query().use(db).from(table).where().lessEqual("1989800").count();
        Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        Assert.assertEquals(1, r.getFieldData().size());
        Assert.assertEquals("100000", r.getFieldData().get(0).get(0));
    }

    @Test
    public void testCustom2() throws TDHSException {
        TDHSResponse r = client.query().use(db).from(table).where().lessEqual("1989800").and().field("id").greaterThan(
                "1000").count();
        Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        Assert.assertEquals(1, r.getFieldData().size());
        Assert.assertEquals("99000", r.getFieldData().get(0).get(0));
    }
}
