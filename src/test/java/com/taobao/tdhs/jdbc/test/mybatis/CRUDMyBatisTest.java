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

import com.taobao.tdhs.jdbc.test.mybatis.mapper.TestMapper;
import com.taobao.tdhs.jdbc.test.mybatis.vo.TestVO;
import org.apache.ibatis.session.SqlSession;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午2:58
 */
public class CRUDMyBatisTest extends TestBase {

    private static DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Date DATE;

    static {
        try {
            DATE = new Date(DATA_FORMAT.parse("2012-03-20 16:58:00").getTime());
        } catch (ParseException e) {
        }
    }

    private static TestVO[] DATA = {new TestVO("aa", 1, 1.1f, null, DATE), new TestVO("bb", 2, 2.2f, null, DATE),
            new TestVO("cc", 3, 3.3f, null, DATE)};

    @BeforeClass
    public static void cleanTable() throws ClassNotFoundException, SQLException {
        truncate("test");
    }

    @Test
    public void testInset() {
        SqlSession tdhsSession = getTDHSSession();
        for (TestVO testVO : DATA) {
            int insert = tdhsSession.getMapper(TestMapper.class).insert(testVO);
            assertEquals(1, insert);
        }
        tdhsSession.close();
    }

    @Test
    public void testInsertDoneWithMysql1() {
        SqlSession mySQLSession = getMySQLSession();
        List<TestVO> testVOs = mySQLSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        mySQLSession.close();
    }

    @Test
    public void testInsertDoneWithTDHS1() {
        SqlSession tdhsSession = getTDHSSession();
        List<TestVO> testVOs = tdhsSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        tdhsSession.close();
    }

    @Test
    public void testUpdate() {
        SqlSession tdhsSession = getTDHSSession();
        TestVO updateVo = new TestVO();
        updateVo.setAaa("kevin");
        int i = tdhsSession.getMapper(TestMapper.class).update(updateVo);
        assertEquals(2, i);
        tdhsSession.close();
        DATA[1].setAaa("kevin");
        DATA[2].setAaa("kevin");
    }

    @Test
    public void testInsertDoneWithMysql2() {
        SqlSession mySQLSession = getMySQLSession();
        List<TestVO> testVOs = mySQLSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        mySQLSession.close();
    }

    @Test
    public void testInsertDoneWithTDHS2() {
        SqlSession tdhsSession = getTDHSSession();
        List<TestVO> testVOs = tdhsSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        tdhsSession.close();
    }

    @Test
    public void testDelete() {
        SqlSession tdhsSession = getTDHSSession();
        TestVO deleteVO = new TestVO();
        deleteVO.setBbb(3);
        int i = tdhsSession.getMapper(TestMapper.class).delete(deleteVO);
        assertEquals(1, i);
        tdhsSession.close();
        DATA = Arrays.copyOf(DATA, 2);

    }

    @Test
    public void testInsertDoneWithMysql3() {
        SqlSession mySQLSession = getMySQLSession();
        List<TestVO> testVOs = mySQLSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        mySQLSession.close();
    }

    @Test
    public void testInsertDoneWithTDHS3() {
        SqlSession tdhsSession = getTDHSSession();
        List<TestVO> testVOs = tdhsSession.getMapper(TestMapper.class).selectAll();
        compareRecord(DATA, testVOs);
        tdhsSession.close();
    }


}
