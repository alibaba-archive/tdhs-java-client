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

package com.taobao.tdhs.client.util;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-1-12 下午12:31
 */
final public class ByteOrderUtil {

    public static void writeIntToNet(byte[] bytes, int i, long value) {
        if (i > (bytes.length - 4)) {
            throw new IllegalArgumentException("out of range");
        }
        for (int j = 3; j >= 0; i++, j--) {
            bytes[i] = (byte) ((value >>> (8 * j)) & 0XFF);
        }
    }


    public static long getUnsignInt(final byte data[], final int pos) {
        int v = (data[pos] & 0xff) << 24 |
                (data[pos + 1] & 0xff) << 16 |
                (data[pos + 2] & 0xff) << 8 |
                (data[pos + 3] & 0xff) << 0;
        return v & 0xFFFFFFFFL;
    }
}
