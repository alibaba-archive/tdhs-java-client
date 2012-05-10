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

public class EasyDeleteTest extends TestBase {
	private void prepare() throws TDHSException {
		TDHSResponse r = client.insert().use(db).from(table)
				.value(fields[0], "100").value(fields[1], "name_100")
				.value(fields[2], "1000").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		client.insert().use(db).from(table).value(fields[0], "200")
				.value(fields[1], "name_200").value(fields[2], "2000").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		client.insert().use(db).from(table).value(fields[0], "300")
				.value(fields[1], "name_300").value(fields[2], "3000").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		client.insert().use(db).from(table).value(fields[0], "400")
				.value(fields[1], "name_400").value(fields[2], "4000").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		client.insert().use(db).from(table).value(fields[0], "500")
				.value(fields[1], "name_500").value(fields[2], "5000").insert();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testNoRecords() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).where()
				.in(new String[] { "1", "2", "3" }).delete();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}

	@Test
	public void testPartRecords() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).where()
				.in(new String[] { "100", "200", "3" }).delete();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
	}

	@Test
	public void testAllRecords() throws TDHSException {
		prepare();

		TDHSResponse r = client.query().use(db).from(table).select(fields)
				.where().in(new String[] { "100" }).delete();
		Assert.assertEquals("1", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.query().use(db).from(table).select(fields).where()
				.greaterThan("100").delete();
		Assert.assertEquals("4", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}
}
