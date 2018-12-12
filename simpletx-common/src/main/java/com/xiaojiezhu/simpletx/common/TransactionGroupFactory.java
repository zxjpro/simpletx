package com.xiaojiezhu.simpletx.common;

import java.lang.annotation.Inherited;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 22:01
 */
public abstract class TransactionGroupFactory {


    /**
     * generate transaction id
     * @return
     */
    public abstract String generateGroupId();



    public static TransactionGroupFactory getInstance(){
        return Instance.INS;
    }

    private static class Instance{

        private static final TransactionGroupFactory INS = new SimpleTransactionGroupFactory();
    }
}
