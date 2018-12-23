package com.xiaojiezhu.simpletx.server.transaction;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 20:28
 */
public class DefaultTransactionGroup implements TransactionGroup{

    private String transactionGroupId;

    private final List<TransactionBlock> blocks = new LinkedList<>();

    private final long createTime = System.currentTimeMillis();

    public DefaultTransactionGroup(String transactionGroupId , TransactionBlock transactionBlock) {
        this.transactionGroupId = transactionGroupId;
        this.blocks.add(transactionBlock);
    }

    @Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public List<TransactionBlock> listBlock() {
        return this.blocks;
    }

    @Override
    public String getTransactionId() {
        return this.transactionGroupId;
    }

    @Override
    public void join(TransactionBlock transactionBlock) {
        this.blocks.add(transactionBlock);
    }
}
