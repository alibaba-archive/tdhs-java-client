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

    private static TDHSClientInstance INSTANCE = null;

    /**
     * Method getInstance returns the INSTANCE of this TDHSClientInstance object.
     *
     * @return the INSTANCE (type TDHSClientInstance) of this TDHSClientInstance object.
     */
    public static synchronized TDHSClientInstance getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new TDHSClientInstance());
    }

    private final Lock lock = new ReentrantLock();

    private final Map<ClientKey, ClientValue> instances = new ConcurrentHashMap<ClientKey, ClientValue>(10);

    /**
     * Constructor TDHSClientInstance creates a new TDHSClientInstance INSTANCE.
     */
    private TDHSClientInstance() {
    }

    /**
     * Method createConnection ...
     *
     * @param info of type Properties
     *
     * @return ClientKey
     *
     * @throws TDHSException when
     */
    public
    @NotNull
    ClientKey createConnection(@NotNull Properties info) throws TDHSException {
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
        String charsetName = info.getProperty(NonRegisteringDriver.CHAREST_NAME_PROPERTY_KEY);
        String readCode = info.getProperty(NonRegisteringDriver.READ_CODE_PROPERTY_KEY);

        String writeCode = info.getProperty(NonRegisteringDriver.WRITE_CODE_PROPERTY_KEY);
        ClientKey key = new ClientKey(new InetSocketAddress(host, port), connectionNumber, timeOut, needReconnect,
                connectTimeOut, charsetName, readCode, writeCode);
        logger.debug("createConnection ClientKey:" + key);
        lock.lock();
        try {
            ClientValue value = instances.get(key);
            if (value != null) {
                value.addRefCount();
                key.setClient(value.getClient());
                return key;
            } else {
                TDHSClient tdhsClient =
                        new TDHSClientImpl(key.getAddress(), connectionNumber, timeOut, needReconnect, connectTimeOut,
                                charsetName, readCode, writeCode);
                value = new ClientValue(tdhsClient);
                instances.put(key, value);
                key.setClient(tdhsClient);
                return key;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method closeConnection ...
     *
     * @param key of type ClientKey
     */
    public void closeConnection(@NotNull ClientKey key) {
        logger.debug("closeConnection ClientKey:" + key);
        TDHSClient client = null;
        lock.lock();
        try {
            ClientValue value = instances.get(key);
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


    private static class ClientValue {
        private TDHSClient client;

        private AtomicLong refCount = new AtomicLong(1);

        private ClientValue(TDHSClient client) {
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
        private String charsetName;
        private String readCode;
        private String writeCode;

        private TDHSClient client;

        private ClientKey(InetSocketAddress address, int connectionNumber, int timeOut, boolean needReconnect,
                          int connectTimeOut, String charsetName, String readCode, String writeCode) {
            this.address = address;
            this.connectionNumber = connectionNumber;
            this.timeOut = timeOut;
            this.needReconnect = needReconnect;
            this.connectTimeOut = connectTimeOut;
            this.charsetName = charsetName;
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

        public String getCharsetName() {
            return charsetName;
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
            if (charsetName != null ? !charsetName.equals(clientKey.charsetName) : clientKey.charsetName != null)
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
            result = 31 * result + (charsetName != null ? charsetName.hashCode() : 0);
            result = 31 * result + (readCode != null ? readCode.hashCode() : 0);
            result = 31 * result + (writeCode != null ? writeCode.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ClientKey{" +
                    "address=" + address +
                    ", connectionNumber=" + connectionNumber +
                    ", timeOut=" + timeOut +
                    ", needReconnect=" + needReconnect +
                    ", connectTimeOut=" + connectTimeOut +
                    ", charsetName='" + charsetName + '\'' +
                    ", readCode='" + readCode + '\'' +
                    ", writeCode='" + writeCode + '\'' +
                    '}';
        }
    }
}
