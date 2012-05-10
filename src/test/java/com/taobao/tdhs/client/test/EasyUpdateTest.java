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

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

public class EasyUpdateTest extends TestBase {
	@Test
	public void testNoRecords() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).set().field("name")
				.set("ccc").where().equal("1").update();
		Assert.assertEquals("0", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.query().use(db).from(table).set().field("name").add(1213)
				.where().equal("1").update();
		Assert.assertEquals("0", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.query().use(db).from(table).set().field("name").sub(1213)
				.where().equal("1").update();
		Assert.assertEquals("0", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testNoFields() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).set()
				.field("NoThisFields").set("ccc").where().equal("1").update();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}
}
