package com.xiaojiezhu.simpletx.core.transaction.manager;

/**
 * 事务组执行的状态，包括整个加入的事务的节点的事务状态
 * @author xiaojie.zhu
 * time 2018/12/12 23:03
 */
public class TransactionGroupInvokeStatus {

    /**
     * transaction group create time
     */
    private long startTime;

    /**
     * the transaction block size
     */
    private int nodeSize;



}
