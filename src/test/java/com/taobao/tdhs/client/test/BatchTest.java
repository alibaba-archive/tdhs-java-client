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

package com.taobao.tdhs.client.test;

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.response.TDHSResponse;
import com.taobao.tdhs.client.response.TDHSResponseEnum;
import com.taobao.tdhs.client.statement.BatchStatement;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-4-12 上午9:06
 */
public class BatchTest extends TestBase {
    @Before
    @After
    public void clean() throws TDHSException {
        client.query().use(db).from(table).where().greaterEqual("-1").delete();
    }

    @Test
    public void testBatchDone() throws TDHSException {
        BatchStatement batchStatement = client.createBatchStatement();
        batchStatement.insert().use(db).from(table)
                .value("id", "1")
                .value("name", "a")
                .value("level", "1").insert();
        batchStatement.insert().use(db).from(table)
                .value("id", "2")
                .value("name", "b")
                .value("level", "2").insert();
        batchStatement.insert().use(db).from(table)
                .value("id", "3")
                .value("name", "c")
                .value("level", "3").insert();
        TDHSResponse[] responses = batchStatement.commit();
        Assert.assertEquals(3, responses.length);
        for (TDHSResponse r : responses) {
            Assert.assertEquals(TDHSResponseEnum.ClientStatus.OK, r.getStatus());
        }
    }

    @Test
    public void testBatchFailed() throws TDHSException {
        BatchStatement batchStatement = client.createBatchStatement();
        batchStatement.insert().use(db).from(table)
                .value("id", "1")
                .value("name", "a")
                .value("level", "1").insert();
        batchStatement.insert().use(db).from(table)
                .value("id", "2")
                .value("name", "b")
                .value("level", "2").insert();
        batchStatement.insert().use(db).from(table)
                .value("id", "1")
                .value("name", "c")
                .value("level", "1").insert();
        TDHSResponse[] responses = batchStatement.commit();
        Assert.assertEquals(3, responses.length);
        Assert.assertEquals(responses[0].getStatus(), TDHSResponseEnum.ClientStatus.SERVER_ERROR);
        Assert.assertEquals(responses[0].getErrorCode(), TDHSResponseEnum.ErrorCode.CLIENT_ERROR_CODE_FAILED_TO_COMMIT);
        Assert.assertEquals(responses[1].getStatus(), TDHSResponseEnum.ClientStatus.SERVER_ERROR);
        Assert.assertEquals(responses[1].getErrorCode(), TDHSResponseEnum.ErrorCode.CLIENT_ERROR_CODE_FAILED_TO_COMMIT);
        Assert.assertEquals(responses[2].getStatus(), TDHSResponseEnum.ClientStatus.DB_ERROR);
        Assert.assertEquals(responses[2].getDbErrorCode(), 121);
    }

}
