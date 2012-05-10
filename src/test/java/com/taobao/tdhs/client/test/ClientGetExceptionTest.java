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

import com.taobao.tdhs.client.common.TDHSCommon.FilterFlag;
import com.taobao.tdhs.client.common.TDHSCommon.FindFlag;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.Filter;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

public class ClientGetExceptionTest extends TestBase {
	@Test
	public void testClientNullDB() {
		try {
			client.get(null, null, null, null, null);
		} catch (TDHSException e) {
			Assert.assertEquals("db can't be empty!", e.getMessage());
		}
		try {
			client.get("", null, null, null, null);
		} catch (TDHSException e) {
			Assert.assertEquals("db can't be empty!", e.getMessage());
		}
	}

	@Test
	public void testClientNullTable() {
		try {
			client.get(db, null, null, null, null);
		} catch (TDHSException e) {
			Assert.assertEquals("table can't be empty!", e.getMessage());
		}
		try {
			client.get(db, "", null, null, null);
		} catch (TDHSException e) {
			Assert.assertEquals("table can't be empty!", e.getMessage());
		}
	}

	/**
	 * INDEX允许为空，为空时，使用主键索引
	 */
	@Test
	public void testClientNullIndexAndFields() {
		try {
			client.get(db, table, null, null, null);
		} catch (TDHSException e) {
			Assert.assertEquals("field can't be empty!", e.getMessage());
		}
		try {
			client.get(db, table, "", new String[0], null);
		} catch (TDHSException e) {
			Assert.assertEquals("field can't be empty!", e.getMessage());
		}
	}

	@Test
	public void testClientIllegalStart() {
		try {
			TDHSResponse r = client.get(db, table, index,
					new String[] { "name" }, new String[][] { { "aaa" } },
					FindFlag.TDHS_EQ, -1, -1, null);
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testClientNullKeys() {
		String[] feilds = new String[] { "", "", "" };
		try {
			client.get(db, table, index, feilds, null);
		} catch (TDHSException e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}
		String[][] nullKeys = new String[0][];
		try {
			client.get(db, table, index, feilds, nullKeys);
		} catch (TDHSException e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}
		nullKeys = new String[1][0];
		nullKeys[0] = new String[0];
		try {
			client.get(db, table, index, feilds, nullKeys);
		} catch (TDHSException e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}
		nullKeys[0] = null;
		try {
			client.get(db, table, index, feilds, nullKeys);
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		nullKeys[0] = new String[] { "" };
		try {
			client.get(db, table, index, feilds, nullKeys);
		} catch (TDHSException e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}
	}

	@Test(expected = Exception.class)
	public void testClientNullGet() throws Exception {
		client.get(null);
	}

	@Test
	public void testClientFullGetZeroFilter() throws Exception {
		TDHSResponse r = client.get(db, table, index, new String[] { "name" },
				new String[][] { { "aaa" } }, FindFlag.TDHS_EQ, -1, -1,
				new Filter[0]);
		/* 2012-01-16 ClientStatus.NOT_FOUND -> ClientStatus.OK */
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testClientFullGetNullFilter() throws Exception {
		Filter f = new Filter("aaaa", FilterFlag.TDHS_EQ, "cccc");
		TDHSResponse r = client.get(db, table, index, new String[] { "name" },
				new String[][] { { "aaa" } }, FindFlag.TDHS_EQ, 0, 100,
				new Filter[] { f });
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}

	@Test
	public void testClientSpecialChar() throws Exception {
		Filter f = new Filter("aaaa", FilterFlag.TDHS_EQ,
				"~!@#$%^&*()_+-={}[]:\\\";'<>?,./");
		TDHSResponse r = client.get("~!@#$%^&*()_+-={}[]:\\\";'<>?,./",
				"~!@#$%^&*()_+-={}[]:\\\";'<>?,./",
				"~!@#$%^&*()_+-={}[]:\\\";'<>?,./",
				new String[] { "~!@#$%^&*()_+-={}[]:\\\";'<>?,./" },
				new String[][] { { "~!@#$%^&*()_+-={}[]:\\\";'<>?,./" } },
				FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}
}
