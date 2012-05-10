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

package com.taobao.tdhs.jdbc;

import com.taobao.tdhs.client.TDHSClient;
import com.taobao.tdhs.client.TDHSClientImpl;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.jdbc.util.ConvertUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 管理TDHSClinet实例
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-7 上午11:39
 */
public final class TDHSClientInstance {

    private static Logger logger = LoggerFactory.getLogger(TDHSClientInstance.class);

    private static final Lock lock = new ReentrantLock();

    private static final Map<ClientKey, ClientVaue> instances = new ConcurrentHashMap<ClientKey, ClientVaue>(10);


    public static @NotNull ClientKey createConnection(@NotNull Properties info) throws TDHSException {
        String host = info.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY, "localhost");
        int port = ConvertUtil.safeConvertInt(info.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY, "9999"), 9999);
        int connectionNumber = ConvertUtil
                .safeConvertInt(info.getProperty(NonRegisteringDriver.CONNECTION_NUMBER_PROPERTY_KEY, "1"), 1);
        int timeOut = ConvertUtil
                .safeConvertInt(info.getProperty(NonRegisteringDriver.TIME_OUT_PROPERTY_KEY, "1000"), 1000);
        boolean needReconnect =
                Boolean.valueOf(info.getProperty(NonRegisteringDriver.NEED_RECONNECT_PROPERTY_KEY, "true"));
        int connectTimeOut = ConvertUtil
                .safeConvertInt(info.getProperty(NonRegisteringDriver.CONNECT_TIMEOUT_PROPERTY_KEY, "1000"), 1000);
        String charestName = info.getProperty(NonRegisteringDriver.CHAREST_NAME_PROPERTY_KEY);
        String readCode = info.getProperty(NonRegisteringDriver.READ_CODE_PROPERTY_KEY);

        String writeCode = info.getProperty(NonRegisteringDriver.WRITE_CODE_PROPERTY_KEY);
        ClientKey key = new ClientKey(new InetSocketAddress(host, port), connectionNumber, timeOut, needReconnect,
                connectTimeOut, charestName, readCode, writeCode);
        logger.debug("createConnection ClientKey:" + key);
        lock.lock();
        try {
            ClientVaue value = instances.get(key);
            if (value != null) {
                value.addRefCount();
                key.setClient(value.getClient());
                return key;
            } else {
                TDHSClient tdhsClient =
                        new TDHSClientImpl(key.getAddress(), connectionNumber, timeOut, needReconnect, connectTimeOut,
                                charestName, readCode, writeCode);
                value = new ClientVaue(tdhsClient);
                instances.put(key, value);
                key.setClient(tdhsClient);
                return key;
            }
        } finally {
            lock.unlock();
        }
    }

    public static void closeConnection(@NotNull ClientKey key) {
        logger.debug("closeConnection ClientKey:" + key);
        TDHSClient client = null;
        lock.lock();
        try {
            ClientVaue value = instances.get(key);
            if (value == null) {
                throw new RuntimeException("Don't have this client in instances,Maybe close twice! key is " + key);
            }
            if (value.needCloseClient()) {
                client = value.getClient();
                instances.remove(key);
            }
        } finally {
            lock.unlock();
        }
        if (client != null) {
            logger.debug("closeConnection and shutDown TDHSClient!");
            client.shutdown();
        }
    }


    private static class ClientVaue {
        private TDHSClient client;

        private AtomicLong refCount = new AtomicLong(1);

        private ClientVaue(TDHSClient client) {
            this.client = client;
        }

        public TDHSClient getClient() {
            return client;
        }

        private void addRefCount() {
            refCount.incrementAndGet();
        }

        private boolean needCloseClient() {
            return refCount.decrementAndGet() == 0;
        }
    }


    public static class ClientKey {
        private InetSocketAddress address;
        private int connectionNumber;
        private int timeOut;
        private boolean needReconnect;
        private int connectTimeOut;
        private String charestName;
        private String readCode;
        private String writeCode;

        private TDHSClient client;

        private ClientKey(InetSocketAddress address, int connectionNumber, int timeOut, boolean needReconnect,
                          int connectTimeOut, String charestName, String readCode, String writeCode) {
            this.address = address;
            this.connectionNumber = connectionNumber;
            this.timeOut = timeOut;
            this.needReconnect = needReconnect;
            this.connectTimeOut = connectTimeOut;
            this.charestName = charestName;
            this.readCode = readCode;
            this.writeCode = writeCode;
        }

        public TDHSClient getClient() {
            return client;
        }

        public void setClient(TDHSClient client) {
            this.client = client;
        }

        public InetSocketAddress getAddress() {
            return address;
        }

        public int getConnectionNumber() {
            return connectionNumber;
        }

        public int getTimeOut() {
            return timeOut;
        }

        public boolean isNeedReconnect() {
            return needReconnect;
        }

        public int getConnectTimeOut() {
            return connectTimeOut;
        }

        public String getCharestName() {
            return charestName;
        }

        public String getReadCode() {
            return readCode;
        }

        public String getWriteCode() {
            return writeCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ClientKey)) return false;

            ClientKey clientKey = (ClientKey) o;

            if (connectTimeOut != clientKey.connectTimeOut) return false;
            if (connectionNumber != clientKey.connectionNumber) return false;
            if (needReconnect != clientKey.needReconnect) return false;
            if (timeOut != clientKey.timeOut) return false;
            if (address != null ? !address.equals(clientKey.address) : clientKey.address != null) return false;
            if (charestName != null ? !charestName.equals(clientKey.charestName) : clientKey.charestName != null)
                return false;
            if (readCode != null ? !readCode.equals(clientKey.readCode) : clientKey.readCode != null) return false;
            if (writeCode != null ? !writeCode.equals(clientKey.writeCode) : clientKey.writeCode != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = address != null ? address.hashCode() : 0;
            result = 31 * result + connectionNumber;
            result = 31 * result + timeOut;
            result = 31 * result + (needReconnect ? 1 : 0);
            result = 31 * result + connectTimeOut;
            result = 31 * result + (charestName != null ? charestName.hashCode() : 0);
            result = 31 * result + (readCode != null ? readCode.hashCode() : 0);
            result = 31 * result + (writeCode != null ? writeCode.hashCode() : 0);
            return result;
        }

        @Override public String toString() {
            return "ClientKey{" +
                    "address=" + address +
                    ", connectionNumber=" + connectionNumber +
                    ", timeOut=" + timeOut +
                    ", needReconnect=" + needReconnect +
                    ", connectTimeOut=" + connectTimeOut +
                    ", charestName='" + charestName + '\'' +
                    ", readCode='" + readCode + '\'' +
                    ", writeCode='" + writeCode + '\'' +
                    '}';
        }
    }
}
