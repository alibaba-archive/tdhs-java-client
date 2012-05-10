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

import com.taobao.tdhs.jdbc.test.mybatis.vo.Person;
import org.apache.ibatis.annotations.Select;
import org.junit.Ignore;

import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 上午11:01
 */
@Ignore
public interface PersonMapper {
    @Select("select Id,name,age,meMo as mm from person where id = #{id}")
    Person selectPerson(int id);

    @Select("select Id,name,age,meMo as mm from person where id > #{id}")
    List<Person> selectPersons(int id);

}
