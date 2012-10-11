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
 * @since 11-12-19 下午5:45
 */
public class Insert extends RequestWithCharset implements Request {
    private TableInfo tableInfo;

    private List<ValueEntry> _values = new ArrayList<ValueEntry>();

    public Insert(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public Insert(TableInfo tableInfo, ValueEntry values[]) {
        this(tableInfo);
        if (values != null && values.length > 0) {
            for (ValueEntry u : values) {
                this.addValue(u);
            }
        }
    }

    public Insert(TableInfo tableInfo, String values[]) {
        this(tableInfo);
        if (values != null && values.length > 0) {
            for (String u : values) {
                this.addValue(u);
            }
        }
    }

    public void addValue(String entry) {
        addValue(TDHSCommon.UpdateFlag.TDHS_UPDATE_SET, entry);
    }

    public void addValue(byte[] entry) {
        addValue(TDHSCommon.UpdateFlag.TDHS_UPDATE_SET, entry);
    }

    public void addValue(TDHSCommon.UpdateFlag flag, String value) {
        _values.add(new ValueEntry(flag, value));
    }

    public void addValue(TDHSCommon.UpdateFlag flag, byte[] value) {
        _values.add(new ValueEntry(flag, value));
    }

    public void addValue(ValueEntry entry) {
        _values.add(entry);
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void isValid(TDHSCommon.ProtocolVersion version) throws TDHSEncodeException {
        if (tableInfo == null) {
            throw new TDHSEncodeException("tableInfo can't be empty!");
        }
        tableInfo.isValid(version);
        if (_values.size() != tableInfo.getFields().size()) {
            throw new TDHSEncodeException("field's size not match values's size");
        }
        if (_values.size() > TDHSCommon.REQUEST_MAX_FIELD_NUM) {
            throw new TDHSEncodeException("too many insert values , larger than 256!");
        }
    }

    @Override
    public String toString() {
        return "Insert{" +
                "tableInfo=" + tableInfo +
                ", values=" + _values +
                '}';
    }
}
