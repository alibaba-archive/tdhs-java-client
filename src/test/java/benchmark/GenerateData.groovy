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

package benchmark

import groovy.sql.Sql
import java.util.concurrent.atomic.AtomicLong

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-9 下午2:27
 *
 */

def client_num = 50

AtomicLong id = new AtomicLong(0)


private List<Sql> connent(int num) {
  def result = []
  1.upto(num) {result.add(Sql.newInstance("jdbc:mysql://" + PropUtil.host + ":3309/benchmark", "test", "test", "com.mysql.jdbc.Driver"))}
  return result
}

def clients = connent(client_num)

def s = new StressTest(count: 80000000)
s.add({
  Sql client = clients.get(it)
  long _id = id.incrementAndGet()
  String table = TableUtil.getTable("test_", _id % 256);
  client.execute("INSERT INTO `" + table + "` (`k`, `i`, `c`) VALUES ('10000', '" + _id.toString() + "', '" + _id.toString() + "_abcdefghijklmnopqrstuvwxyz" + "')")
}, client_num)

s.run()


clients.each {it.close()}

