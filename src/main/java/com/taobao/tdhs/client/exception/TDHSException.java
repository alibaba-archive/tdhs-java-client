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

package com.taobao.tdhs.client.exception;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-12-2 上午11:43
 */
public class TDHSException extends Exception {
    private static final long serialVersionUID = 450727724481426515L;

    public TDHSException() {
    }

    public TDHSException(String message) {
        super(message);
    }

    public TDHSException(String message, Throwable cause) {
        super(message, cause);
    }

    public TDHSException(Throwable cause) {
        super(cause);
    }

}
