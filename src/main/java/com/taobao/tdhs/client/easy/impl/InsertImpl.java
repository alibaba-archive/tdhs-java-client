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
import com.taobao.tdhs.client.easy.Insert;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.TableInfo;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.statement.Statement;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午4:21
 */
public class InsertImpl implements Insert {

    private final Statement statement;

    private final com.taobao.tdhs.client.request.Insert insert;

    public InsertImpl(Statement statement) {
        this.statement = statement;
        this.insert = new com.taobao.tdhs.client.request.Insert(new TableInfo());
    }

    public Insert use(String db) {
        insert.getTableInfo().setDb(db);
        return this;
    }

    public Insert from(String table) {
        insert.getTableInfo().setTable(table);
        return this;
    }

    public Insert value(String field, @Nullable String value) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(value);
        return this;
    }

    public Insert value(String field, long value) {
        return value(field, String.valueOf(value));
    }

    public Insert value(String field, int value) {
        return value(field, String.valueOf(value));
    }

    public Insert value(String field, short value) {
        return value(field, String.valueOf(value));
    }

    public Insert value(String field, char value) {
        return value(field, String.valueOf(value));
    }

    public Insert valueSetNow(String field) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(TDHSCommon.UpdateFlag.TDHS_UPDATE_NOW, "");
        return this;
    }

    public Insert valueSetNull(String field) {
        return value(field, null);
    }

    public Insert value(String field, TDHSCommon.UpdateFlag flag, String value) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(flag, value);
        return this;
    }

    public TDHSResponse insert() throws TDHSException {
        return statement.insert(insert);
    }

    public TDHSResponse insert(String charsetName) throws TDHSException {
        insert.setCharestName(charsetName);
        return statement.insert(insert);
    }

}
