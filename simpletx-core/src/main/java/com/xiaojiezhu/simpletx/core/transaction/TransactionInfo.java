package com.xiaojiezhu.simpletx.core.transaction;

import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 16:15
 */
public class TransactionInfo {

    @Getter
    private TransactionMethodAttribute methodAttribute;

    @Getter
    private PlatformTransactionManager transactionManager;

    @Getter
    private TransactionAttribute transactionAttribute;

    private TransactionStatus status;
    /**
     * 用于获取 TransactionStatus 的线程id
     */
    private long statusThreadId;

    /**
     * 根事务，最先发起者
     */
    @Getter
    private boolean rootTransaction;

    @Getter
    private String transactionGroupId;

    public TransactionInfo(TransactionMethodAttribute methodAttribute, PlatformTransactionManager transactionManager, TransactionAttribute transactionAttribute, boolean rootTransaction, String transactionGroupId) {
        this.methodAttribute = methodAttribute;
        this.transactionManager = transactionManager;
        this.transactionAttribute = transactionAttribute;
        this.rootTransaction = rootTransaction;
        this.transactionGroupId = transactionGroupId;
    }

    public synchronized TransactionStatus getStatus() {
        if(this.status == null){
            long threadId = Thread.currentThread().getId();

            if(this.statusThreadId != threadId){
                if(this.statusThreadId == 0){
                    this.statusThreadId = threadId;
                    if(this.transactionAttribute != null){

                        this.status = transactionManager.getTransaction(transactionAttribute);
                    }else{
                        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                        this.status = transactionManager.getTransaction(def);
                    }

                }else{
                    throw new RuntimeException("the TransactionStatus bind threadId:" + this.statusThreadId + " , current threadId:" + threadId);
                }
            }
        }

        return this.status;
    }
}
