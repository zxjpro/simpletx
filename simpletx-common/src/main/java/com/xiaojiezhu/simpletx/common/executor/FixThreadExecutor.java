package com.xiaojiezhu.simpletx.common.executor;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 17:49
 */
public class FixThreadExecutor implements ThreadExecutor {
    public final Logger LOG = LoggerFactory.getLogger(getClass());
    /**
     * the thread wait queue logger over size
     */
    private static final int LOGGER_TASK_WAIT_SIZE = 2;

    private final int threadSize;
    private final ThreadPoolExecutor pool;
    private final BlockingQueue<Runnable> queue;

    public FixThreadExecutor(int threadSize) {
        if(threadSize <= 0){
            throw new IllegalArgumentException("threadSize must greater 0 ");
        }
        this.threadSize = threadSize;

        this.queue = new LinkedBlockingQueue<>();

        this.pool = new ThreadPoolExecutor(this.threadSize , this.threadSize , 60 , TimeUnit.MINUTES ,
                this.queue , new DefaultThreadFactory());
    }

    @Override
    public ExecutorFuture executeFuture(Runnable runnable) {

        long waitThreadSize = this.getWaitThreadSize();
        if(waitThreadSize > LOGGER_TASK_WAIT_SIZE){
            LOG.error("task queue is waiting " + waitThreadSize + " size");
        }


        this.pool.execute(runnable);
        return null;
    }

    @Override
    public long getMaxThreadSize() {
        return this.threadSize;
    }

    @Override
    public long getWaitThreadSize() {
        return this.queue.size();
    }

    @Override
    public void execute(Runnable command) {
        this.executeFuture(command);
    }


    class DefaultThreadFactory implements ThreadFactory{
        static final String PREFIX_NAME = "SIMPLETX_";
        private final AtomicLong COUNT = new AtomicLong();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(PREFIX_NAME + COUNT.getAndIncrement());
            return thread;
        }
    }

}
