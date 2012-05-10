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

import com.taobao.tdhs.client.request.ValueEntry;
import org.junit.Assert;
import org.junit.Test;

import com.taobao.tdhs.client.common.TDHSCommon.FindFlag;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

public class ClientUpdateExceptionTest extends TestBase {
	@Test
	public void testNullUpdateEntry() throws TDHSException {
		try {
			client.update("a", "b", "c", new String[] { "d" }, null,
					new String[][] { { "e" } }, FindFlag.TDHS_EQ, 0, 100, null);
		} catch (Exception e) {
			Assert.assertEquals("field's size not match updateEntries's size",
					e.getMessage());
		}

		try {
			client.update("a", "b", "c", new String[] { "d" },
					new ValueEntry[0], new String[][] { { "e" } },
					FindFlag.TDHS_EQ, 0, 100, null);
		} catch (Exception e) {
			Assert.assertEquals("field's size not match updateEntries's size",
					e.getMessage());
		}

		TDHSResponse r = client.update("a", "b", "c", new String[] { "d" },
				new ValueEntry[1], new String[][] { { "e" } },
				FindFlag.TDHS_EQ, 0, 100, null);
		Assert.assertEquals(ClientStatus.BAD_REQUEST, r.getStatus());
	}

	@Test
	public void testNullUpdate() throws TDHSException {
		try {
			client.update(null);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("update is null!", e.getMessage());
		}
	}
}
