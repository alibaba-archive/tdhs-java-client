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
import com.taobao.tdhs.client.easy.Set;
import com.taobao.tdhs.client.request.Get;
import com.taobao.tdhs.client.request.Update;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午4:12
 */
public class SetImpl implements Set {

    private String field = null;

    private final Get get;

    private final Update update;

    private final Query query;

    public SetImpl(Get get, Update update, Query query) {
        this.get = get;
        this.query = query;
        this.update = update;
    }

    public Set field(String field) {
        if (this.field != null) {
            throw new IllegalArgumentException("can't field twice!");
        }
        this.field = field;
        return this;
    }

    private Query setIt(String value, TDHSCommon.UpdateFlag updateFlag) {
        if (this.field == null) {
            throw new IllegalArgumentException("no field!");
        }
        this.get.getTableInfo().getFields().add(this.field);
        this.field = null;
        this.update.addEntry(updateFlag, value);
        return query;
    }

    public Query add(long value) {
        return setIt(String.valueOf(value), TDHSCommon.UpdateFlag.TDHS_UPDATE_ADD);
    }

    public Query sub(long value) {
        return setIt(String.valueOf(value), TDHSCommon.UpdateFlag.TDHS_UPDATE_SUB);
    }

    public Query set(@Nullable String value) {
        return setIt(value, TDHSCommon.UpdateFlag.TDHS_UPDATE_SET);
    }

    public Query set(long value) {
        return set(String.valueOf(value));
    }

    public Query set(int value) {
        return set(String.valueOf(value));
    }

    public Query set(short value) {
        return set(String.valueOf(value));
    }

    public Query set(char value) {
        return set(String.valueOf(value));
    }

    public Query setNow() {
        return setIt("", TDHSCommon.UpdateFlag.TDHS_UPDATE_NOW);
    }

    public Query setNull() {
        return set(null);
    }
}
