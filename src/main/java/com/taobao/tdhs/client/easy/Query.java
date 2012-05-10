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

package com.taobao.tdhs.client.easy;

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午2:45
 */
public interface Query {

    /**
     * assign the db
     *
     * @param db of type String
     *
     * @return Query
     */
    Query use(String db);

    /**
     * assign the table.
     *
     * @param table of type String
     *
     * @return Query
     */
    Query from(String table);

    /**
     * select the fields
     *
     * @param fields of type String...
     *
     * @return Query
     */
    Query select(String... fields);

    /**
     * start where condition for index
     *
     * @return Where
     */
    Where where();

    /**
     * start and condition for filter
     *
     * @return And
     */
    And and();

    /**
     * start Set for update values
     *
     * @return Set
     */
    Set set();

    /**
     * set start and limit
     *
     * @param start of type int
     * @param limit of type int
     *
     * @return Query
     */
    Query limit(int start, int limit);

    /**
     * execute get operation
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse get() throws TDHSException;

    /**
     * execute delete operation
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse delete() throws TDHSException;

    /**
     * execute count operation
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse count() throws TDHSException;

    /**
     * execute update operation
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse update() throws TDHSException;

    /**
     * execute get operation with charestName
     *
     * @param charestName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse get(String charestName) throws TDHSException;

    /**
     * execute count operation  with charestName
     *
     * @param charestName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse count(String charestName) throws TDHSException;

    /**
     * execute delete operation  with charestName
     *
     * @param charestName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse delete(String charestName) throws TDHSException;

    /**
     * execute update operation  with charestName
     *
     * @param charestName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse update(String charestName) throws TDHSException;
}
