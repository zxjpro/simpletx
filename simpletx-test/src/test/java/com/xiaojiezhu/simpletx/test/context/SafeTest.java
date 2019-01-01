package com.xiaojiezhu.simpletx.test.context;

import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.protocol.client.ExpireFutureContainer;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.context.DefaultTransactionServerContext;

import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 21:19
 */
public class SafeTest {

    public static void main(String[] args) {
        DefaultTransactionServerContext context = new DefaultTransactionServerContext(new FixThreadExecutor(10) , new ExpireFutureContainer());

        Thread t1 = new Thread(()->{
            long lastPrintTime = 0 ;
            while (true){
                String groupId = UUID.randomUUID().toString();
                context.createTransactionGroup(groupId , new TransactionBlock());

                if(System.currentTimeMillis() - lastPrintTime > 1000){
                    lastPrintTime = System.currentTimeMillis();
                    System.out.println("add 1..");

                }
            }
        });

        Thread t2 = new Thread(()->{
            long lastPrintTime = 0 ;
            while (true){
                String groupId = UUID.randomUUID().toString();
                context.createTransactionGroup(groupId , new TransactionBlock());

                if(System.currentTimeMillis() - lastPrintTime > 1000){
                    lastPrintTime = System.currentTimeMillis();
                    System.out.println("add 2..");

                }
            }
        });

        Thread t3 = new Thread(() -> {
            long lastPrintTime = 0 ;
            while (true){
                int groupSize = context.transactionGroupSize();

                if(System.currentTimeMillis() - lastPrintTime > 1000){
                    lastPrintTime = System.currentTimeMillis();
                    System.out.println(groupSize);
                }
            }

        });

        t1.start();
        t2.start();
        t3.start();

    }
}
