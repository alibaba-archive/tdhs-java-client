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

package com.taobao.tdhs.jdbc.test.mybatis.vo;

import java.util.Date;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午3:28
 */
public class OrderVO extends BaseVO {

    private long userId;

    private long auctionId;

    private String auctionName;

    private int number;

    private int deleted;

    private Date created;

    private Date lastModify;

    public OrderVO(long userId, long acutionId, String acutionName, int number, int deleted, Date created) {
        this.userId = userId;
        this.auctionId = acutionId;
        this.auctionName = acutionName;
        this.number = number;
        this.deleted = deleted;
        this.created = created;
    }

    public OrderVO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(long auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderVO)) return false;

        OrderVO orderVO = (OrderVO) o;

        if (auctionId != orderVO.auctionId) return false;
        if (deleted != orderVO.deleted) return false;
        if (number != orderVO.number) return false;
        if (userId != orderVO.userId) return false;
        if (auctionName != null ? !auctionName.equals(orderVO.auctionName) : orderVO.auctionName != null) return false;
        if (created != null ? !created.equals(orderVO.created) : orderVO.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (auctionId ^ (auctionId >>> 32));
        result = 31 * result + (auctionName != null ? auctionName.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + deleted;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "OrderVO{" +
                "userId=" + userId +
                ", auctionId=" + auctionId +
                ", auctionName='" + auctionName + '\'' +
                ", number=" + number +
                ", deleted=" + deleted +
                ", created=" + created +
                ", lastModify=" + lastModify +
                '}';
    }
}
