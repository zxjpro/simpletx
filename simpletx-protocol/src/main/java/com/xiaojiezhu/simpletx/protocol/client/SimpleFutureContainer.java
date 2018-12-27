package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import com.xiaojiezhu.simpletx.util.asserts.Asserts;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/25 22:28
 */
public class SimpleFutureContainer implements FutureContainer {

    private final ConcurrentHashMap<Integer , FutureCondition> futureMap = new ConcurrentHashMap<>();

    @Override
    public void add(int id, FutureCondition futureCondition) {
        Asserts.assertNotNull(futureCondition , "futureCondition can not be null");
        this.futureMap.put(id , futureCondition);
    }

    @Override
    public void remove(int id) {
        this.futureMap.remove(id);
    }

    @Override
    public FutureCondition find(int id) {
        return this.futureMap.get(id);
    }
}
