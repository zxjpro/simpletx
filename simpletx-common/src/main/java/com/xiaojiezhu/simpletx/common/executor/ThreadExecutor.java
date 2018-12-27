package com.xiaojiezhu.simpletx.common.executor;

import java.util.concurrent.Executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 17:36
 */
public interface ThreadExecutor extends Executor {

    /**
     * execute with thread
     * @param runnable
     * @return
     */
    ExecutorFuture executeFuture(Runnable runnable);

    long getMaxThreadSize();

    /**
     * wait on queue size
     * @return
     */
    long getWaitThreadSize();

}
