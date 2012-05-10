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

import org.jetbrains.annotations.Nullable;

/**
 * Set Value when update
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:15
 */
public interface Set {

    /**
     * set field which need be update
     *
     * @param field of type String
     *
     * @return Set
     */
    Set field(String field);

    /**
     * add some number to field
     *
     * @param value of type long
     *
     * @return Query
     */
    Query add(long value);

    /**
     * sub some number to field
     *
     * @param value of type long
     *
     * @return Query
     */
    Query sub(long value);

    /**
     * set some value to field
     *
     * @param value of type String
     *
     * @return Query
     */
    Query set(@Nullable String value);

    /**
     * set some number to field
     *
     * @param value of type long
     *
     * @return Query
     */
    Query set(long value);

    /**
     * set some number to field
     *
     * @param value of type int
     *
     * @return Query
     */
    Query set(int value);

    /**
     * set some number to field
     *
     * @param value of type short
     *
     * @return Query
     */
    Query set(short value);

    /**
     * set some number to field
     *
     * @param value of type char
     *
     * @return Query
     */
    Query set(char value);

    /**
     * set now() to field
     *
     * @return Query
     */
    Query setNow();

    /**
     * set null to field
     *
     * @return Query
     */
    Query setNull();
}
