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

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:04
 */
public interface And {
    /**
     * assign field
     *
     * @param field of type String
     *
     * @return And
     */
    And field(String field);

    /**
     * field equal value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query equal(String value);

    /**
     * field greaterEqual value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query greaterEqual(String value);

    /**
     * field lessEqual value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query lessEqual(String value);

    /**
     * field greaterThan value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query greaterThan(String value);

    /**
     * field lessThan value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query lessThan(String value);

    /**
     * field not value
     *
     * @param value of type String
     *
     * @return Query
     */
    Query not(String value);

    /**
     * field is null
     *
     * @return Query
     */
    Query isNull();
}
