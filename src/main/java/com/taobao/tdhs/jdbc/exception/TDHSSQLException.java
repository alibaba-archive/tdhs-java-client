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

package com.taobao.tdhs.jdbc.exception;

import java.sql.SQLException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-9 上午11:11
 */
public class TDHSSQLException extends SQLException {

    public TDHSSQLException(String reason, String sql) {
        super(reason + " SQL:[" + sql + "]");
    }
}
