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

package benchmark.insert

import benchmark.StressTest
import benchmark.TableUtil
import com.google.code.hs4j.HSClient
import com.google.code.hs4j.IndexSession
import com.google.code.hs4j.impl.HSClientImpl
import java.util.concurrent.atomic.AtomicLong
import benchmark.PropUtil

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-9 下午3:32
 *
 */


AtomicLong id = new AtomicLong(0)
HSClient hsClient = new HSClientImpl(new InetSocketAddress(PropUtil.host, 8889), 3);
List<IndexSession> sesson_pool = []

0.upto(256 - 1) {
  String table = TableUtil.getTable("test_", it);
  sesson_pool.add(hsClient.openIndexSession("benchmark_insert", table,
          "PRIMARY", ["k", "i", "c"] as String[]))
}

def s = new StressTest()
s.add({
  def _id = id.incrementAndGet()

  IndexSession session = sesson_pool.get((_id.longValue() % 256).intValue())
  session.insert("10000", _id.toString(), _id.toString() + "_abcdefghijklmnopqrstuvwxyz")
}, 100)


s.run()
hsClient.shutdown()
