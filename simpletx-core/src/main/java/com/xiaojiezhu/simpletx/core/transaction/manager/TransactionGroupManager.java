package com.xiaojiezhu.simpletx.core.transaction.manager;

import com.xiaojiezhu.simpletx.common.executor.Future;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 21:13
 */
public interface TransactionGroupManager {

    void createGroup(TransactionInfo transactionInfo, MethodParameter methodParameter);

    void joinGroup(TransactionInfo transactionInfo, MethodParameter methodParameter);

    /**
     * get the transaction group invoke status
     * @return
     */
    TransactionGroupInvokeStatus status();


    TransactionGroupInvokeFuture notifyCommit();

    TransactionGroupInvokeFuture notifyRollback();


}
