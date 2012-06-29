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

public class EasyInsertTest extends TestBase {
	/**
	 * 此处自动把非数字字符串转换0，具体需要在数据库中my.ini中配置
	 * sql-mode="NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
	 */
	@Test
	public void testIllegalValue() throws TDHSException {
		TDHSResponse r = client.insert().use(db).from(table)
				.value(fields[0], "asd").value(fields[1], "31")
				.value(fields[2], "mmm").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.delete(db, table, null, new String[][] { { "0" } },
				FindFlag.TDHS_EQ, 0, 100, null);
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	/**
	 * 此处自动把非数字字符串转换0，具体需要在数据库中my.ini中配置
	 * sql-mode="NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
	 */
	@Test
	public void testMissField() throws TDHSException {
		TDHSResponse r = client.insert().use(db).from(table)
				.value(fields[0], "1").value(fields[2], "10").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.delete(db, table, null, new String[][] { { "1" } },
				FindFlag.TDHS_EQ, 0, 100, null);
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testNullValue() throws TDHSException {
		TDHSResponse r = client.insert().use(db).from(table)
				.value(fields[0], (String) null).insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.delete(db, table, null, new String[][] { { "0" } },
				FindFlag.TDHS_EQ, 0, 100, null);
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.insert().use(db).from(table).value(fields[0], "").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.delete(db, table, null, new String[][] { { "0" } },
				FindFlag.TDHS_EQ, 0, 100, null);
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testNullField() throws TDHSException {
		TDHSResponse r = client.insert().use(db).from(table).value(null, "abc")
				.insert();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());

		client.insert().use(db).from(table).value("", "abc").insert();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}

	@Test
	public void testSpecialChar() throws TDHSException {
		TDHSResponse r = client
				.insert()
				.use(db)
				.from(table)
				.value("~!@#$%^&*()_+-={}[]<>?,./:;\\\"'",
						"~!@#$%^&*()_+-={}[]<>?,./:;\\\"'").insert();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}
}
