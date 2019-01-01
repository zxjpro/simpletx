package com.xiaojiezhu.simpletx.protocol.client;


import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;

/**
 * save the sync connection response data
 * @author xiaojie.zhu
 * time 2018/12/25 22:22
 */
public interface FutureContainer {

    /**
     * add a future
     * @param id
     * @param futureCondition
     */
    void add(Object id , FutureCondition futureCondition);

    /**
     * remove a future
     * @param id
     */
    void remove(Object id);

    /**
     * find a future by id
     * @param id
     * @return
     */
    FutureCondition find(Object id);

}
