package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import com.xiaojiezhu.simpletx.util.asserts.Asserts;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/25 22:28
 */
public class SimpleFutureContainer implements FutureContainer {

    private final ConcurrentHashMap<Object , FutureCondition> futureMap = new ConcurrentHashMap<>();

    @Override
    public void add(Object id, FutureCondition futureCondition) {
        Asserts.assertNotNull(futureCondition , "futureCondition can not be null");
        this.futureMap.put(id , futureCondition);
    }

    @Override
    public void remove(Object id) {
        this.futureMap.remove(id);
    }

    @Override
    public FutureCondition find(Object id) {
        return this.futureMap.get(id);
    }

    @Override
    public FutureCondition findAndRemove(Object id) {
        FutureCondition futureCondition = find(id);
        this.remove(id);
        return futureCondition;
    }
}
