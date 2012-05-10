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
 * @since 11-12-2 下午1:28
 */
public class TDHSEncodeException extends TDHSException {

    private static final long serialVersionUID = 4248787784350507825L;

    public TDHSEncodeException() {
    }

    public TDHSEncodeException(String message) {
        super(message);
    }

    public TDHSEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TDHSEncodeException(Throwable cause) {
        super(cause);
    }

}
