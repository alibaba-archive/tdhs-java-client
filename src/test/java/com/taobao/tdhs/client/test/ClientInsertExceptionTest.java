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

import org.junit.Assert;
import org.junit.Test;

import com.taobao.tdhs.client.common.TDHSCommon.FindFlag;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

public class ClientInsertExceptionTest extends TestBase {
	@Test
	public void testNullProperty() throws TDHSException {
		try {
			client.insert(db, table, null, null);
		} catch (Exception e) {
			Assert.assertEquals("field can't be empty!", e.getMessage());
		}

		try {
			client.insert(db, table, new String[0], null);
		} catch (Exception e) {
			Assert.assertEquals("field can't be empty!", e.getMessage());
		}

		try {
			TDHSResponse r = client.insert(db, table,
					new String[] { fields[2] }, new String[] { "0" });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
		} catch (Exception e) {
			Assert.assertEquals("field's size not match values's size",
					e.getMessage());
		} finally {
			client.delete(db, table, null, new String[][] { { "0" } },
					FindFlag.TDHS_EQ, 0, 1, null);
		}
	}

	@Test
	public void testNull() throws TDHSException {
		try {
			client.insert(null);
		} catch (Exception e) {
			Assert.assertEquals("insert is null!", e.getMessage());
		}
	}
}
