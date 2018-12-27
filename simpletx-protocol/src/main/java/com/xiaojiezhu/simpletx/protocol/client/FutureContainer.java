package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.future.Future;
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
    void add(int id , FutureCondition futureCondition);

    /**
     * remove a future
     * @param id
     */
    void remove(int id);

    /**
     * find a future by id
     * @param id
     * @return
     */
    FutureCondition find(int id);
}
