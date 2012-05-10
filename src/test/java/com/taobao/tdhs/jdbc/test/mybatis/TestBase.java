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

import com.taobao.tdhs.jdbc.test.mybatis.mapper.OrderMapper;
import com.taobao.tdhs.jdbc.test.mybatis.mapper.PersonMapper;
import com.taobao.tdhs.jdbc.test.mybatis.mapper.TestMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Ignore;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 上午10:32
 */
@Ignore
public abstract class TestBase extends com.taobao.tdhs.jdbc.test.TestBase {

    protected SqlSession getMySQLSession() {
        return createSession(new MySQLDataSource());
    }

    protected SqlSession getTDHSSession() {
        return createSession(new TDHSDataSource());
    }


    private SqlSession createSession(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(PersonMapper.class);
        configuration.addMapper(TestMapper.class);
        configuration.addMapper(OrderMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory.openSession();
    }

    protected <T> void compareRecord(List<T> mysqlRecord, List<T> tdhsRrcord) {
        assertEquals(mysqlRecord.size(), tdhsRrcord.size());
        for (int i = 0; i < mysqlRecord.size(); i++) {
            assertEquals(mysqlRecord.get(i), tdhsRrcord.get(i));
        }
    }

    protected <T> void compareRecord(T[] mysqlRecord, List<T> tdhsRrcord) {
        assertEquals(mysqlRecord.length, tdhsRrcord.size());
        for (int i = 0; i < mysqlRecord.length; i++) {
            assertEquals(mysqlRecord[i], tdhsRrcord.get(i));
        }
    }


}
