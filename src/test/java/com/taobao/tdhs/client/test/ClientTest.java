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

import org.junit.Test;

/**
 * <p>
 * 创建客户端
 * </p>
 * <p>
 * <ol>
 * <li>正常创建</li>
 * <li>不正常IP创建</li>
 * </ol>
 * </p>
 * 
 * @author yuanfeng.mc
 * 
 */
public class ClientTest {
	@Test
	public void testErrorCreateClient() {
//		TDHSClient c = null;
//		try {
//			c = new TDHSClientImpl(new InetSocketAddress("这不是个IP地址", 9999), 1,
//					1, false, 1);
//		} catch (TDHSException e) {
//			Assert.assertEquals("connect time out", e.getMessage());
//		} finally {
//			if (c != null) {
//				c.shutdown();
//			}
//		}
	}

	@Test
	public void testCreateErrorClient() {
//		TDHSClient c = null;
//		try {
//			c = new TDHSClientImpl(new InetSocketAddress("这不是个IP地址", 9999),
//					-10);
//		} catch (Exception e) {
//			Assert.assertEquals("connectionNumber must be positive!",
//					e.getMessage());
//		} finally {
//			if (c != null) {
//				c.shutdown();
//			}
//		}
	}

}
