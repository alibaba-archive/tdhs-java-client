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

import com.taobao.tdhs.client.response.TDHSResponse;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-21 下午5:07
 */
public class TDHSBatchException extends TDHSException {
    private TDHSResponse response;

    public TDHSBatchException(TDHSResponse response) {
        this.response = response;
    }

    public TDHSResponse getResponse() {
        return response;
    }

    @Override public String toString() {
        return "TDHSBatchException{" +
                "response=" + response +
                '}';
    }
}
