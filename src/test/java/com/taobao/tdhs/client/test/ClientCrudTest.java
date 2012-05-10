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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import com.taobao.tdhs.client.request.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.tdhs.client.common.TDHSCommon.FilterFlag;
import com.taobao.tdhs.client.common.TDHSCommon.FindFlag;
import com.taobao.tdhs.client.common.TDHSCommon.UpdateFlag;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.ValueEntry;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum.ClientStatus;

public class ClientCrudTest extends TestBase {
	private String id = "";
	private String name = "";
	private String level = "";
	private Random rand = new Random();

	@Before
	public void prepare() throws TDHSException {
		// 生成随机值
		id = rand.nextInt() + "";
		name = UUID.randomUUID().toString().substring(0, 12);
		level = rand.nextInt() + "";

		TableInfo tableInfo = new TableInfo(db, table, index, fields);
		Insert insert = new Insert(tableInfo);
		insert.addValue(id);
		insert.addValue(name);
		insert.addValue(level);
		TDHSResponse r = client.insert(insert);

		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@After
	public void clean() throws TDHSException {
		TableInfo tableInfo = new TableInfo(db, table, index, fields);
		Get get = new Get(tableInfo, new String[][] { { name } },
				FindFlag.TDHS_EQ, 0, 20);
		TDHSResponse r = client.delete(get);

		Assert.assertEquals("1", r.getFieldData().get(0).get(0));
		Assert.assertEquals(ClientStatus.OK, r.getStatus());
	}

	@Test
	public void testUpdate1() throws TDHSException {
		try {
			ValueEntry entry = new ValueEntry(UpdateFlag.TDHS_UPDATE_SET, "5");
			TDHSResponse r = client.update(db, table, null,
					new String[] { fields[0] }, new ValueEntry[] { entry },
					new String[][] { { id } }, FindFlag.TDHS_EQ, 0, 100, null);

			Assert.assertEquals("1", r.getFieldData().get(0).get(0));
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			id = "5";
		} catch (TDHSException e) {
			throw new TDHSException("Update exception:" + e.getMessage());
		}
	}

	@Test
	public void testUpdate2() throws TDHSException {
		try {
			ValueEntry entry = new ValueEntry(UpdateFlag.TDHS_UPDATE_ADD, "5");
			TDHSResponse r = client.update(db, table, null,
					new String[] { fields[0] }, new ValueEntry[] { entry },
					new String[][] { { id } }, FindFlag.TDHS_EQ, 0, 100, null);

			Assert.assertEquals("1", r.getFieldData().get(0).get(0));
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			id = Integer.parseInt(id) + 5 + "";
		} catch (Exception e) {
			throw new TDHSException("Update exception:" + e.getMessage());
		}
	}

	@Test
	public void testUpdate3() throws TDHSException {
		try {
			ValueEntry entry = new ValueEntry(UpdateFlag.TDHS_UPDATE_SUB, "5");
			TDHSResponse r = client.update(db, table, null,
					new String[] { fields[0] }, new ValueEntry[] { entry },
					new String[][] { { id } }, FindFlag.TDHS_EQ, 0, 100, null);

			Assert.assertEquals("1", r.getFieldData().get(0).get(0));
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			id = Integer.parseInt(id) - 5 + "";
		} catch (Exception e) {
			throw new TDHSException("Update exception:" + e.getMessage());
		}
	}

	@Test
	public void testGet01() throws TDHSException, SQLException {
		try {
			TableInfo tableInfo = new TableInfo(db, table, index,
					new String[] { fields[0] });
			// keys与上面index对应
			Get get = new Get(tableInfo, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 20);

			TDHSResponse r = client.get(get);
			ResultSet rs = r.getResultSet();
			int c = 0;
			while (rs.next()) {
				c++;
			}
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(c, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get exception:" + e.getMessage());
		}
	}

	@Test
	public void testGet02() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });

			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}

		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet03() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet04() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet05() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet06() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet07() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet08() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet09() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet10() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet11() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet12() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet13() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet14() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet15() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet16() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LE, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet17() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet18() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet19() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet20() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet21() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet22() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet23() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet24() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_GT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet25() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet26() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet27() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet28() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet29() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet30() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_LT, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet31() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_EQ, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet32() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet33() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LE, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet34() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_GT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet35() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_LT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet36() throws TDHSException {
		try {
			Filter f = new Filter(fields[2], FilterFlag.TDHS_NOT, level);
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } },
					FindFlag.TDHS_IN, 0, 100, new Filter[] { f });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(0, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get1 exception", e);
		}
	}

	@Test
	public void testGet37() throws TDHSException {
		try {
			// 此get方法调用get(String db, String table, String index, String
			// fields[], String keys[][],TDHSCommon.FindFlag findFlag, int
			// start, int limit, Filter filters[])
			// 其中start设置为0, limit设置为1
			// 所以下面判断getFieldData().size()为1（从数据库中看应该为5，特此说明）
			TDHSResponse r = client.get(db, table, index, new String[] {
					fields[0], fields[2] }, new String[][] { { name } });
			Assert.assertEquals(ClientStatus.OK, r.getStatus());
			Assert.assertEquals(1, r.getFieldData().size());
		} catch (TDHSException e) {
			throw new TDHSException("Get2 exception:" + e.getMessage());
		}
	}
}
