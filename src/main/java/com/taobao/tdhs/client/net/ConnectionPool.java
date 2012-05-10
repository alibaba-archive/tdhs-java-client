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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-26 下午3:55
 */
public class ConnectionPool<T> {
    private List<T> pool;

    private int index = 0;

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ConnectionPool(int num) {
        pool = new ArrayList<T>(num);
    }

    public void add(T t, Handler<T> handler) {
        rwLock.writeLock().lock();
        try {
            if (handler != null) {
                handler.execute(t);
            }
            pool.add(t);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public boolean remove(T t) {
        rwLock.writeLock().lock();
        try {
            return pool.remove(t);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Method get ...
     *
     * @return Channel
     */
    public T get() {
        rwLock.readLock().lock();
        try {
            if (pool.isEmpty()) {
                return null;
            }
            int idx = Math.abs(index++ % pool.size());
            return pool.get(idx);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void close(Handler<T> handler) {
        rwLock.writeLock().lock();
        try {
            Iterator<T> iterator = pool.iterator();
            while (iterator.hasNext()) {
                T t = iterator.next();
                if (handler != null) {
                    handler.execute(t);
                }
                iterator.remove();
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int size() {
        rwLock.readLock().lock();
        try {
            return pool.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        rwLock.readLock().lock();
        try {
            return pool.isEmpty();
        } finally {
            rwLock.readLock().unlock();
        }
    }


    public interface Handler<T> {
        void execute(T t);
    }


}
