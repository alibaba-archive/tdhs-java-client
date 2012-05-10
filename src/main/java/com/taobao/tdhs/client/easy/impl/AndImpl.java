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

package com.taobao.tdhs.client.easy.impl;

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.easy.And;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.request.Get;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:55
 */
public class AndImpl implements And {

    private String field = null;

    private final Get get;

    private final Query query;

    public AndImpl(Get get, Query query) {
        this.query = query;
        this.get = get;
    }

    public And field(String field) {
        if (this.field != null) {
            throw new IllegalArgumentException("can't field twice!");
        }
        this.field = field;
        return this;
    }

    private Query filter(@Nullable String value, TDHSCommon.FilterFlag filterFlag) {
        if (this.field == null) {
            throw new IllegalArgumentException("no field!");
        }
        this.get.addFilter(field, filterFlag, value);
        this.field = null;
        return query;
    }

    public Query equal(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_EQ);
    }


    public Query greaterEqual(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_GE);
    }

    public Query lessEqual(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_LE);
    }

    public Query greaterThan(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_GT);
    }

    public Query lessThan(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_LT);
    }

    public Query not(String value) {
        return filter(value, TDHSCommon.FilterFlag.TDHS_NOT);
    }

    public Query isNull() {
        return filter(null, TDHSCommon.FilterFlag.TDHS_EQ);
    }
}
