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
 * @since 12-2-9 下午2:11
 */
public class TDHSTimeoutException extends TDHSException {
    private static final long serialVersionUID = -6031912630289588430L;

    public TDHSTimeoutException() {
    }

    public TDHSTimeoutException(String message) {
        super(message);
    }

    public TDHSTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TDHSTimeoutException(Throwable cause) {
        super(cause);
    }
}
