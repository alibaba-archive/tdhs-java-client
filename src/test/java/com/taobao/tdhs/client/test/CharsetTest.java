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
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;
import junit.framework.Assert;
import org.junit.Test;

/**
 * 
 * @author yuanfeng.mc
 * 
 */
public class CharsetTest extends TestBase{
	private final String db = "unit_test";
	private final String table = "unit_test_utf8";
	private final String[] fields = { "id", "name", "level" };

	@Test
	public void testDifferentCharset() throws TDHSException {
		try {
			client.setCharsetName("gbk");

			TDHSResponse r = client.insert(db, table, fields, new String[] {
					"999", "中文", "999" });

			Assert.assertEquals(ClientStatus.OK, r.getStatus());

			r = client.get(db, table, null, fields,
					new String[][] { { "999" } });

			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals("", r.getFieldData().get(0).get(1));

			client.setCharsetName("utf8");

		} finally {
			TDHSResponse r = client.delete(db, table, null,
					new String[][] { { "999" } }, FindFlag.TDHS_EQ, 0, 1, null);
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals("1", r.getFieldData().get(0).get(0));
		}
	}
}
