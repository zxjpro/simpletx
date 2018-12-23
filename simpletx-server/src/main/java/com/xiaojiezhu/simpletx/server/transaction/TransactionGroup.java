package com.xiaojiezhu.simpletx.server.transaction;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:45
 */
public interface TransactionGroup {

    /**
     * the transaction group create time
     * @return
     */
    long getCreateTime();

    List<TransactionBlock> listBlock();

    String getTransactionId();

    void join(TransactionBlock transactionBlock);


}
