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

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.exception.TDHSTimeoutException;
import com.taobao.tdhs.client.net.NetParameters;
import com.taobao.tdhs.client.net.TDHSNet;
import com.taobao.tdhs.client.net.netty.TDHSNetForNetty;
import com.taobao.tdhs.client.packet.BasePacket;
import com.taobao.tdhs.client.protocol.TDHSProtocol;
import com.taobao.tdhs.client.request.*;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.statement.BatchStatement;
import com.taobao.tdhs.client.statement.BatchStatementImpl;
import com.taobao.tdhs.client.statement.Statement;
import com.taobao.tdhs.client.statement.StatementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-10-31 下午1:32
 */
public class TDHSClientImpl implements TDHSClient {

    private final TDHSNet tdhsNet;

    private static final AtomicLong id = new AtomicLong(1L);

    private static final ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>> responses =
            new ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>>();

    private final TDHSProtocol protocol;

    private final int timeOut; //ms

    private String charsetName;


    /**
     * Constructor TDHSClientImpl creates a new TDHSClientImpl instance.
     *
     * @param address of type InetSocketAddress
     * @param props   of type Map
     *
     * @throws TDHSException when
     */
    public TDHSClientImpl(InetSocketAddress address, Map props) throws TDHSException {
        this(address, props.containsKey(CONNECTION_NUMBER) ? (Integer) props.get(CONNECTION_NUMBER) : 1,
                props.containsKey(TIME_OUT) ? (Integer) props.get(TIME_OUT) : 1000,
                props.containsKey(NEED_RECONNECT) ? (Boolean) props.get(NEED_RECONNECT) : true,
                props.containsKey(CONNECT_TIMEOUT) ? (Integer) props.get(CONNECT_TIMEOUT) : 1000,
                props.containsKey(CHAREST_NAME) ? (String) props.get(CHAREST_NAME) : null,
                props.containsKey(READ_CODE) ? (String) props.get(READ_CODE) : null,
                props.containsKey(WRITE_CODE) ? (String) props.get(WRITE_CODE) : null);
    }

    /**
     * Constructor TDHSClientImpl creates a new TDHSClientImpl instance.
     *
     * @param address          of type InetSocketAddress
     * @param connectionNumber of type int
     *
     * @throws TDHSException when
     */
    public TDHSClientImpl(InetSocketAddress address, int connectionNumber) throws TDHSException {
        this(address, connectionNumber, 1000);
    }

    /**
     * Constructor TDHSClientImpl creates a new TDHSClientImpl instance.
     *
     * @param address          of type InetSocketAddress
     * @param connectionNumber of type int
     * @param timeOut          of type int
     *
     * @throws TDHSException when
     */
    public TDHSClientImpl(InetSocketAddress address, int connectionNumber, int timeOut) throws TDHSException {
        this(address, connectionNumber, timeOut, true, 1000);
    }

    /**
     * Constructor TDHSClientImpl creates a new TDHSClientImpl instance.
     *
     * @param address          of type InetSocketAddress
     * @param connectionNumber of type int
     * @param timeOut          of type int
     * @param needReconnect    of type boolean
     * @param connectTimeOut   of type int
     *
     * @throws TDHSException when
     */
    public TDHSClientImpl(InetSocketAddress address, int connectionNumber, int timeOut, boolean needReconnect,
                          int connectTimeOut) throws TDHSException {
        this(address, connectionNumber, timeOut, needReconnect, connectTimeOut, null, null, null);
    }

    /**
     * Constructor TDHSClientImpl creates a new TDHSClientImpl instance.
     *
     * @param address          of type InetSocketAddress
     * @param connectionNumber of type int
     * @param timeOut          of type int
     * @param needReconnect    of type boolean
     * @param connectTimeOut   of type int
     * @param charsetName      of type String
     * @param readCode         of type String
     * @param writeCode        of type String
     *
     * @throws TDHSException when
     */
    public TDHSClientImpl(InetSocketAddress address, int connectionNumber, int timeOut, boolean needReconnect,
                          int connectTimeOut, @Nullable String charsetName, @Nullable String readCode,
                          @Nullable String writeCode)
            throws TDHSException {

        if (connectionNumber <= 0) {
            throw new IllegalArgumentException("connectionNumber must be positive!");
        }
        this.timeOut = timeOut;
        protocol = TDHSCommon.PROTOCOL_FOR_BINARY;
        tdhsNet = new TDHSNetForNetty();
        this.charsetName = charsetName;
        NetParameters parameters = new NetParameters();
        parameters.setAddress(address);
        parameters.setConnectionNumber(connectionNumber);
        parameters.setNeedReconnect(needReconnect);
        tdhsNet.initNet(parameters, protocol.shakeHandPacket(this.timeOut, readCode, writeCode), responses);
        if (connectTimeOut > 0 && !awaitForConnected(connectTimeOut, TimeUnit.MILLISECONDS)) {
            throw new TDHSTimeoutException("connect time out");
        }
    }


    /**
     * @see TDHSClient#awaitForConnected(long, TimeUnit)
     */
    public boolean awaitForConnected(long timeout, TimeUnit unit) {
        return tdhsNet.awaitForConnected(timeout, unit);
    }

    /**
     * Method getCharsetName returns the charsetName of this TDHSClientImpl object.
     *
     * @return the charsetName (type String) of this TDHSClientImpl object.
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Method setCharsetName sets the charsetName of this TDHSClientImpl object.
     *
     * @param charsetName the charsetName of this TDHSClientImpl object.
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * Method createStatement ...
     *
     * @return Statement
     */
    public Statement createStatement() {
        return new StatementImpl(tdhsNet, id, responses, protocol, timeOut, charsetName);
    }

    /**
     * Method createStatement ...
     *
     * @param hash of type int
     *
     * @return Statement
     */
    public Statement createStatement(int hash) {
        return new StatementImpl(tdhsNet, id, responses, protocol, timeOut, charsetName, hash);
    }

    /**
     * Method createBatchStatement ...
     *
     * @return BatchStatement
     */
    public BatchStatement createBatchStatement() {
        return new BatchStatementImpl(tdhsNet, id, responses, protocol, timeOut, charsetName);
    }

    /**
     * Method get ...
     *
     * @param db     of type String
     * @param table  of type String
     * @param index  of type String
     * @param fields of type String[]
     * @param keys   of type String[][]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    // just get one
    public TDHSResponse get(@NotNull String db, @NotNull String table, @Nullable String index, @NotNull String fields[],
                            @NotNull String keys[][])
            throws TDHSException {
        return createStatement().get(db, table, index, fields, keys);
    }


    /**
     * Method get ...
     *
     * @param db       of type String
     * @param table    of type String
     * @param index    of type String
     * @param fields   of type String[]
     * @param keys     of type String[][]
     * @param findFlag of type FindFlag
     * @param start    of type int
     * @param limit    of type int
     * @param filters  of type Filter[]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse get(@NotNull String db, @NotNull String table, @Nullable String index, @NotNull String fields[],
                            @NotNull String keys[][], @NotNull TDHSCommon.FindFlag findFlag, int start,
                            int limit, @Nullable Filter filters[])
            throws TDHSException {
        return createStatement().get(db, table, index, fields, keys, findFlag, start, limit, filters);
    }

    /**
     * Method get ...
     *
     * @param get of type Get
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse get(@NotNull Get get) throws TDHSException {
        return createStatement().get(get);
    }

    /**
     * Method count ...
     *
     * @param db       of type String
     * @param table    of type String
     * @param index    of type String
     * @param keys     of type String[][]
     * @param findFlag of type FindFlag
     * @param start    of type int
     * @param limit    of type int
     * @param filters  of type Filter[]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse count(@NotNull String db, @NotNull String table, String index, @NotNull String[][] keys,
                              @NotNull TDHSCommon.FindFlag findFlag, int start, int limit, @Nullable Filter[] filters)
            throws TDHSException {
        return createStatement().count(db, table, index, keys, findFlag, start, limit, filters);
    }

    /**
     * Method count ...
     *
     * @param get of type Get
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse count(@NotNull Get get) throws TDHSException {
        return createStatement().count(get);
    }

    /**
     * Method delete ...
     *
     * @param db       of type String
     * @param table    of type String
     * @param index    of type String
     * @param keys     of type String[][]
     * @param findFlag of type FindFlag
     * @param start    of type int
     * @param limit    of type int
     * @param filters  of type Filter[]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse delete(@NotNull String db, @NotNull String table, String index, @NotNull String keys[][],
                               @NotNull TDHSCommon.FindFlag findFlag, int start,
                               int limit, @Nullable Filter filters[])
            throws TDHSException {
        return createStatement().delete(db, table, index, keys, findFlag, start, limit, filters);
    }

    /**
     * Method delete ...
     *
     * @param get of type Get
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse delete(@NotNull Get get) throws TDHSException {
        return createStatement().delete(get);
    }

    /**
     * Method update ...
     *
     * @param db         of type String
     * @param table      of type String
     * @param index      of type String
     * @param fields     of type String[]
     * @param valueEntry of type ValueEntry[]
     * @param keys       of type String[][]
     * @param findFlag   of type FindFlag
     * @param start      of type int
     * @param limit      of type int
     * @param filters    of type Filter[]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse update(@NotNull String db, @NotNull String table, String index, @NotNull String fields[],
                               @NotNull ValueEntry valueEntry[],
                               @NotNull String keys[][],
                               @NotNull TDHSCommon.FindFlag findFlag, int start,
                               int limit, @Nullable Filter filters[])
            throws TDHSException {
        return createStatement().update(db, table, index, fields, valueEntry, keys, findFlag, start, limit, filters);
    }

    /**
     * Method update ...
     *
     * @param update of type Update
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse update(@NotNull Update update) throws TDHSException {
        return createStatement().update(update);
    }


    /**
     * Method insert ...
     *
     * @param db     of type String
     * @param table  of type String
     * @param fields of type String[]
     * @param values of type String[]
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse insert(@NotNull String db, @NotNull String table, @NotNull String fields[],
                               @NotNull String values[])
            throws TDHSException {
        return createStatement().insert(db, table, fields, values);
    }

    /**
     * Method insert ...
     *
     * @param insert of type Insert
     *
     * @return TDHSResponse
     *
     * @throws TDHSException when
     */
    public TDHSResponse insert(@NotNull Insert insert) throws TDHSException {
        return createStatement().insert(insert);
    }

    /**
     * Method query ...
     *
     * @return Query
     */
    public Query query() {
        return createStatement().query();
    }

    /**
     * Method insert ...
     *
     * @return Insert
     */
    public com.taobao.tdhs.client.easy.Insert insert() {
        return createStatement().insert();
    }

    /**
     * Method shutdown ...
     */
    public void shutdown() {
        tdhsNet.release();
    }

}
