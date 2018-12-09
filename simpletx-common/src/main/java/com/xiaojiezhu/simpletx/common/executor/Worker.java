package com.xiaojiezhu.simpletx.common.executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/9 15:21
 */
class Worker implements Runnable {
    private final Runnable runnable;
    private final long initTime;
    private long startTime;
    private long completeTime;

    private Throwable ex;

    public Worker(Runnable runnable) {
        this.runnable = runnable;
        this.initTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        try {
            this.runnable.run();
        } catch (Throwable e){
            this.ex = e;
        }

        this.completeTime = System.currentTimeMillis();
    }

    public ExecutorFuture.Status getStatus(){
        if(this.startTime == 0){
            return ExecutorFuture.Status.WAITING;
        }else if(this.completeTime == 0){
            return ExecutorFuture.Status.EXECUTERING;
        }else if(this.startTime != 0 && this.completeTime != 0){
            return ExecutorFuture.Status.COMPLETE;
        }else{
            throw new RuntimeException("not define status");
        }
    }


    /**
     * 获取等待时间，也就是从创建任务，到任务开始的时间
     */
    public long getWaitTime(){
        if(this.startTime == 0){
            throw new WorkerException("this worker is not start");
        }
        return this.startTime - this.initTime;
    }

    /**
     * 获取任务的执行时间，不包括等待时间
     * @return
     */
    public long getExecuteTime(){
        if(this.startTime == 0 || this.completeTime == 0){
            throw new WorkerException("this task is not complete");
        }
        return this.completeTime - this.startTime;
    }

    /**
     * 获取总共的时间，包括等待的时间，以及执行的时间
     * @return
     */
    public long getTotalTime(){
        if(this.startTime == 0 || this.completeTime == 0){
            throw new WorkerException("this task is not complete");
        }
        return this.completeTime - this.initTime;
    }

    public boolean isRunError(){
        return this.ex != null;
    }

    public Throwable getError(){
        return this.ex;
    }

    public long getInitTime() {
        return initTime;
    }

    public long getStartTime() {
        if(this.startTime == 0){
            throw new WorkerException("this worker is not start");
        }
        return startTime;
    }

    public long getCompleteTime() {
        if(this.startTime == 0 || this.completeTime == 0){
            throw new WorkerException("this task is not complete");
        }
        return completeTime;
    }
}
