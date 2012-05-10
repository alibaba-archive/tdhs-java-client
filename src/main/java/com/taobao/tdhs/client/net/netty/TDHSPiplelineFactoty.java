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
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-10-31 下午4:04
 */
public class TDHSPiplelineFactoty implements ChannelPipelineFactory {
    private BasePacket shakeHandeMessage;

    private Map<Long, ArrayBlockingQueue<BasePacket>> responses;

    private TDHSNetForNetty tdhsNetForNetty;

    public TDHSPiplelineFactoty(BasePacket shakeHandeMessage,
                                Map<Long, ArrayBlockingQueue<BasePacket>> responses, TDHSNetForNetty tdhsNetForNetty) {
        this.responses = responses;
        this.shakeHandeMessage = shakeHandeMessage;
        this.tdhsNetForNetty = tdhsNetForNetty;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new TDHSDecoder());
        pipeline.addLast("encoder", new TDHSEncoder());
        pipeline.addLast("handler", new TDHSClientHandler(shakeHandeMessage, responses, tdhsNetForNetty));
        return pipeline;
    }
}
