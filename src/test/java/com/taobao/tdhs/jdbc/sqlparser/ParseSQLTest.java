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

package com.taobao.tdhs.jdbc.sqlparser;

import org.junit.Test;


public class ParseSQLTest {

    private void print(ParseSQL ps) {
        System.out.println(ps.getSql());
        System.out.println(ps.getTableName());
        System.out.println(ps.getErrmsg());
        if (ps.getSqlType() != SQLType.INSERT) {
            System.out.println(ps.getHint());
            System.out.println(ps.getSortMethod());
            System.out.println(ps.getOrderByColumn());
            System.out.println(ps.getListOperationStructs());
        }
        if (ps.getSqlType() == SQLType.INSERT) {
            System.out.println(ps.getInsertEntries());
        }
        if (ps.getSqlType() == SQLType.SELECT) {
            System.out.println(ps.getColumns());

        }
        if (ps.getSqlType() == SQLType.UPDATE) {
            System.out.println(ps.getUpdateEntries());

        }
        System.out.print("\n\n");
    }

    @Test
    public void test1() {
        String sql =
                "select/* tdhs:idx_ab(a, b)*/ a as a1,b from db.test where id = 1 and a in (1,2,\"3\") ; and b is null order by a limit 1,2";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

    @Test
    public void test2() {
        String sql = "delete/* tdhs:idx_ab(a, b)*/ from db.test where id = 1 and status=2 order by id,status desc";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

    @Test
    public void test3() {
        String sql =
                "update/* tdhs:idx_ab(a, b)*/db.test set xxx='bbb' where id = 1 and status=2 and type=5 order by a asc";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

    @Test
    public void test4() {
        String sql = "select/* tdhs:idx_ab(a, b)*/ a,b from db.test as t where t.id = 1 order by a";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

    @Test
    public void test5() {
        String sql = "insert into test(a,b) values(\';1\',2)";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

    @Test
    public void test7() {
        String sql = "select user_nick as unick,b from db.test as t where t.id = 1 order by a";
        ParseSQL parseSQL = new ParseSQL(sql);
        parseSQL.sqlDispatch();
        print(parseSQL);
    }

}
