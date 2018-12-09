package com.xiaojiezhu.simpletx.test;

import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 18:11
 */
public class ThreadExecutorTest {


    @Test
    public void test(){


        long count = 1;

        while (true){
            System.out.println("name:" + (count++));
        }

    }


    @Test
    public void test2(){
        ThreadExecutor executor = new FixThreadExecutor(5);
        AtomicLong count = new AtomicLong();

        while (true){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(() -> {
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String name = Thread.currentThread().getName();
                System.out.println(name + "\t" + count.getAndIncrement() + "\t wait:" + executor.getWaitThreadSize());
            });
        }
    }
}
