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

package com.taobao.tdhs.jdbc.test;

import com.taobao.tdhs.jdbc.exception.TDHSSQLException;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-26 上午10:46
 */
public class ErrorTest extends TestBase {

    public static void execute(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute(sql);
        } finally {
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }

    @Test(expected = TDHSSQLException.class)
    public void testErrorSQL() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(), "kill 1");
    }


    @Test(expected = TDHSSQLException.class)
    public void testMissIndexField() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(),
                "select/*tdhs:[idx_user_auction(user_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where user_id =1 and auction_name!=\"Book\" order by auction_id desc;");
    }

    @Test(expected = TDHSSQLException.class)
    public void testHaveOr() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(),
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 and user_id = 1 or auction_id = 1");
    }

    @Test(expected = TDHSSQLException.class)
    public void testHaveOr2() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(),
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 and (user_id = 1 or auction_id = 1)");
    }

    @Test(expected = TDHSSQLException.class)
    public void testErrorField() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(),
                "select `id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 ");
    }

    @Test(expected = TDHSSQLException.class)
    public void testErrorSQL2() throws ClassNotFoundException, SQLException {
        execute(getTDHSConnection(),
                "select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 ;select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>1 ;");
    }


}
