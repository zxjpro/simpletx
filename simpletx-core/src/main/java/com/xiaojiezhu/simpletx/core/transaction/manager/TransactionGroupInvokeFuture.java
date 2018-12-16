package com.xiaojiezhu.simpletx.core.transaction.manager;

import com.xiaojiezhu.simpletx.common.executor.Future;
import com.xiaojiezhu.simpletx.common.executor.FutureListener;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 12:38
 */
public class TransactionGroupInvokeFuture implements Future<TransactionGroupInvokeStatus> {
    @Override
    public TransactionGroupInvokeStatus getNow() {
        return null;
    }

    @Override
    public TransactionGroupInvokeStatus get() throws InterruptedException {
        return null;
    }

    @Override
    public TransactionGroupInvokeStatus get(long time, TimeUnit timeUnit) throws InterruptedException {
        return null;
    }

    @Override
    public void await() throws InterruptedException {

    }

    @Override
    public void await(long time, TimeUnit timeUnit) throws InterruptedException {

    }

    @Override
    public void addListener(FutureListener<TransactionGroupInvokeStatus> futureListener) {

    }
}
