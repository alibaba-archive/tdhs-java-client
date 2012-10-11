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

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-13 下午1:01
 */
public class Filters implements Request {

    private List<Filter> _filter = new ArrayList<Filter>(1);

    public void addFilter(String field, TDHSCommon.FilterFlag flag, String value) {
        _filter.add(new Filter(field, flag, value));
    }

    public void addFilter(Filter filter) {
        _filter.add(filter);
    }


    public void isValid(TDHSCommon.ProtocolVersion version) throws TDHSEncodeException {
        if (_filter != null && _filter.size() > TDHSCommon.REQUEST_MAX_FIELD_NUM) {
            throw new TDHSEncodeException("too many filter , larger than 256!");
        }
    }

    @Override public String toString() {
        return "Filters{" +
                "filter=" + _filter +
                '}';
    }
}
