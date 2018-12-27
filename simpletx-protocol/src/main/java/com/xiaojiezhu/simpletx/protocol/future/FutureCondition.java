package com.xiaojiezhu.simpletx.protocol.future;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaojie.zhu
 * time 2018/12/24 23:03
 */
public class FutureCondition {

    @Getter
    private int id;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    @Setter
    @Getter
    private Future<?> future;

    @Setter
    @Getter
    private FutureListener<?> futureListener;

    public FutureCondition(int id) {
        this.id = id;
    }

    @Setter
    @Getter
    Object value;


    public void await() throws InterruptedException{
        this.lock.lock();

        try {
            this.condition.await();
        } finally {
            this.lock.unlock();
        }


    }

    boolean await(long time, TimeUnit unit) throws InterruptedException{
        this.lock.lock();

        boolean r = false;
        try {
            r = this.condition.await(time , unit);
        } finally {
            this.lock.unlock();
        }

        return r;
    }

    public void signal(){
        this.lock.lock();

        try {
            this.condition.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void signalAll(){
        this.lock.lock();

        try {
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}
