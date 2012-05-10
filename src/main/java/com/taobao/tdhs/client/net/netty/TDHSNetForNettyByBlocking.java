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

import com.taobao.tdhs.client.net.NetParameters;
import com.taobao.tdhs.client.net.TDHSNet;
import com.taobao.tdhs.client.packet.BasePacket;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * 没有被使用哦~
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-26 下午4:21
 */
public class TDHSNetForNettyByBlocking extends TDHSNetForNetty implements TDHSNet {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void _initNet(NetParameters parameters, BasePacket shakeHandPacket,
                            Map<Long, ArrayBlockingQueue<BasePacket>> responses) {
        parameters.isVaild();
        bootstrap = new ClientBootstrap(
                new OioClientSocketChannelFactory(
                        Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(
                new TDHSPiplelineFactoty(shakeHandPacket, responses, this));
    }


}
