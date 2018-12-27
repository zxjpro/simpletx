package com.xiaojiezhu.simpletx.protocol.future;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 12:12
 */
public interface FutureListener<T> {

    void complete(Future<T> future);
}
