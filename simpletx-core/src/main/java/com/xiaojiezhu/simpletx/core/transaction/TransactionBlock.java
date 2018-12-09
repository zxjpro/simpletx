package com.xiaojiezhu.simpletx.core.transaction;

/**
 * @author xiaojie.zhu
 * time 2018/12/5 22:59
 */
public interface TransactionBlock {

    String getApplicationName();

    /**
     * 获取方法签名
     * @return
     */
    String getMethodSignature();

    /**
     * 块事物开始的时间
     * @return
     */
    long getBeginTime();


    /**
     * 块事务的结束时间
     * @return
     */
    long getEndTime();

}
