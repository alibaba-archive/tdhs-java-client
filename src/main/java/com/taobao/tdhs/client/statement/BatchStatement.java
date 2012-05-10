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

package com.taobao.tdhs.client.statement;

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;

/**
 * BatchStatement for commit some request in a transaction
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-2-21 上午10:06
 */
public interface BatchStatement extends Statement {

    /**
     * commit the batch statement
     *
     * @return TDHSResponse[]
     *
     * @throws TDHSException when has some error.
     */
    TDHSResponse[] commit() throws TDHSException;

    /**
     * Method setTimeOut sets the timeOut of this BatchStatement object.
     *
     * @param timeOut the timeOut of this BatchStatement object.
     */
    void setTimeOut(int timeOut);
}
