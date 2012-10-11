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

package com.taobao.tdhs.client;

import com.taobao.tdhs.client.statement.BatchStatement;
import com.taobao.tdhs.client.statement.Statement;

import java.util.concurrent.TimeUnit;

/**
 * The TDH_Socket Client interface
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-26 下午4:53
 */
public interface TDHSClient extends Statement {

    public static final String CONNECTION_NUMBER = "connectionNumber";
    public static final String TIME_OUT = "timeOut";
    public static final String NEED_RECONNECT = "needReconnect";
    public static final String CONNECT_TIMEOUT = "connectTimeout";
    public static final String CHARSET_NAME = "charsetName";
    public static final String READ_CODE = "readCode";
    public static final String WRITE_CODE = "writeCode";
    public static final String LOWER_CASE_TABLE_NAMES = "lowerCaseTableNames";
    public static final String VERSION = "1";


    /**
     * Method getCharsetName returns the charsetName of this TDHSClient object.
     * like <code>UTF8</code> for the string encoding when read and send
     *
     * @return the charsetName (type String) of this TDHSClient object.
     */
    String getCharsetName();

    /**
     * Method setCharsetName sets the charsetName of this TDHSClient object.
     * like <code>UTF8</code> for the string encoding when read and send
     *
     * @param charsetName the charsetName of this TDHSClient object.
     */
    void setCharsetName(String charsetName);

    /**
     * Method isLowerCaseTableNames returns the lowerCaseTableNames of this TDHSClient object.
     *
     * @return the lowerCaseTableNames (type boolean) of this TDHSClient object.
     */
    boolean isLowerCaseTableNames();

    /**
     * Method setLowerCaseTableNames sets the lowerCaseTableNames of this TDHSClient object.
     *
     * @param lowerCaseTableNames the lowerCaseTableNames of this TDHSClient object.
     */
    void setLowerCaseTableNames(boolean lowerCaseTableNames);


    /**
     * wait for connected
     *
     * @param timeout of type long
     * @param unit    of type TimeUnit
     *
     * @return boolean , <code>true</code> mean connected ,<code>false</code> mean not connected
     */
    boolean awaitForConnected(long timeout, TimeUnit unit);

    /**
     * Method createStatement ...
     * <p/>
     * create a statement for one thread
     *
     * @return Statement
     */
    Statement createStatement();

    /**
     * Method createStatement ...
     * <p/>
     * create a statement for one thread
     *
     * @param hash of type int,  use hash to make sure the request from this statement will
     *             be run a assigned thread on the server-side
     *             the thread is <code>hash%thread_count</code>
     *
     * @return Statement
     */
    Statement createStatement(int hash);

    /**
     * create Batch Statement ...
     *
     * @return BatchStatement
     */
    BatchStatement createBatchStatement();

    /**
     * shutdown the client
     * stop the thread for io
     */
    void shutdown();
}
