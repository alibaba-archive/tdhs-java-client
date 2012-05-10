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

public class EasySelectTest extends TestBase {
	@Test
	public void testNullDB() {
		try {
			client.query().from(table).select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("db can't be empty!", e.getMessage());
		}

		try {
			client.query().use("").from(table).select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("db can't be empty!", e.getMessage());
		}

		try {
			client.query().use(null).from(table).select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("db can't be empty!", e.getMessage());
		}
	}

	@Test
	public void testNullFrom() {
		try {
			client.query().use(db).select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("table can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from("").select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("table can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(null).select(fields).get();
		} catch (Exception e) {
			Assert.assertEquals("table can't be empty!", e.getMessage());
		}
	}

	@Test
	public void testNullFields() {
		try {
			client.query().use(db).from(table).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select("").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select().get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select("", "", "").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be missing!", e.getMessage());
		}
	}

	@Test
	public void testNullIndex() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).select(fields)
				.where().index("").equal("a").and().field("level")
				.greaterEqual("9").get();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());

		// 为null时使用主键索引
		r = client.query().use(db).from(table).select(fields).where()
				.index(null).equal("a").and().field("level").greaterEqual("9")
				.get();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());

		r = client.query().use(db).from(table).select(fields).where()
				.index("NoThisIndex").equal("a").and().field("level")
				.equal("9").get();
		Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());

		r = client.query().use(db).from(table).select(fields).where()
				.index(index).equal("a").and().field("level").equal("9")
				.limit(-1, -1).get();
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testNullEqual() {
		try {
			client.query().use(db).from(table).select(fields).where().equal("")
					.get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.equal(new String[0]).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where().equal()
					.get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.equal(new String[20]).get();
		} catch (Exception e) {
			Assert.assertEquals("too many keys ,larger than 10!",
					e.getMessage());
		}
	}

	@Test
	public void testNullGreaterEqual() {
		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterEqual("").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterEqual(new String[0]).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterEqual().get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterEqual(new String[20]).get();
		} catch (Exception e) {
			Assert.assertEquals("too many keys ,larger than 10!",
					e.getMessage());
		}
	}

	@Test
	public void testNullGreaterThan() {
		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterThan("").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterThan(new String[0]).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterThan().get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.greaterThan(new String[20]).get();
		} catch (Exception e) {
			Assert.assertEquals("too many keys ,larger than 10!",
					e.getMessage());
		}
	}

	@Test
	public void testNullLessThan() {
		try {
			client.query().use(db).from(table).select(fields).where()
					.lessThan("").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessThan(new String[0]).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessThan().get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessThan(new String[20]).get();
		} catch (Exception e) {
			Assert.assertEquals("too many keys ,larger than 10!",
					e.getMessage());
		}
	}

	@Test
	public void testNullLessEqual() {
		try {
			client.query().use(db).from(table).select(fields).where()
					.lessEqual("").get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessEqual(new String[0]).get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessEqual().get();
		} catch (Exception e) {
			Assert.assertEquals("key can't be empty!", e.getMessage());
		}

		try {
			client.query().use(db).from(table).select(fields).where()
					.lessEqual(new String[20]).get();
		} catch (Exception e) {
			Assert.assertEquals("too many keys ,larger than 10!",
					e.getMessage());
		}
	}

	@Test
	public void testNullNot() throws TDHSException {
		TDHSResponse r = client.query().use(db).from(table).where().equal("a")
				.and().field("name").not("").get();
		Assert.assertEquals(ClientStatus.BAD_REQUEST, r.getStatus());

		r = client.query().use(db).from(table).where().equal("a").and()
				.field("name").not(null).get();
		Assert.assertEquals(ClientStatus.BAD_REQUEST, r.getStatus());
	}

	@Test
	public void testSetException() {
		try {
			client.query().use("unit_test").from("unit_test").set().field(null)
					.set("aaaa").update();
		} catch (Exception e) {
			Assert.assertEquals("no field!", e.getMessage());
		}

		try {
			client.query().use("unit_test").from("unit_test").set().field("name")
					.field("").set("aaaa").update();
		} catch (Exception e) {
			Assert.assertEquals("can't field twice!", e.getMessage());
		}
	}

	@Test
	public void testAndException() {
		try {
			client.query().use("unit_test").from("unit_test").where().equal("1")
					.and().field(null).equal("aaaa").get();
		} catch (Exception e) {
			Assert.assertEquals("no field!", e.getMessage());
		}

		try {
			client.query().use("unit_test").from("unit_test").where().equal("1")
					.and().field("name").field("name").equal("aaaa").get();
		} catch (Exception e) {
			Assert.assertEquals("can't field twice!", e.getMessage());
		}
	}

	@Test
	public void testSepcialChar() {
		try {
			TDHSResponse r = client.query()
					.use("~!@#$%^&*(\\\")_+-=[]{}<>?,./")
					.from("~!@#$%^&*(\\\")_+-=[]{}<>?,./")
					.select("~!@#$%^&*(\\\")_+-=[]{}<>?,./").where()
					.equal("~!@#$%^&*(\\\")_+-=[]{}<>?,./").and()
					.field("~!@#$%^&*(\\\")_+-=[]{}<>?,./")
					.greaterEqual("~!@#$%^&*(\\\")_+-=[]{}<>?,./").get();
			Assert.assertEquals(ClientStatus.NOT_FOUND, r.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
