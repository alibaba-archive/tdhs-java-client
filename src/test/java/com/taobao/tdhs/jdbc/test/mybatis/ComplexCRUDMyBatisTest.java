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

import au.com.bytecode.opencsv.CSVReader;
import com.taobao.tdhs.jdbc.test.mybatis.mapper.OrderMapper;
import com.taobao.tdhs.jdbc.test.mybatis.vo.OrderVO;
import org.apache.ibatis.session.SqlSession;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午3:22
 */
public class ComplexCRUDMyBatisTest extends TestBase {
    private static DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static List<OrderVO> DATA;

    @BeforeClass
    public static void init() throws ClassNotFoundException, SQLException, IOException, ParseException {
        truncate("orders");
        CSVReader reader = new CSVReader(
                new InputStreamReader(ClassLoader.getSystemResourceAsStream("order.csv")));
        List<String[]> data = reader.readAll();
        data.remove(0);     //remove header
        DATA = new ArrayList<OrderVO>();
        for (String[] s : data) {
            DATA.add(new OrderVO(Long.valueOf(s[0]), Long.valueOf(s[1]), s[2], Integer.valueOf(s[3]),
                    Integer.valueOf(s[4]), DATA_FORMAT.parse(s[5])));
        }
    }


    @Test
    public void testInset() {
        SqlSession tdhsSession = getTDHSSession();
        for (OrderVO orderVO : DATA) {
            int insert = tdhsSession.getMapper(OrderMapper.class).insert(orderVO);
            assertEquals(1, insert);
        }
        tdhsSession.close();
    }

    @Test
    public void testInsertDone() {
        SqlSession tdhsSession = getTDHSSession();
        List<OrderVO> orderVOs = tdhsSession.getMapper(OrderMapper.class).selectAll();
        compareRecord(DATA, orderVOs);
        tdhsSession.close();
    }

    @Test
    public void testSelect1() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectGreaterthanId(0);
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectGreaterthanId(0);
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect2() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectGreaterthanId(1);
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectGreaterthanId(1);
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect3() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        OrderVO tdhsOrderVO = tdhsSession.getMapper(OrderMapper.class).selectEqualId(1);
        OrderVO mysqlOrderVO = mySQLSession.getMapper(OrderMapper.class).selectEqualId(1);
        assertEquals(mysqlOrderVO, tdhsOrderVO);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect4() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest1();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest1();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect5() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest2();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest2();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect6() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest3();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest3();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect7() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest4();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest4();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect8() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest5();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest5();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testSelect9() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectTest6();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectTest6();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testZCount() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        int tdhsCount = tdhsSession.getMapper(OrderMapper.class).countAll();
        int mysqlCount = mySQLSession.getMapper(OrderMapper.class).countAll();
        System.out.println(mysqlCount + "||" + tdhsCount);
        assertTrue(mysqlCount > 0);
        assertEquals(mysqlCount, tdhsCount);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testZCount1() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        int tdhsCount = tdhsSession.getMapper(OrderMapper.class).count(1);
        int mysqlCount = mySQLSession.getMapper(OrderMapper.class).count(1);
        System.out.println(mysqlCount + "||" + tdhsCount);
        assertTrue(mysqlCount > 0);
        assertEquals(mysqlCount, tdhsCount);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testZCount2() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        int tdhsCount = tdhsSession.getMapper(OrderMapper.class).count(4);
        int mysqlCount = mySQLSession.getMapper(OrderMapper.class).count(4);
        System.out.println(mysqlCount + "||" + tdhsCount);
        assertTrue(mysqlCount > 0);
        assertEquals(mysqlCount, tdhsCount);
        tdhsSession.close();
        mySQLSession.close();
    }


    @Test
    public void testDelete() {
        SqlSession tdhsSession = getTDHSSession();
        int insert = tdhsSession.getMapper(OrderMapper.class).delete();
        assertEquals(3, insert);
        tdhsSession.close();
    }

    @Test
    public void testDeleteDone() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectAll();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectAll();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }

    @Test
    public void testUpdate() {
        SqlSession tdhsSession = getTDHSSession();
        int insert = tdhsSession.getMapper(OrderMapper.class).update();
        assertEquals(2, insert);
        tdhsSession.close();
    }


    @Test
    public void testUpdateDone() throws ClassNotFoundException, SQLException {
        SqlSession tdhsSession = getTDHSSession();
        SqlSession mySQLSession = getMySQLSession();
        List<OrderVO> tdhsOrderVOs = tdhsSession.getMapper(OrderMapper.class).selectAll();
        List<OrderVO> mysqlOrderVOs = mySQLSession.getMapper(OrderMapper.class).selectAll();
        compareRecord(mysqlOrderVOs, tdhsOrderVOs);
        tdhsSession.close();
        mySQLSession.close();
    }
}
