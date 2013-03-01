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

import java.util.List;

/**
 * Where condition
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午2:49
 */
public interface Where {

    /**
     * assign the fields in the index
     *
     * @param field of type String...
     *
     * @return Where
     */
    Where fields(String... field);

    /**
     * assign the ndex
     *
     * @param index of type String
     *
     * @return Where
     */
    Where index(String index);

    /**
     * Method equal ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query equal(String... key);

    /**
     * Method descEqual ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query descEqual(String... key);

    /**
     * Method greaterEqual ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query greaterEqual(String... key);

    /**
     * Method lessEqual ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query lessEqual(String... key);

    /**
     * Method greaterThan ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query greaterThan(String... key);

    /**
     * Method lessThan ...
     *
     * @param key of type String...
     *
     * @return Query
     */
    Query lessThan(String... key);

    /**
     * Method in ...
     *
     * @param keys of type String[]...
     *
     * @return Query
     */
    Query in(String[]... keys);

    /**
     * Method in ...
     *
     * @param keys of type List<String>...
     *
     * @return Query
     */
    Query in(List<String>... keys);

    /**
     * Between query.
     *
     * @param keys the keys
     *
     * @return the query
     */
    Query between(String[]... keys);

}
