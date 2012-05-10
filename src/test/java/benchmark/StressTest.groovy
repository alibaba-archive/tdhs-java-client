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

package benchmark

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-9 下午1:12
 *
 */
class StressTest {

  int interval = 1;   //s

  int count = 0;

  int time = 0;

  boolean print_rt = true

  private final AtomicLong MONITOR_STAT_QPS_NUM = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_TOTAL = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_0MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_0_1MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_1_5MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_5_10MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_10_50MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_50_100MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_100_500MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_500_1000MS = new AtomicLong();

  private final AtomicLong MONITOR_RESPNSE_1000_MS = new AtomicLong();

  private final ThreadLocal<Long> START_TIME = new ThreadLocal<Long>();

  private Map<Closure, Integer> closures = new HashMap<Closure, Integer>();

  private int thread_num = 0;

  private AtomicInteger stop_thread_num = new AtomicInteger();

  private ExecutorService pool;

  private boolean is_stop = false

  /**
   * Method start ...
   */
  private void start() {
    START_TIME.set(System.currentTimeMillis());
  }

  /**
   * Method end ...
   */
  private void end() {
    long start = START_TIME.get();
    long response = System.currentTimeMillis() - start;
    MONITOR_STAT_QPS_NUM.incrementAndGet();
    MONITOR_RESPNSE_TOTAL.incrementAndGet();
    if (response <= 0) {
      MONITOR_RESPNSE_0MS.incrementAndGet();
    } else if (response > 0 && response <= 1) {
      MONITOR_RESPNSE_0_1MS.incrementAndGet();
    } else if (response > 1 && response <= 5) {
      MONITOR_RESPNSE_1_5MS.incrementAndGet();
    } else if (response > 5 && response <= 10) {
      MONITOR_RESPNSE_5_10MS.incrementAndGet();
    } else if (response > 10 && response <= 50) {
      MONITOR_RESPNSE_10_50MS.incrementAndGet();
    } else if (response > 50 && response <= 100) {
      MONITOR_RESPNSE_50_100MS.incrementAndGet();
    } else if (response > 100 && response <= 500) {
      MONITOR_RESPNSE_100_500MS.incrementAndGet();
    } else if (response > 500 && response <= 1000) {
      MONITOR_RESPNSE_500_1000MS.incrementAndGet();
    } else if (response > 1000) {
      MONITOR_RESPNSE_1000_MS.incrementAndGet();
    }
  }

  public void add(Closure closure, int threadNum) {
    closures.put(closure, threadNum)
    this.thread_num += threadNum
  }

  public void run() {
    pool = Executors.newFixedThreadPool(thread_num + 1)
    def defer = { c -> pool.submit(c as Runnable) }
    closures.each { c ->
      if (c.value > 0) {
        1.upto(c.value) {  idx ->
          defer {runit(c.key, idx - 1)}
        }
      }
    }
    defer {monitor()}
    pool.shutdown()
    while (!pool.awaitTermination(1000, TimeUnit.SECONDS)) {
    }
    println "#######################################"
  }


  private void runit(Closure closure, int i) {
    int c = count / thread_num;
    try {
      while (!is_stop && (count == 0 || c > 0)) {
        try {
          start()
          closure.call(i)
        } catch (Exception e) {
          e.printStackTrace()
        } finally {
          end()
          if (c > 0) {
            c--
          }
        }
      }
    } finally {
      stop_thread_num.incrementAndGet()
    }
  }

  /**
   * Method run ...
   */
  private void monitor() {
    println("start monitor")
    long start_time = System.currentTimeMillis()
    long end_time = start_time + (time * 1000);
    while (stop_thread_num.get() < thread_num) {
      if (time > 0 && System.currentTimeMillis() >= end_time) {
        println "time is over"
        is_stop = true
      }
      Thread.sleep(interval * 1000);

      long num = MONITOR_STAT_QPS_NUM.getAndSet(0);
      println("QPS:" + (num / interval) + " TOTAL:" + MONITOR_RESPNSE_TOTAL.get());

      if (print_rt) {
        print_rt();
      }
      println("----------------------------------");
    }

    def total_time = (System.currentTimeMillis() - start_time) / 1000

    println "avg QPS:" + MONITOR_RESPNSE_TOTAL.get() / total_time + " ,total:" + MONITOR_RESPNSE_TOTAL.get()
    print_rt()
    println("end monitor")
  }

  private def print_rt() {
    long total = MONITOR_RESPNSE_TOTAL.get();
    println(" RT <= 0:      " + (MONITOR_RESPNSE_0MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_0MS.get() + "/" + total);
    println(" RT (0,1]:     " + (MONITOR_RESPNSE_0_1MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_0_1MS.get() + "/" + total);
    println(" RT (1,5]:     " + (MONITOR_RESPNSE_1_5MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_1_5MS.get() + "/" + total);
    println(" RT (5,10]:    " + (MONITOR_RESPNSE_5_10MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_5_10MS.get() + "/" + total);
    println(" RT (10,50]:   " + (MONITOR_RESPNSE_10_50MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_10_50MS.get() + "/" + total);
    println(" RT (50,100]:  " + (MONITOR_RESPNSE_50_100MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_50_100MS.get() + "/" + total);
    println(" RT (100,500]: " + (MONITOR_RESPNSE_100_500MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_100_500MS.get() + "/" + total);
    println(" RT (500,1000]:" + (MONITOR_RESPNSE_500_1000MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_500_1000MS.get() + "/" + total);
    println(" RT > 1000:    " + (MONITOR_RESPNSE_1000_MS.get() * 100 / total) + "% " +
            MONITOR_RESPNSE_1000_MS.get() + "/" + total)
  }
}
