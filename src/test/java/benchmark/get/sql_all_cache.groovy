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

package benchmark.get

import benchmark.PropUtil
import benchmark.RandomUtil
import benchmark.StressTest
import benchmark.TableUtil
import groovy.sql.Sql

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-9 下午2:14
 *
 */
def client_num = 70

private List<Sql> connent(int num) {
  def result = []
  1.upto(num) {result.add(Sql.newInstance("jdbc:mysql://" + PropUtil.host + ":3309/benchmark", "test", "test", "com.mysql.jdbc.Driver"))}
  return result
}

def clients = connent(client_num)

def s = new StressTest()
s.add({
  Sql client = clients.get(it)
  def _id = RandomUtil.getId(1024, 1) + 1
  String table = TableUtil.getTable("test_", _id.longValue() % 256);
  client.execute("select id ,k,i,c from " + table + " where i =" + _id.toString())
}, client_num)

s.run()


clients.each {it.close()}
