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

import com.taobao.tdhs.client.net.ConnectionPool;
import com.taobao.tdhs.client.packet.BasePacket;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-10-31 下午1:38
 */
public class TDHSClientHandler extends SimpleChannelUpstreamHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private BasePacket shakeHandeMessage;

    private Map<Long, ArrayBlockingQueue<BasePacket>> responses;

    private TDHSNetForNetty tdhsNetForNetty;

    public TDHSClientHandler(BasePacket shakeHandeMessage,
                             Map<Long, ArrayBlockingQueue<BasePacket>> responses, TDHSNetForNetty tdhsNetForNetty) {
        this.shakeHandeMessage = shakeHandeMessage;
        this.responses = responses;
        this.tdhsNetForNetty = tdhsNetForNetty;
    }

    @Override public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        //握手需要被连接池的写锁保护起来
        this.tdhsNetForNetty.addConnectedConnectionToPool(e.getChannel(), new ConnectionPool.Handler<Channel>() {
            public void execute(Channel channel) {
                channel.write(shakeHandeMessage);
            }
        });
    }


    @Override public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.warn("channelDisconnected!");
        tdhsNetForNetty.needCloseChannel(e.getChannel());
    }

    @Override public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        BasePacket packet = (BasePacket) e.getMessage();
        ArrayBlockingQueue<BasePacket> blockingQueue = responses.get(packet.getSeqId());
        if (blockingQueue != null) {
            blockingQueue.put(packet);
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an com.taobao.tdhs.client.exception is raised.
        logger.error("exceptionCaught!", e.getCause());
        tdhsNetForNetty.needCloseChannel(e.getChannel());
    }
}
