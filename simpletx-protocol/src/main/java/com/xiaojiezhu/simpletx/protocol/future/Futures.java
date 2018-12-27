package com.xiaojiezhu.simpletx.protocol.future;

import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 10:48
 */
public class Futures {

    /**
     * create a server response future
     * @param messageId
     * @return
     */
    public static <T> Future<T> createOkErrorFuture(SimpletxContext simpletxContext , int messageId) {
        FutureCondition fc = new FutureCondition(messageId);
        Future<T> future = new DefaultFuture<>(fc);
        fc.setFuture(future);
        simpletxContext.getFutureContainer().add(messageId, fc);
        return future;
    }
}
