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

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-27 下午3:22
 */
public interface Insert {

    /**
     * assign the db
     *
     * @param db of type String
     *
     * @return Insert
     */
    Insert use(String db);

    /**
     * assign the table
     *
     * @param table of type String
     *
     * @return Insert
     */
    Insert from(String table);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type String
     *
     * @return Insert
     */
    Insert value(String field, @Nullable String value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type long
     *
     * @return Insert
     */
    Insert value(String field, long value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type int
     *
     * @return Insert
     */
    Insert value(String field, int value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type short
     *
     * @return Insert
     */
    Insert value(String field, short value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type char
     *
     * @return Insert
     */
    Insert value(String field, char value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type byte
     *
     * @return Insert
     */
    Insert value(String field, byte value);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param value of type byte[]
     *
     * @return Insert
     */
    Insert value(String field, byte[] value);

    /**
     * insert now() into field
     *
     * @param field of type String
     *
     * @return Insert
     */
    Insert valueSetNow(String field);

    /**
     * insert null into field
     *
     * @param field of type String
     *
     * @return Insert
     */
    Insert valueSetNull(String field);

    /**
     * insert the value into field
     *
     * @param field of type String
     * @param flag  of type UpdateFlag
     * @param value of type String
     *
     * @return Insert
     */
    Insert value(String field, TDHSCommon.UpdateFlag flag, String value);

    /**
     * execute insert operation
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse insert() throws TDHSException;

    /**
     * execute insert operation with charsetName
     *
     * @param charsetName of type String
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    TDHSResponse insert(String charsetName) throws TDHSException;

}
