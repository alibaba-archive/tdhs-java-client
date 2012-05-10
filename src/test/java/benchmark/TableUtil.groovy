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

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-9 下午1:12
 *
 */
final class TableUtil {

  public static String getTable(String prefix, long index) {
    return prefix + index.toString().padLeft(4, "0");
  }

  public static void main(String[] args) {
    println getTable("test_", 123);
  }

}
