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

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.net.AbstractTDHSNet;
import com.taobao.tdhs.client.net.ConnectionPool;
import com.taobao.tdhs.client.net.NetParameters;
import com.taobao.tdhs.client.net.TDHSNet;
import com.taobao.tdhs.client.packet.BasePacket;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-26 下午4:21
 */
public class TDHSNetForNetty extends AbstractTDHSNet<Channel> implements TDHSNet {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ClientBootstrap bootstrap;

    private transient boolean isReleasing = false;


    public void write(BasePacket packet) throws TDHSException {
        Channel channel = connectionPool.get();
        if (channel == null) {
            throw new TDHSException("no available connection! maybe server has something error!");
        }
        channel.write(packet);
    }

    protected void _initNet(NetParameters parameters, BasePacket shakeHandPacket,
                            Map<Long, ArrayBlockingQueue<BasePacket>> responses) {
        parameters.isVaild();
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(
                new TDHSPiplelineFactoty(shakeHandPacket, responses, this));
    }

    @Override
    protected Channel _connect(InetSocketAddress address) {
        ChannelFuture future = bootstrap.connect(address);
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            logger.error("connect failed!", future.getCause());
            return null;
        } else {
            return channel;
        }
    }

    protected void _release() {
        logger.warn("client is Releasing now!");
        isReleasing = true;
        connectionPool.close(new ConnectionPool.Handler<Channel>() {
            public void execute(Channel channel) {
                channel.close();
            }
        });
        bootstrap.releaseExternalResources();
    }

    public void needCloseChannel(Channel channel) {
        if (isReleasing) {
            //Releasing will close channel by self
            return;
        }
        boolean ret = connectionPool.remove(channel);
        if (channel.isOpen()) {
            channel.close();
        }
        if (ret) {
            needConnectionNumber.incrementAndGet();
        }
    }
}
