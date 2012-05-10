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

package com.taobao.tdhs.jdbc.test.mybatis;

import com.taobao.tdhs.jdbc.test.mybatis.mapper.PersonMapper;
import com.taobao.tdhs.jdbc.test.mybatis.vo.Person;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午2:18
 */
public class SimpleSelectMyBatisTest extends TestBase {
    private static Person[] data =
            {new Person(1, "Kevin", 30, "Coder"), new Person(2, "Vivian", 30, "Wife"),
                    new Person(3, "Kitty", 2, "Daughter")};

    @Test
    public void testSelect1() {
        SqlSession tdhsSession = getTDHSSession();
        Person tdhsPerson = tdhsSession.getMapper(PersonMapper.class).selectPerson(2);
        assertEquals(data[1], tdhsPerson);
        SqlSession mysqlSession = getTDHSSession();
        Person mysqlPerson = mysqlSession.getMapper(PersonMapper.class).selectPerson(2);
        assertEquals(mysqlPerson, tdhsPerson);
        tdhsSession.close();
        mysqlSession.close();
    }

    @Test
    public void testSelect2() {
        SqlSession tdhsSession = getTDHSSession();
        Person tdhsPerson = tdhsSession.getMapper(PersonMapper.class).selectPerson(3);
        assertEquals(data[2], tdhsPerson);
        SqlSession mysqlSession = getTDHSSession();
        Person mysqlPerson = mysqlSession.getMapper(PersonMapper.class).selectPerson(3);
        assertEquals(mysqlPerson, tdhsPerson);
        tdhsSession.close();
        mysqlSession.close();
    }

    @Test
    public void testSelect3() {
        SqlSession tdhsSession = getTDHSSession();
        List<Person> tdhsPerson = tdhsSession.getMapper(PersonMapper.class).selectPersons(1);
        SqlSession mysqlSession = getTDHSSession();
        List<Person> mysqlPerson = mysqlSession.getMapper(PersonMapper.class).selectPersons(1);
        compareRecord(mysqlPerson, tdhsPerson);
        tdhsSession.close();
        mysqlSession.close();
    }

    @Test
    public void testSelect4() {
        SqlSession tdhsSession = getTDHSSession();
        List<Person> tdhsPerson = tdhsSession.getMapper(PersonMapper.class).selectPersons(-1);
        SqlSession mysqlSession = getTDHSSession();
        List<Person> mysqlPerson = mysqlSession.getMapper(PersonMapper.class).selectPersons(-1);
        compareRecord(mysqlPerson, tdhsPerson);
        tdhsSession.close();
        mysqlSession.close();
    }
}
