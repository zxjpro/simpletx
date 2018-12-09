package com.xiaojiezhu.simpletx.common.executor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaojie.zhu
 * time 2018/12/9 15:11
 */
public class SimpleThreadExecutor implements ThreadExecutor {

    private volatile AtomicLong count = new AtomicLong();


    @Override
    public ExecutorFuture execute(Runnable runnable) {
        Worker worker = new Worker(runnable);

        Thread thread = new Thread(worker);
        thread.setName(getClass().getSimpleName() + "_" + count.getAndIncrement());
        thread.start();
        return null;
    }

    @Override
    public long getMaxThreadSize() {
        return 0;
    }

    @Override
    public long getWaitThreadSize() {
        return 0;
    }

}
