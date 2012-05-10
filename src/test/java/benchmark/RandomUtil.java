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

package benchmark;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Ignore;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-9 下午5:07
 */
@Ignore
public final class RandomUtil {

	private static final Random RANDOM = new Random();

	public static BigDecimal getId(BigDecimal total, BigDecimal step) {
		return step.multiply(new BigDecimal(RANDOM.nextInt(total.intValue())));
	}

	public static BigDecimal getSid(BigDecimal total, AtomicLong sid) {
		BigDecimal ret = new BigDecimal(sid.getAndIncrement());
		if (ret.longValue() > total.longValue()) {
			sid.set(1);
		}
		return ret;
	}

	public static int nextInt(int i) {
		return RANDOM.nextInt(i);
	}

}
