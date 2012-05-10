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
import com.taobao.tdhs.client.request.Filter;

public class ClientDeleteExceptionTest extends TestBase {
	@Test
	public void testNullProperty() throws TDHSException {
		try {
			client.delete(db, table, null, null, FindFlag.TDHS_EQ, -1, -1, null);
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}

		try {
			client.delete(db, table, null, new String[0][], FindFlag.TDHS_EQ,
					-1, -1, null);
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}

		try {
			client.delete(db, table, null, new String[1][0], FindFlag.TDHS_EQ,
					-1, -1, null);
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.delete(db, table, null, new String[1][0], FindFlag.TDHS_EQ,
					-1, -1, new Filter[0]);
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}
	}

	@Test
	public void testNull() throws TDHSException {
		try {
			client.delete(null);
		} catch (Exception e) {
			Assert.assertEquals("get is null!", e.getMessage());
		}
	}
}
