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

package com.taobao.tdhs.client.net;

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.packet.BasePacket;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-26 下午4:08
 */
public interface TDHSNet {

    /**
     * init the net parameters
     *
     * @param parameters      of type NetParameters
     * @param shakeHandPacket of type BasePacket
     * @param responses       of type Map<Long, ArrayBlockingQueue<BasePacket>>
     */
    void initNet(NetParameters parameters, BasePacket shakeHandPacket,
                 Map<Long, ArrayBlockingQueue<BasePacket>> responses);

    /**
     * await for connected ...
     *
     * @param timeout of type long
     * @param unit    of type TimeUnit
     *
     * @return boolean
     */
    boolean awaitForConnected(long timeout, TimeUnit unit);

    /**
     * write packet
     *
     * @param packet of type BasePacket
     *
     * @throws TDHSException when
     */
    void write(BasePacket packet) throws TDHSException;

    /**
     * release resource when shutdown
     */
    void release();

}
