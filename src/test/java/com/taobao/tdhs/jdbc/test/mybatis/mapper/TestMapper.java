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

package com.taobao.tdhs.jdbc.test.mybatis.mapper;

import com.taobao.tdhs.jdbc.test.mybatis.vo.TestVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.junit.Ignore;

import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午2:46
 */
@Ignore
public interface TestMapper {

    @Insert("insert into test(a,b,c,n,t ,now) values (#{aaa},#{bbb},#{ccc},#{nnn},#{ttt},now())")
    int insert(TestVO testVO);

    @Select("select id,a as aaa,b as bbb,c as ccc,n as nnn,t as ttt,now from test where id >-100")
    List<TestVO> selectAll();

    @Update("update test set a = #{aaa} , now=now() where b>1")
    int update(TestVO testVO);

    @Delete("delete from test where b = #{bbb}")
    int delete(TestVO testVO);

}
