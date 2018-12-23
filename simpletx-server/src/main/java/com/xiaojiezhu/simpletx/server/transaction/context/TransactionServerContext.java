package com.xiaojiezhu.simpletx.server.transaction.context;

import com.xiaojiezhu.simpletx.protocol.server.ServerContext;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.TransactionGroup;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:38
 */
public interface TransactionServerContext extends ServerContext {

    /**
     * create a new transaction group
     * @param groupId
     * @param transactionBlock
     */
    void createTransactionGroup(String groupId , TransactionBlock transactionBlock);

    /**
     * remove transaction group
     * @param groupId
     */
    void removeTransactionGroup(String groupId);

    /**
     * find a transaction group
     * @param groupId
     * @return
     */
    TransactionGroup findTransactionGroup(String groupId);

    /**
     * join a exists transaction group
     * @param groupId groupId
     * @param transactionBlock
     */
    void joinTransactionGroup(String groupId , TransactionBlock transactionBlock);

    /**
     * length transaction group
     * @return
     */
    int transactionGroupSize();
}
