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

import com.taobao.tdhs.jdbc.test.mybatis.vo.OrderVO;
import org.apache.ibatis.annotations.*;
import org.junit.Ignore;

import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午3:55
 */
@Ignore
public interface OrderMapper {
    @Insert("insert into orders(user_id,auction_id,auction_name,number,deleted,created,last_modify) " +
            "values (#{userId},#{auctionId},#{auctionName},#{number},#{deleted},#{created},now())")
    int insert(OrderVO orderVO);

    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>0")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectAll();


    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id>#{id}")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectGreaterthanId(int id);

    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where id=#{id}")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    OrderVO selectEqualId(int id);


    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where  id>1 and user_id = 1")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest1();

    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where  id>1 and user_id = 1 and auction_id = 1")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest2();


    @Select("select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where  user_id =1 order by auction_id")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest3();

    @Select("select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where  user_id =1 order by auction_id desc")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest4();

    @Select("select/*tdhs:[idx_user_auction(user_id,auction_id)]*/ id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where   user_id =1 and auction_name!=\"Book\" order by auction_id desc")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest5();

    @Select("select id,user_id,auction_id,auction_name,number,deleted,created,last_modify from orders where  id in (1,3,5)")
    @Results({@Result(column = "user_id", property = "userId"), @Result(column = "auction_id", property = "auctionId"),
            @Result(column = "auction_name", property = "auctionName"),
            @Result(column = "last_modify", property = "lastModify")})
    List<OrderVO> selectTest6();

    @Delete("delete from orders where user_id =1 and auction_name!=\"Book\"")
    int delete();

    @Update("update orders set number = number +1 , last_modify=now() where user_id =2 and auction_name!=\"Book\"")
    int update();

    @Select("select count(*) from orders where id>-1")
    int countAll();

    @Select("select count(*) from orders where id>#{id}")
    int count(int id);

}
