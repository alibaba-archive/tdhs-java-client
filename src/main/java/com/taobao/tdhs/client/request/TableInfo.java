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
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-13 上午11:36
 */
public class TableInfo implements Request {

    private String _db;
    private String _table;
    private String _index;
    private List<String> _fields = new ArrayList<String>(10);

    private boolean needField;

    public TableInfo() {
    }

    public TableInfo(String db, String table, String index, String[] fields) {
        this._db = db == null ? null : db.toLowerCase();
        this._table = table == null ? null : table.toLowerCase();
        this._index = index;
        if (fields != null) {
            Collections.addAll(this._fields, fields);
        }
        this.needField = true;
    }

    public String getDb() {
        return _db;
    }

    public void setDb(String db) {
        this._db = db == null ? null : db.toLowerCase();
    }

    public String getTable() {
        return _table;
    }

    public void setTable(String _table) {
        this._table = _table == null ? null : _table.toLowerCase();
    }

    public String getIndex() {
        return _index;
    }

    public void setIndex(String _index) {
        this._index = _index;
    }

    public List<String> getFields() {
        return _fields;
    }

    public void setNeedField(boolean needField) {
        this.needField = needField;
    }


    public void isVaild() throws TDHSEncodeException {
        if (StringUtils.isBlank(_db)) {
            throw new TDHSEncodeException("db can't be empty!");
        }
        if (StringUtils.isBlank(_table)) {
            throw new TDHSEncodeException("table can't be empty!");
        }
        if (needField && (_fields == null || _fields.size() == 0)) {
            throw new TDHSEncodeException("field can't be empty!");
        }
        if (needField && _fields != null && _fields.size() > TDHSCommon.REQUEST_MAX_FIELD_NUM) {
            throw new TDHSEncodeException("too many field , larger than 256!");
        }

    }

    @Override public String toString() {
        return "TableInfo{" +
                "db='" + _db + '\'' +
                ", table='" + _table + '\'' +
                ", index='" + _index + '\'' +
                ", fields=" + _fields +
                ", needField=" + needField +
                '}';
    }
}
