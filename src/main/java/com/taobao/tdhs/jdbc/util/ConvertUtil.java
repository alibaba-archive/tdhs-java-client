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

package com.taobao.tdhs.jdbc.util;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-7 下午12:15
 */
public final class ConvertUtil {

    public static int safeConvertInt(String v, int defaults) {
        try {
            return Integer.valueOf(v);
        } catch (NumberFormatException e) {
            return defaults;
        }
    }
}
