package com.xiaojiezhu.simpletx.common.executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/9 15:13
 */
public class DefaultExecutorFuture implements ExecutorFuture {
    private Worker worker;


    @Override
    public Status getStatus() {
        return this.worker.getStatus();
    }

    public long getWaitTime() {
        return worker.getWaitTime();
    }

    public long getExecuteTime() {
        return worker.getExecuteTime();
    }

    public long getTotalTime() {
        return worker.getTotalTime();
    }

    public boolean isRunError() {
        return worker.isRunError();
    }

    public Throwable getError() {
        return worker.getError();
    }

    public long getInitTime() {
        return worker.getInitTime();
    }

    public long getStartTime() {
        return worker.getStartTime();
    }

    public long getCompleteTime() {
        return worker.getCompleteTime();
    }
}
