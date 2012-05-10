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
 * @since 11-12-13 下午1:25
 */

public class Filter implements Request {
    private String _field;

    private int _____flag;

    private String _value;


    public Filter(String _field, TDHSCommon.FilterFlag flag, String value) {
        this._field = _field;
        this._____flag = flag.getValue();
        this._value = value;
    }

    public void isVaild() throws TDHSEncodeException {
        //nothing
    }

    @Override public String toString() {
        return "Filter{" +
                "field='" + _field + '\'' +
                ", flag=" + _____flag +
                ", value='" + _value + '\'' +
                '}';
    }
}