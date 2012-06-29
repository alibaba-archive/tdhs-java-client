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

    /**
     * Constructor InsertImpl creates a new InsertImpl instance.
     *
     * @param statement of type Statement
     */
    public InsertImpl(Statement statement) {
        this.statement = statement;
        this.insert = new com.taobao.tdhs.client.request.Insert(new TableInfo());
    }

    /**
     * Method use ...
     *
     * @param db of type String
     *
     * @return Insert
     */
    public Insert use(String db) {
        insert.getTableInfo().setDb(db);
        return this;
    }

    /**
     * Method from ...
     *
     * @param table of type String
     *
     * @return Insert
     */
    public Insert from(String table) {
        insert.getTableInfo().setTable(table);
        return this;
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type String
     *
     * @return Insert
     */
    public Insert value(String field, @Nullable String value) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(value);
        return this;
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type long
     *
     * @return Insert
     */
    public Insert value(String field, long value) {
        return value(field, String.valueOf(value));
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type int
     *
     * @return Insert
     */
    public Insert value(String field, int value) {
        return value(field, String.valueOf(value));
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type short
     *
     * @return Insert
     */
    public Insert value(String field, short value) {
        return value(field, String.valueOf(value));
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type char
     *
     * @return Insert
     */
    public Insert value(String field, char value) {
        return value(field, String.valueOf(value));
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type byte
     *
     * @return Insert
     */
    public Insert value(String field, byte value) {
        return value(field, String.valueOf(value));
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param value of type byte[]
     *
     * @return Insert
     */
    public Insert value(String field, byte[] value) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(value);
        return this;
    }

    /**
     * Method valueSetNow ...
     *
     * @param field of type String
     *
     * @return Insert
     */
    public Insert valueSetNow(String field) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(TDHSCommon.UpdateFlag.TDHS_UPDATE_NOW, "");
        return this;
    }

    /**
     * Method valueSetNull ...
     *
     * @param field of type String
     *
     * @return Insert
     */
    public Insert valueSetNull(String field) {
        return value(field, (String) null);
    }

    /**
     * Method value ...
     *
     * @param field of type String
     * @param flag  of type UpdateFlag
     * @param value of type String
     *
     * @return Insert
     */
    public Insert value(String field, TDHSCommon.UpdateFlag flag, String value) {
        insert.getTableInfo().getFields().add(field);
        insert.addValue(flag, value);
        return this;
    }

    /**
     * Method insert ...
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse insert() throws TDHSException {
        return statement.insert(insert);
    }

    /**
     * Method insert ...
     *
     * @param charsetName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse insert(String charsetName) throws TDHSException {
        insert.setCharsetName(charsetName);
        return statement.insert(insert);
    }

}
