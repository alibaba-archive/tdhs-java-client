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

import java.sql.SQLException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-6 下午2:58
 */
public class Driver extends NonRegisteringDriver implements java.sql.Driver {

    // ~ Static fields/initializers
    // ---------------------------------------------

    //
    // Register ourselves with the DriverManager
    //
    static {
        try {
            java.sql.DriverManager.registerDriver(new Driver());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    // ~ Constructors
    // -----------------------------------------------------------

    /**
     * Construct a new driver and register it with DriverManager
     *
     * @throws SQLException if a database error occurs.
     */
    public Driver() throws SQLException {
        // Required for Class.forName().newInstance()
    }
}
