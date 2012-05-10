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

import com.taobao.tdhs.client.common.TDHSCommon.FindFlag;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.Get;
import com.taobao.tdhs.client.request.TableInfo;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;
import junit.framework.Assert;
import org.junit.Test;

public class DataTest extends TestBase {
    private final String db = "unit_test";
    private final String table = "unit_test_data";
    private final String[] fields = {"id", "name", "level"};

    @Test
    public void testTenThousand() throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, null, fields);
        Get get = new Get(tableInfo, new String[][]{{"-1"}},
                FindFlag.TDHS_GE, 0, 10000);
        TDHSResponse r = client.get(get);
        Assert.assertEquals(ClientStatus.OK, r.getStatus());
        Assert.assertEquals(10000, r.getFieldData().size());
    }

    @Test
    public void testHundredThousand() throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, null,
                new String[]{fields[0]});
        Get get = new Get(tableInfo, new String[][]{{"-1"}},
                FindFlag.TDHS_GE, 0, 100000);
        TDHSResponse r = client.get(get);
        Assert.assertEquals(ClientStatus.OK, r.getStatus());
        Assert.assertEquals(100000, r.getFieldData().size());
    }
}
