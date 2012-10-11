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
public abstract class RequestWithCharset implements Request {

    private String charsetName;

    /**
     * Method getCharsetName returns the charsetName of this RequestWithCharest object.
     *
     * @return the charsetName (type String) of this RequestWithCharest object.
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Method setCharsetName sets the charsetName of this RequestWithCharest object.
     *
     * @param charsetName the charsetName of this RequestWithCharest object.
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
}
