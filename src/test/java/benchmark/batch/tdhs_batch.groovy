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

package benchmark.batch

import benchmark.PropUtil
import benchmark.StressTest
import com.taobao.tdhs.client.TDHSClient
import com.taobao.tdhs.client.TDHSClientImpl
import com.taobao.tdhs.client.statement.BatchStatement
import java.util.concurrent.atomic.AtomicLong

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-22 下午2:04
 *
 */


AtomicLong v = new AtomicLong(1)

TDHSClient client = new TDHSClientImpl(new InetSocketAddress(PropUtil.host, 9999), 2);

def s = new StressTest()
s.add({
  BatchStatement b = client.createBatchStatement()
  b.insert().use("test").from("test")
          .value("data", v.getAndIncrement().toString() + "_aaa")
          .insert()
  b.insert().use("test").from("test")
          .value("data", v.getAndIncrement().toString() + "_bbb")
          .insert()
  b.insert().use("test").from("test")
          .value("data", v.getAndIncrement().toString() + "_ccc")
          .insert()

  b.commit()
}, 100)

s.run()
client.shutdown()
