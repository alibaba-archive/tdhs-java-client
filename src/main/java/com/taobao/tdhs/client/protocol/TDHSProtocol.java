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

package com.taobao.tdhs.client.protocol;

import com.taobao.tdhs.client.exception.TDHSEncodeException;
import com.taobao.tdhs.client.packet.BasePacket;
import com.taobao.tdhs.client.request.RequestWithCharest;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-2 上午11:09
 */
public interface TDHSProtocol {
    /**
     * get the shakeHand Packet
     *
     * @param timeOut
     *
     * @return BasePacket
     */
    BasePacket shakeHandPacket(int timeOut, @Nullable String readCode,
                               @Nullable String writeCode);

    /**
     * encode Request to packet data
     *
     * @param o of type Object  ,request
     *
     * @return byte[] ,the packet data
     *
     * @throws com.taobao.tdhs.client.exception.TDHSEncodeException
     *
     */
    byte[] encode(RequestWithCharest o) throws TDHSEncodeException;
}
