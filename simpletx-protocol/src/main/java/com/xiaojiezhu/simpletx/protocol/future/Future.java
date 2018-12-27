package com.xiaojiezhu.simpletx.protocol.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    T get(long time , TimeUnit timeUnit) throws InterruptedException, TimeoutException;

    /**
     * wait future complete
      */
    void await() throws InterruptedException;


    /**
     * await and interrupt time
     * @param time
     * @param timeUnit
     */
    void await(long time, TimeUnit timeUnit) throws InterruptedException, TimeoutException;


    /**
     * add the future complete listener
     * @param futureListener
     */
    void addListener(FutureListener<T> futureListener) throws InterruptedException;

}
