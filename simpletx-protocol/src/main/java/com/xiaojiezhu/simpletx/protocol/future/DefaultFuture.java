package com.xiaojiezhu.simpletx.protocol.future;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaojie.zhu
 * time 2018/12/24 21:23
 */
@AllArgsConstructor
public class DefaultFuture<T> implements Future<T> {

    private final FutureCondition futureCondition;

    @Override
    public T getNow() {
        return (T) this.futureCondition.getValue();
    }

    @Override
    public T get() throws InterruptedException {
        T value = this.getNow();
        if(value != null){
            return value;
        }else{
            this.await();

            return this.getNow();
        }
    }

    @Override
    public T get(long time, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        T value = this.getNow();
        if(value != null){
            return value;
        }else{
            this.await(time , timeUnit);

            return this.getNow();
        }
    }

    @Override
    public void await() throws InterruptedException {
        this.futureCondition.await();
    }

    @Override
    public void await(long time, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        boolean r = futureCondition.await(time, timeUnit);
        if(!r){
            throw new TimeoutException("await timeout , time:" + time + " , timeUnit:" + timeUnit);
        }

    }

    @Override
    public void addListener(FutureListener<T> futureListener) throws InterruptedException {
        T value = getNow();
        if(value != null){
            futureListener.complete(this);
        }

        this.futureCondition.setFutureListener(futureListener);

    }
}
