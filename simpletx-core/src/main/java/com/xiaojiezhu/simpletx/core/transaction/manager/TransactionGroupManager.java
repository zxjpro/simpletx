package com.xiaojiezhu.simpletx.core.transaction.manager;

import com.xiaojiezhu.simpletx.core.net.packet.input.TransactionGroupCompleteInputPacket;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.net.OkErrorResult;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 21:13
 */
public interface TransactionGroupManager {

    Future<OkErrorPacket> createGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter);

    Future<OkErrorPacket> joinGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter);

    /**
     * get the transaction group invoke status
     * @return
     */
    TransactionGroupInvokeStatus status();


    /**
     * notify simpletx-server to transaction group commit
     * @param transactionGroupId
     * @return
     */
    Future<TransactionGroupCompleteInputPacket> notifyGroupCommit(String transactionGroupId);

    /**
     * notify simpletx-server to transaction group rollback
     * @param transactionGroupId
     * @return
     */
    Future<TransactionGroupCompleteInputPacket> notifyGroupRollback(String transactionGroupId);


    /**
     * when local transaction invoke complete , notify to simpletx-server
     * @param commit true: commit transaction , false: rollback transaction
     * @param success  true: invoke local transaction success  , false: invoke local transaction fail
     * @param messageId messageId of simpletx-server notify client invoke local transaction
     * @param useTime invoke local transaction time
     */
    void notifyLocalTransactionComplete(boolean commit, boolean success, int messageId ,long useTime);
}
