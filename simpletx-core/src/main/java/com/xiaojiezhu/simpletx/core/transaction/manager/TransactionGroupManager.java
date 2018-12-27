package com.xiaojiezhu.simpletx.core.transaction.manager;

import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.net.OkErrorResult;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 21:13
 */
public interface TransactionGroupManager {

    Future<OkErrorResult> createGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter);

    Future<OkErrorResult> joinGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter);

    /**
     * get the transaction group invoke status
     * @return
     */
    TransactionGroupInvokeStatus status();


    Future<OkErrorResult> notifyCommit(String transactionGroupId);

    Future<OkErrorResult> notifyRollback(String transactionGroupId);


}
