package com.xiaojiezhu.simpletx.protocol.future;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaojie.zhu
 * time 2018-12-30 21:25
 */
public interface FutureCondition {

    Object getId();

    Future<?> getFuture();

    FutureListener<?> getFutureListener();

    void setFutureListener(FutureListener<?> futureListener);

    Object getValue();

    void setValue(Object value);

    void await() throws InterruptedException;

    boolean await(long time, TimeUnit unit) throws InterruptedException;

    void signal();


    /**
     * get the last num
     * @return
     */
    int getNum();

    /**
     * get the count number
     * @return
     */
    int getCount();
}
