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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

/**
 * <p>
 * 测试流程：
 * </p>
 * <p>
 * 见各方法注释
 * </p>
 * 
 * @author yuanfeng.mc
 * 
 */
public class EasyPackageTest extends TestBase {
	private String id = "";
	private String name = "";
	private String level = "";
	private Random rand = new Random();

	@Before
	public void prepare() {
		try {
			id = rand.nextInt() + "";
			name = UUID.randomUUID().toString().substring(0, 12);
			level = rand.nextInt() + "";

			TDHSResponse response = client.insert().use(db).from(table)
					.value(fields[0], id).value(fields[1], name)
					.value(fields[2], level).insert();

			Assert.assertEquals(ClientStatus.OK, response.getStatus());
		} catch (TDHSException e) {
			Assert.fail("Insert exception:" + e.getMessage());
		}
	}

	@After
	public void clean() {
		try {
			TDHSResponse response = client.query().use(db).from(table).where()
					.equal(id).delete();

			Assert.assertEquals(ClientStatus.OK, response.getStatus());
		} catch (TDHSException e) {
			Assert.fail("Delete exception:" + e.getMessage());
		}
	}

	@Test
	public void testDuplicateInsert() throws TDHSException {
		TDHSResponse response = client.insert().use(db).from(table)
				.value(fields[0], id).value(fields[1], name)
				.value(fields[2], level).insert();

		Assert.assertEquals(ClientStatus.DB_ERROR, response.getStatus());
	}

	/**
	 * update unit_test set name='my_name_updated' where id=9999
	 */
	@Test
	public void test02Update() throws TDHSException {
		name = UUID.randomUUID().toString().substring(0, 12);
		TDHSResponse response = client.query().use(db).from(table).set()
				.field(fields[1]).set(name).where().equal(id).update();

		Assert.assertEquals("1", response.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
	}

	/**
	 * select id,name,level from unit_test where id=9999 limit 0,100
	 */
	@Test
	public void test03Get() throws TDHSException {
		List<String> key = new ArrayList<String>(1);
		key.add(name);
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().index(index).in(key).and()
				.field(fields[2]).not("0").limit(0, 100).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		// 1. response.getFieldData() return List<List<String>>
		// 2. List<String> 存放单字段的值
		// 3. 外层List 存放(2)的集合数据
		Assert.assertEquals(id, response.getFieldData().get(0).get(0));
		Assert.assertEquals(name, response.getFieldData().get(0).get(1));
	}

	/**
	 * update unit_test set level=level+1 where id=9999
	 */
	@Test
	public void test04Update() throws TDHSException {
		level = Integer.parseInt(level) + 1 + "";
		TDHSResponse response = client.query().use(db).from(table).set()
				.field(fields[2]).add(1).where().equal(id).update();

		Assert.assertEquals("1", response.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
	}

	/**
	 * select id,name,level from unit_test where id=9999 and level=100
	 */
	@Test
	public void test05Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().in(new String[] { id }).and()
				.field(fields[2]).equal(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(response.getFieldData().get(0).size(),
				response.getFieldNumber());
		Assert.assertEquals(level, response.getFieldData().get(0).get(2));
	}

	/**
	 * update unit_test set level=level-1 where id=9999
	 */
	@Test
	public void test06Update() throws TDHSException {
		level = Integer.parseInt(level) - 1 + "";
		TDHSResponse response = client.query().use(db).from(table).set()
				.field(fields[2]).sub(1).where().equal(id).update();

		Assert.assertEquals("1", response.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
	}

	/**
	 * select id,name,level from unit_test where id<=9999 and level<=99
	 */
	@Test
	public void test07Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().lessEqual(id).and().field(fields[2])
				.lessEqual(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(1, response.getFieldData().size());
		Assert.assertEquals(level, response.getFieldData().get(0).get(2));
	}

	/**
	 * select id,name,level from unit_test where id<9999 and level<99
	 */
	@Test
	public void test08Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().lessThan(id).and().field(fields[2])
				.lessThan(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(0, response.getFieldData().size());
	}

	/**
	 * select id,name,level from unit_test where id>=9999 and level>=99
	 */
	@Test
	public void test09Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().greaterEqual(id).and().field(fields[2])
				.greaterEqual(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(1, response.getFieldData().size());
	}

	/**
	 * select id,name,level from unit_test where id>9999 and level>99
	 */
	@Test
	public void test10Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().greaterThan(id).and().field(fields[2])
				.greaterThan(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(0, response.getFieldData().size());
	}

	/**
	 * select id,name,level from unit_test where id = 1 and level != 99
	 */
	@Test
	public void test11Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().equal(id).and().field(fields[2])
				.not("1").get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(1, response.getFieldData().size());

		response = client.query().use(db).from(table).select(fields).where()
				.equal(id).and().field(fields[2]).not(level).get();

		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(0, response.getFieldData().size());
	}

	/**
	 * <ol>
	 * <li>select id,name,level from unit_test where id >= 9999</li>
	 * <li>select id,name,level from unit_test where id > 9999</li>
	 * <li>select id,name,level from unit_test where id <= 9999</li>
	 * <li>select id,name,level from unit_test where id < 9999</li>
	 * <ol>
	 */
	@Test
	public void test12Get() throws TDHSException {
		TDHSResponse response = client.query().use(db).from(table)
				.select(fields).where().greaterEqual(id).get();
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(1, response.getFieldData().size());

		response = client.query().use(db).from(table).select(fields).where()
				.greaterThan(id).get();
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(0, response.getFieldData().size());

		response = client.query().use(db).from(table).select(fields).where()
				.lessEqual(id).get();
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(1, response.getFieldData().size());

		response = client.query().use(db).from(table).select(fields).where()
				.lessThan(id).get();
		Assert.assertEquals(ClientStatus.OK, response.getStatus());
		Assert.assertEquals(0, response.getFieldData().size());
	}

}
