package com.xiaojiezhu.simpletx.protocol.future;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaojie.zhu
 * time 2018/12/24 23:03
 */
class CountFutureCondition implements FutureCondition{
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private Object id;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    @Setter
    private Future<?> future;

    private FutureListener<?> futureListener;

    private final AtomicInteger num;

    public CountFutureCondition(Object id) {
        this(id , 1);
    }

    public CountFutureCondition(Object id , int num) {
        this.id = id;
        this.num = new AtomicInteger(num);
    }

    private Object value;



    @Override
    public void await() throws InterruptedException{
        if(this.num.get() > 0){
            this.lock.lock();

            try {
                this.condition.await();
            } finally {
                this.lock.unlock();
            }
        }



    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException{
        if(this.num.get() > 0){
            this.lock.lock();

            boolean r = false;
            try {
                r = this.condition.await(time, unit);
            } finally {
                this.lock.unlock();
            }

            return r;
        }else{
            return true;
        }

    }

    @Override
    public void signal(){
        if(this.num.get() <= 0){
            throw new RuntimeException("num must > 0");
        }
        int i = this.num.decrementAndGet();


        if(i == 0){

            this.lock.lock();

            try {
                this.condition.signalAll();
            } finally {
                this.lock.unlock();
            }

        }

    }



    @Override
    public Object getId() {
        return this.id;
    }

    @Override
    public Future<?> getFuture() {
        return this.future;
    }

    @Override
    public FutureListener<?> getFutureListener() {
        return this.futureListener;
    }

    @Override
    public void setFutureListener(FutureListener<?> futureListener) {
        this.futureListener = futureListener;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

}
