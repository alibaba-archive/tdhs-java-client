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

import benchmark.RandomUtil
import benchmark.StressTest
import benchmark.TableUtil
import com.taobao.tdhs.client.TDHSClient
import com.taobao.tdhs.client.TDHSClientImpl
import com.taobao.tdhs.client.response.TDHSResponse
import benchmark.PropUtil

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-9 下午3:31
 *
 */
def getId() {
  if (RandomUtil.nextInt(100) < 95) {
    Integer i = RandomUtil.nextInt(1024) + 1;
    return i
  } else {
    Integer i = RandomUtil.nextInt(80000000) + 1;
    return i
  }
}

TDHSClient client = new TDHSClientImpl(new InetSocketAddress(PropUtil.host, 9999), 2);

def s = new StressTest()
s.add({
  def _id = getId()
  String table = TableUtil.getTable("test_", _id.longValue() % 256);
  try {
    TDHSResponse response = client.query().use("benchmark").from(table)
            .select("id", "k", "i", "c")
            .where().index("idx_i").equal(_id.toString()).get()
  } catch (Exception e) {
  }

//  if (response != null && response.getStatus() != TDHSResponseEnum.ClientStatus.OK) {
//    System.out.println(response);
//  }
}, 100)

s.run()
client.shutdown()
