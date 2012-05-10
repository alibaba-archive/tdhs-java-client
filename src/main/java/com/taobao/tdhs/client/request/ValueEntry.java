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

package com.taobao.tdhs.client.request;

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.exception.TDHSEncodeException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-14 下午4:30
 */
public class ValueEntry implements Request {
    private int ____flag;

    private String _value;

    public ValueEntry(TDHSCommon.UpdateFlag flag, String _value) {
        this.____flag = flag.getValue();
        this._value = _value;
    }

    public void isVaild() throws TDHSEncodeException {
    }

    @Override public String toString() {
        return "ValueEntry{" +
                "flag=" + ____flag +
                ", value='" + _value + '\'' +
                '}';
    }
}
