package com.xiaojiezhu.simpletx.common.executor;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 12:07
 */
public interface Future<T> {

    /**
     * get data now , it may be null
     * @return
     */
    T getNow();

    /**
     * wait and get
     * @return
     */
    T get() throws InterruptedException;


    /**
     * wait if not interrupt and get data
     * @param time
     * @param timeUnit
     * @return
     * @throws InterruptedException
     */
    T get(long time , TimeUnit timeUnit) throws InterruptedException;

    /**
     * wait future complete
      */
    void await() throws InterruptedException;


    /**
     * await and interrupt time
     * @param time
     * @param timeUnit
     */
    void await(long time, TimeUnit timeUnit) throws InterruptedException;


    /**
     * add the future complete listener
     * @param futureListener
     */
    void addListener(FutureListener<T> futureListener);

}
