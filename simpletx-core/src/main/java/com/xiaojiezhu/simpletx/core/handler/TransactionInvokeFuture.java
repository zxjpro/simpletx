package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.core.exception.DuplicateInvokeException;
import com.xiaojiezhu.simpletx.util.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 16:51
 */
public class TransactionInvokeFuture {

    private final Runnable commitRunnable;
    private final Runnable rollbackRunnable;

    private final String groupId;

    private Boolean commit;

    private Lock lock;
    private Condition condition;

    private long timeout;


    public TransactionInvokeFuture(Runnable commitRunnable, Runnable rollbackRunnable, String groupId, long timeout) {
        this.commitRunnable = commitRunnable;
        this.rollbackRunnable = rollbackRunnable;
        this.groupId = groupId;
        this.timeout = timeout;
    }


    /**
     * start the future
     * @throws InterruptedException
     */
    public void start() throws InterruptedException, TimeoutException {
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.lock.lock();

        try {
            boolean await = this.condition.await(this.timeout, TimeUnit.MILLISECONDS);
            if(!await){
                //TODO: 等待超时未处理
                //throw new TimeoutException(StringUtils.str("transaction group : " , this.groupId , " wait tx-server timeout"));
            }
        } finally {
            this.lock.unlock();
        }

        if(this.commit == null){
            //TODO: 等待超时未处理
        }else{
            if(this.commit){
                commitRunnable.run();
            }else{
                rollbackRunnable.run();
            }
        }
    }



    public String getGroupId(){
        return this.groupId;
    }

    public synchronized void commit(){
        this.check();

        this.commit = true;

        this.condition.signalAll();

    }

    public synchronized void rollback(){
        this.check();

        this.commit = false;

        this.condition.signalAll();
    }

    private void check(){
        if(this.lock == null){
            throw new NullPointerException("this future does't start");
        }
        if(commit != null){
            throw new DuplicateInvokeException("transaction group can not invoke twice");
        }
    }


}
