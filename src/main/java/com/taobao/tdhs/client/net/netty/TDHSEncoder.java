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

package com.taobao.tdhs.client.net.netty;

import com.taobao.tdhs.client.packet.BasePacket;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.nio.ByteOrder;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-10-31 下午2:50
 */
public class TDHSEncoder extends OneToOneEncoder {
    @Override protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (!(msg instanceof BasePacket)) {
            return msg;
        }
        return ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, ((BasePacket) msg).toByteArray());
    }
}
