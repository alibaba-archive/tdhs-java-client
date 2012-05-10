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

package com.taobao.tdhs.client.request;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-1-17 下午1:04
 */
public abstract class RequestWithCharest implements Request {

    private String charestName;

    /**
     * Method getCharestName returns the charestName of this RequestWithCharest object.
     *
     * @return the charestName (type String) of this RequestWithCharest object.
     */
    public String getCharestName() {
        return charestName;
    }

    /**
     * Method setCharestName sets the charestName of this RequestWithCharest object.
     *
     * @param charestName the charestName of this RequestWithCharest object.
     */
    public void setCharestName(String charestName) {
        this.charestName = charestName;
    }
}
