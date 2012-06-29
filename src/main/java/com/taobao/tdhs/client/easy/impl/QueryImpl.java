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

import com.taobao.tdhs.client.easy.And;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.easy.Set;
import com.taobao.tdhs.client.easy.Where;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.request.Get;
import com.taobao.tdhs.client.request.TableInfo;
import com.taobao.tdhs.client.request.Update;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.statement.Statement;

import java.util.Collections;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:27
 */
public class QueryImpl implements Query {
    private final Get get;

    private final Update update;

    private final Where where;

    private final And and;

    private final Set set;

    private final Statement statement;

    public QueryImpl(Statement statement) {
        this.statement = statement;
        this.get = new Get(new TableInfo());
        this.update = new Update(this.get);
        this.where = new WhereImpl(get, this);
        this.and = new AndImpl(get, this);
        this.set = new SetImpl(get, update, this);
    }

    public Query use(String db) {
        get.getTableInfo().setDb(db);
        return this;
    }

    public Query from(String table) {
        get.getTableInfo().setTable(table);
        return this;
    }

    public Query select(String... fields) {
        if (fields != null) {
            Collections.addAll(get.getTableInfo().getFields(), fields);
        }
        return this;
    }

    public Where where() {
        return this.where;
    }

    public And and() {
        return this.and;
    }

    public Set set() {
        return this.set;
    }

    public Query limit(int start, int limit) {
        get.setStart(start);
        get.setLimit(limit);
        return this;
    }

    public TDHSResponse get() throws TDHSException {
        return statement.get(get);
    }

    public TDHSResponse delete() throws TDHSException {
        return statement.delete(get);
    }

    public TDHSResponse count() throws TDHSException {
        return statement.count(get);
    }

    public TDHSResponse update() throws TDHSException {
        return statement.update(update);
    }

    public TDHSResponse get(String charsetName) throws TDHSException {
        get.setCharsetName(charsetName);
        return statement.get(get);
    }

    public TDHSResponse count(String charsetName) throws TDHSException {
        get.setCharsetName(charsetName);
        return statement.count(get);
    }

    public TDHSResponse delete(String charsetName) throws TDHSException {
        get.setCharsetName(charsetName);
        return statement.delete(get);
    }

    public TDHSResponse update(String charsetName) throws TDHSException {
        update.setCharsetName(charsetName);
        return statement.update(update);
    }
}
