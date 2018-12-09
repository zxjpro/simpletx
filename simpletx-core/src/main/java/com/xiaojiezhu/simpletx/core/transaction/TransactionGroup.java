package com.xiaojiezhu.simpletx.core.transaction;

/**
 * @author xiaojie.zhu
 * time 2018/12/4 23:07
 */
public interface TransactionGroup {

    /**
     * transaction group id
     * @return
     */
    String getGroupId();

    /**
     * get transaction block
     * @param object
     * @return
     */
    TransactionBlock getTransactionBlock(Object object);




}
