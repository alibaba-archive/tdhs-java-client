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

package com.taobao.tdhs.client.response;

import com.taobao.tdhs.client.request.TableInfo;

import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午1:46
 */
public class TDHSMetaData {

    private String db;

    private String table;

    private List<String> fieldNames;

    public TDHSMetaData(TableInfo info) {
        this.db = info.getDb();
        this.table = info.getTable();
        this.fieldNames = info.getFields();
    }


    public TDHSMetaData(TableInfo info, List<String> fieldNames) {
        this.db = info.getDb();
        this.table = info.getTable();
        this.fieldNames = fieldNames;
    }


    public TDHSMetaData(String db, String table, List<String> fieldNames) {
        this.db = db;
        this.table = table;
        this.fieldNames = fieldNames;
    }

    public String getDb() {
        return db;
    }

    public String getTable() {
        return table;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }
}
