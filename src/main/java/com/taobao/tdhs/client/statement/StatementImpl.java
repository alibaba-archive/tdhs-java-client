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

package com.taobao.tdhs.client.statement;

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.easy.Query;
import com.taobao.tdhs.client.easy.impl.InsertImpl;
import com.taobao.tdhs.client.easy.impl.QueryImpl;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.exception.TDHSTimeoutException;
import com.taobao.tdhs.client.net.TDHSNet;
import com.taobao.tdhs.client.packet.BasePacket;
import com.taobao.tdhs.client.protocol.TDHSProtocol;
import com.taobao.tdhs.client.request.*;
import com.taobao.tdhs.client.response.TDHSMetaData;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-20 下午4:45
 */
public class StatementImpl implements Statement {
    protected final TDHSNet tdhsNet;

    protected final AtomicLong id;

    protected final ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>> responses;

    protected final TDHSProtocol protocol;

    protected final int timeOut; //ms

    protected String charsetName;

    private long hash = 0;

    public StatementImpl(TDHSNet tdhsNet, AtomicLong id,
                         ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>> responses,
                         TDHSProtocol protocol, int timeOut, String charsetName) {
        this.tdhsNet = tdhsNet;
        this.id = id;
        this.responses = responses;
        this.protocol = protocol;
        this.timeOut = timeOut;
        this.charsetName = charsetName;
        this.hash = 0;
    }


    public StatementImpl(TDHSNet tdhsNet, AtomicLong id,
                         ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>> responses,
                         TDHSProtocol protocol, int timeOut, String charsetName, int hash) {
        this.tdhsNet = tdhsNet;
        this.id = id;
        this.responses = responses;
        this.protocol = protocol;
        this.timeOut = timeOut;
        this.charsetName = charsetName;
        this.hash = hash & 0xFFFFFFFFL;
    }

    // just get one
    public TDHSResponse get(@NotNull String db, @NotNull String table, String index, @NotNull String fields[],
                            @NotNull String keys[][])
            throws TDHSException {
        return get(db, table, index, fields, keys, TDHSCommon.FindFlag.TDHS_EQ, 0, 1, null);
    }


    public TDHSResponse get(@NotNull String db, @NotNull String table, @Nullable String index, @NotNull String fields[],
                            @NotNull String keys[][], @NotNull TDHSCommon.FindFlag findFlag, int start,
                            int limit, @Nullable Filter filters[])
            throws TDHSException {
        Get get = new Get(new TableInfo(db, table, index, fields), keys, findFlag, start, limit, filters);
        return get(get);
    }

    public TDHSResponse get(@NotNull Get get) throws TDHSException {
        if (get == null) {
            throw new IllegalArgumentException("get is null!");
        }
        return sendRequest(TDHSCommon.RequestType.GET, get, new TDHSMetaData(get.getTableInfo()));
    }

    public TDHSResponse count(@NotNull String db, @NotNull String table, String index, @NotNull String[][] keys,
                              @NotNull TDHSCommon.FindFlag findFlag, int start, int limit, @Nullable Filter[] filters)
            throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, index, null);
        Get get = new Get(tableInfo, keys, findFlag, start, limit, filters);
        return count(get);
    }

    public TDHSResponse count(@NotNull Get get) throws TDHSException {
        if (get == null) {
            throw new IllegalArgumentException("get is null!");
        }
        if (get.getTableInfo() != null) {
            get.getTableInfo().setNeedField(false);
        }
        return sendRequest(TDHSCommon.RequestType.COUNT, get,
                new TDHSMetaData(get.getTableInfo(), Arrays.asList("count(*)")));
    }

    public TDHSResponse delete(@NotNull String db, @NotNull String table, String index, @NotNull String keys[][],
                               @NotNull TDHSCommon.FindFlag findFlag, int start,
                               int limit, @Nullable Filter filters[])
            throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, index, null);
        Get get = new Get(tableInfo, keys, findFlag, start, limit, filters);
        return delete(get);
    }

    public TDHSResponse delete(@NotNull Get get) throws TDHSException {
        if (get == null) {
            throw new IllegalArgumentException("get is null!");
        }
        if (get.getTableInfo() != null) {
            get.getTableInfo().setNeedField(false);
        }
        return sendRequest(TDHSCommon.RequestType.DELETE, get,
                new TDHSMetaData(get.getTableInfo(), Arrays.asList("deleted", "changed")));
    }

    public TDHSResponse update(@NotNull String db, @NotNull String table, String index, @NotNull String fields[],
                               @NotNull ValueEntry valueEntry[],
                               @NotNull String keys[][],
                               @NotNull TDHSCommon.FindFlag findFlag, int start,
                               int limit, @Nullable Filter filters[])
            throws TDHSException {
        Get get = new Get(new TableInfo(db, table, index, fields), keys, findFlag, start, limit, filters);
        Update update = new Update(get, valueEntry);
        return update(update);
    }

    public TDHSResponse update(@NotNull Update update) throws TDHSException {
        if (update == null) {
            throw new IllegalArgumentException("update is null!");
        }
        return sendRequest(TDHSCommon.RequestType.UPDATE, update,
                new TDHSMetaData(update.getGet().getTableInfo(), Arrays.asList("updated", "changed")));
    }


    public TDHSResponse insert(@NotNull String db, @NotNull String table, @NotNull String fields[],
                               @NotNull String values[])
            throws TDHSException {
        TableInfo tableInfo = new TableInfo(db, table, null, fields);
        Insert insert = new Insert(tableInfo, values);
        return insert(insert);
    }

    public TDHSResponse insert(@NotNull Insert insert) throws TDHSException {
        if (insert == null) {
            throw new IllegalArgumentException("insert is null!");
        }
        return sendRequest(TDHSCommon.RequestType.INSERT, insert,
                new TDHSMetaData(insert.getTableInfo(), Arrays.asList("lastid")));
    }

    public Query query() {
        return new QueryImpl(this);
    }

    public com.taobao.tdhs.client.easy.Insert insert() {
        return new InsertImpl(this);
    }

    protected TDHSResponse sendRequest(TDHSCommon.RequestType type, RequestWithCharest request, TDHSMetaData metaData)
            throws TDHSException {
        if (request == null) {
            throw new IllegalArgumentException("request can't be NULL!");
        }
        if (StringUtils.isBlank(request.getCharsetName())) {
            //use default charsetName
            request.setCharsetName(this.charsetName);
        }
        byte data[] = protocol.encode(request);
        BasePacket packet = new BasePacket(type, id.getAndIncrement(), hash, data);
        ArrayBlockingQueue<BasePacket> queue = new ArrayBlockingQueue<BasePacket>(1);
        long seqId = packet.getSeqId();
        responses.put(seqId, queue);
        try {
            tdhsNet.write(packet);
            return do_response(queue, metaData, request.getCharsetName());
        } finally {
            responses.remove(seqId);
            queue = null;
        }
    }

    protected TDHSResponse do_response(ArrayBlockingQueue<BasePacket> queue, TDHSMetaData metaData,
                                       String charsetName)
            throws TDHSException {
        ByteArrayOutputStream retData = new ByteArrayOutputStream();
        try {
            while (true) {
                BasePacket ret = null;
                ret = queue.poll(timeOut, TimeUnit.MILLISECONDS);
                if (ret == null) {
                    throw new TDHSTimeoutException("TimeOut");
                } else {
                    if (TDHSResponseEnum.ClientStatus.ACCEPT.equals(ret.getClientStatus())) {
                        retData.write(ret.getData());
                    } else if (TDHSResponseEnum.ClientStatus.OK.equals(ret.getClientStatus())) {
                        retData.write(ret.getData());
                        return new TDHSResponse(ret.getClientStatus(), metaData, retData.toByteArray(),
                                charsetName);
                    } else if (ret.getClientStatus() != null && ret.getClientStatus().getStatus() >= 400 &&
                            ret.getClientStatus().getStatus() < 600) {
                        return new TDHSResponse(ret.getClientStatus(), metaData, ret.getData(), charsetName);
                    } else {
                        throw new TDHSException("unknown response code! [" + (ret.getClientStatus() != null ?
                                String.valueOf(ret.getClientStatus().getStatus()) : "") + "]");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new TDHSException(e);
        } catch (IOException e) {
            throw new TDHSException(e);
        }
    }

}
