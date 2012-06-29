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
 * @since 11-12-14 下午4:28
 */
public class Update extends RequestWithCharest implements Request {

    private Get get;

    private List<ValueEntry> _valueEntries = new ArrayList<ValueEntry>(1);

    public Update(Get get) {
        this.get = get;
    }

    public Update(Get get, ValueEntry valueEntry[]) {
        this(get);
        if (valueEntry != null && valueEntry.length > 0) {
            for (ValueEntry u : valueEntry) {
                this.addEntry(u);
            }
        }
    }


    public void addEntry(TDHSCommon.UpdateFlag flag, String value) {
        _valueEntries.add(new ValueEntry(flag, value));
    }

    public void addEntry(TDHSCommon.UpdateFlag flag, byte[] value) {
        _valueEntries.add(new ValueEntry(flag, value));
    }

    public void addEntry(ValueEntry entry) {
        _valueEntries.add(entry);
    }

    public Get getGet() {
        return get;
    }

    public void isVaild() throws TDHSEncodeException {
        if (get == null) {
            throw new TDHSEncodeException("get can't be empty!");
        }
        get.isVaild();
        if (_valueEntries.size() != get.getTableInfo().getFields().size()) {
            throw new TDHSEncodeException("field's size not match updateEntries's size");
        }
        if (_valueEntries.size() > TDHSCommon.REQUEST_MAX_FIELD_NUM) {
            throw new TDHSEncodeException("too many updateEntries , larger than 256!");
        }
    }

    @Override
    public String toString() {
        return "Update{" +
                "get=" + get +
                ", valueEntries=" + _valueEntries +
                '}';
    }
}
