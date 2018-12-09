package com.xiaojiezhu.simpletx.common.executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 17:36
 */
public interface ThreadExecutor {

    /**
     * execute with thread
     * @param runnable
     * @return
     */
    ExecutorFuture execute(Runnable runnable);

    long getMaxThreadSize();

    /**
     * wait on queue size
     * @return
     */
    long getWaitThreadSize();

}
