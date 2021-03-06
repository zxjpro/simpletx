package com.xiaojiezhu.simpletx.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:28
 */
public class MessageIdGenerator {

    private final AtomicInteger count = new AtomicInteger(1);

    public synchronized int next() {
        if(count.get() == Integer.MAX_VALUE){
            count.set(1);
        }

        return count.getAndIncrement();
    }

    public static MessageIdGenerator getInstance(){
        return Instance.INSTANCE;
    }



    private static class Instance{
        private static final MessageIdGenerator INSTANCE = new MessageIdGenerator();
    }
}
