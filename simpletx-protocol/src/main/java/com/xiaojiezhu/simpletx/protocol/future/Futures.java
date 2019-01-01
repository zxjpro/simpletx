package com.xiaojiezhu.simpletx.protocol.future;

import com.xiaojiezhu.simpletx.protocol.client.FutureContainer;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 10:48
 */
public class Futures {

    /**
     * create a server response future
     * @param id future id
     * @return
     */
    public static <T> Future<T> createFuture(FutureContainer futureContainer, Object id) {
        CountFutureCondition fc = new CountFutureCondition(id);
        Future<T> future = new DefaultFuture<>(fc);
        fc.setFuture(future);
        futureContainer.add(id, fc);
        return future;
    }

    /**
     * create the of list value future , the default future num is 1
     * @param id future id
     */
    public static <T> Future<T> createListValueFuture(FutureContainer futureContainer, Object id){
        return createListValueFuture(futureContainer , id , 1);
    }

    /**
     *
     * @param futureContainer
     * @param id future id
     * @param num
     * @param <T>
     * @return
     */
    public static <T> Future<T> createListValueFuture(FutureContainer futureContainer, Object id , int num){
        ListFutureCondition lfc = new ListFutureCondition(id , num);
        Future<T> future = new DefaultFuture<>(lfc);
        lfc.setFuture(future);
        futureContainer.add(id , lfc);

        return future;
    }


}
