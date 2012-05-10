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

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 下午2:41
 */
@Ignore
public class BaseVO {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
