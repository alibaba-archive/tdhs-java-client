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
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.easy.Where;
import com.taobao.tdhs.client.request.Get;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:48
 */
public class WhereImpl implements Where {

    private final Get get;

    private final Query query;

    public WhereImpl(Get get, Query query) {
        this.get = get;
        this.query = query;
    }

    public Where fields(String... field) {
        StringBuilder sb = new StringBuilder("|");
        for (String f : field) {
            if (StringUtils.isNotBlank((f))) {
                sb.append(f);
                sb.append('|');
            }
        }
        get.getTableInfo().setIndex(sb.toString());
        return this;
    }

    public Where index(String index) {
        get.getTableInfo().setIndex(index);
        return this;
    }

    public Query equal(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_EQ);
        get.setKey(key);
        return query;
    }

    public Query descEqual(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_DEQ);
        get.setKey(key);
        return query;
    }

    public Query greaterEqual(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_GE);
        get.setKey(key);
        return query;
    }

    public Query lessEqual(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_LE);
        get.setKey(key);
        return query;
    }

    public Query greaterThan(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_GT);
        get.setKey(key);
        return query;
    }

    public Query lessThan(String... key) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_LT);
        get.setKey(key);
        return query;
    }

    public Query in(String[]... keys) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_IN);
        get.setKey(keys);
        return query;
    }

    public Query in(List<String>... keys) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_IN);
        get.setKey(keys);
        return query;
    }

    public Query between(String[]... keys) {
        get.setFindFlag(TDHSCommon.FindFlag.TDHS_BETWEEN);
        get.setKey(keys);
        return query;
    }
}
