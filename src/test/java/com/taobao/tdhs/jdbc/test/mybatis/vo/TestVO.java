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

import org.junit.Ignore;

import java.util.Date;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午2:44
 */
@Ignore
public class TestVO extends BaseVO {
    private String aaa;

    private int bbb;

    private float ccc;

    private String nnn;

    private Date ttt;

    private Date now;

    public TestVO() {
    }

    public TestVO(String aaa, int bbb, float ccc, String nnn, Date ttt) {
        this.aaa = aaa;
        this.bbb = bbb;
        this.ccc = ccc;
        this.nnn = nnn;
        this.ttt = ttt;
    }

    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    public int getBbb() {
        return bbb;
    }

    public void setBbb(int bbb) {
        this.bbb = bbb;
    }

    public float getCcc() {
        return ccc;
    }

    public void setCcc(float ccc) {
        this.ccc = ccc;
    }

    public String getNnn() {
        return nnn;
    }

    public void setNnn(String nnn) {
        this.nnn = nnn;
    }

    public Date getTtt() {
        return ttt;
    }

    public void setTtt(Date ttt) {
        this.ttt = ttt;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestVO)) return false;

        TestVO testVO = (TestVO) o;

        if (bbb != testVO.bbb) return false;
        if (Float.compare(testVO.ccc, ccc) != 0) return false;
        if (aaa != null ? !aaa.equals(testVO.aaa) : testVO.aaa != null) return false;
        if (nnn != null ? !nnn.equals(testVO.nnn) : testVO.nnn != null) return false;
        if (ttt != null ? !ttt.equals(testVO.ttt) : testVO.ttt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aaa != null ? aaa.hashCode() : 0;
        result = 31 * result + bbb;
        result = 31 * result + (ccc != +0.0f ? Float.floatToIntBits(ccc) : 0);
        result = 31 * result + (nnn != null ? nnn.hashCode() : 0);
        result = 31 * result + (ttt != null ? ttt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TestVO{" +
                "aaa='" + aaa + '\'' +
                ", bbb=" + bbb +
                ", ccc=" + ccc +
                ", nnn='" + nnn + '\'' +
                ", ttt=" + ttt +
                ", now=" + now +
                '}';
    }
}
