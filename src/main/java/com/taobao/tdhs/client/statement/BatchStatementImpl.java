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
import com.taobao.tdhs.client.exception.TDHSBatchException;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.exception.TDHSTimeoutException;
import com.taobao.tdhs.client.net.TDHSNet;
import com.taobao.tdhs.client.packet.BasePacket;
import com.taobao.tdhs.client.request.Get;
import com.taobao.tdhs.client.request.RequestWithCharset;
import com.taobao.tdhs.client.response.TDHSMetaData;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-21 下午1:31
 */
public class BatchStatementImpl extends StatementImpl implements BatchStatement {

    private final List<internal_struct> batchRequest = new LinkedList<internal_struct>();

    protected int batchTimeOut = -1;


    public BatchStatementImpl(TDHSNet tdhsNet, AtomicLong id,
                              ConcurrentHashMap<Long, ArrayBlockingQueue<BasePacket>> responses,
                              TDHSCommon.ProtocolVersion version,
                              int timeOut, String charsetName, boolean lowerCaseTableNames) {
        super(tdhsNet, id, responses, version, timeOut, charsetName, lowerCaseTableNames);
    }

    @Override
    public TDHSResponse get(@NotNull Get get) throws TDHSException {
        throw new UnsupportedOperationException("Batch is not support GET operation!");
    }

    @Override
    public TDHSResponse count(@NotNull Get get) throws TDHSException {
        throw new UnsupportedOperationException("Batch is not support COUNT operation!");
    }


    public TDHSResponse[] commit() throws TDHSException {
        ByteArrayOutputStream retData = new ByteArrayOutputStream(2 * 1024);
        long headerId = id.getAndIncrement();
        try {
            try {
                for (internal_struct is : batchRequest) {
                    retData.write(is.getPacket().toByteArray());
                    responses.put(is.getPacket().getSeqId(), new ArrayBlockingQueue<BasePacket>(1));
                }
            } catch (IOException e) {
                throw new TDHSException(e);
            }
            BasePacket headerPacket =
                    new BasePacket(TDHSCommon.RequestType.BATCH, headerId, batchRequest.size(),
                            retData.toByteArray());
            ArrayBlockingQueue<BasePacket> queue = new ArrayBlockingQueue<BasePacket>(1);
            responses.put(headerId, queue);
            tdhsNet.write(headerPacket);
            return do_real_response(queue);
        } finally {
            responses.remove(headerId);
            for (internal_struct is : batchRequest) {
                responses.remove(is.getPacket().getSeqId());
            }
            batchRequest.clear();
        }
    }

    public void setTimeOut(int timeOut) {
        this.batchTimeOut = timeOut;
    }

    protected int getTimeOut() {
        if (this.batchTimeOut > 0) {
            return this.batchTimeOut;
        } else {
            return this.timeOut;
        }
    }

    private TDHSResponse[] do_real_response(ArrayBlockingQueue<BasePacket> queue) throws TDHSException {
        BasePacket ret = null;
        try {
            ret = queue.poll(getTimeOut(), TimeUnit.MILLISECONDS);
            if (ret == null) {
                throw new TDHSTimeoutException("TimeOut");
            }
            if (!TDHSResponseEnum.ClientStatus.MULTI_STATUS.equals(ret.getClientStatus())) {
                if (ret.getClientStatus() != null && ret.getClientStatus().getStatus() >= 400 &&
                        ret.getClientStatus().getStatus() < 600) {
                    throw new TDHSBatchException(
                            new TDHSResponse(ret.getClientStatus(), null, ret.getData(), charsetName));
                } else {
                    throw new TDHSException("unknown response code! [" + (ret.getClientStatus() != null ?
                            String.valueOf(ret.getClientStatus().getStatus()) : "") + "]");
                }
            }
            if (ret.getBatchNumber() != batchRequest.size()) {
                throw new TDHSException(
                        "unmatch batch size! request is[" + String.valueOf(batchRequest.size()) + "], response is [" +
                                String.valueOf(ret.getBatchNumber()) + "]");
            }

            TDHSResponse result[] = new TDHSResponse[batchRequest.size()];
            int i = 0;
            for (internal_struct is : batchRequest) {
                result[i++] =
                        do_response(responses.get(is.getPacket().getSeqId()), is.getMetaData(), is.getCharsetName());
            }
            return result;
        } catch (InterruptedException e) {
            throw new TDHSException(e);
        }
    }

    @Override
    protected TDHSResponse sendRequest(TDHSCommon.RequestType type, RequestWithCharset request, TDHSMetaData metaData)
            throws TDHSException {
        if (request == null) {
            throw new IllegalArgumentException("request can't be NULL!");
        }
        if (StringUtils.isBlank(request.getCharsetName())) {
            //use default charsetName
            request.setCharsetName(this.charsetName);
        }
        byte data[] = version.getTdhsProtocol().encode(request);
        BasePacket packet = new BasePacket(type, id.getAndIncrement(), data);
        batchRequest.add(new internal_struct(packet, metaData, request.getCharsetName()));
        return null;
    }


    private class internal_struct {
        private BasePacket packet;

        private TDHSMetaData metaData;

        private String charsetName;

        private internal_struct(BasePacket packet, TDHSMetaData metaData, String charsetName) {
            this.packet = packet;
            this.metaData = metaData;
            this.charsetName = charsetName;
        }

        public BasePacket getPacket() {
            return packet;
        }

        public TDHSMetaData getMetaData() {
            return metaData;
        }

        public String getCharsetName() {
            return charsetName;
        }
    }

}
