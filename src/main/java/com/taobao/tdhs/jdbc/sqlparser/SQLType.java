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

package com.taobao.tdhs.jdbc.sqlparser;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-8 上午10:25
 */
public enum SQLType {
    INSERT(0), UPDATE(1), DELETE(2), SELECT(3);

    private int value;

    private SQLType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
