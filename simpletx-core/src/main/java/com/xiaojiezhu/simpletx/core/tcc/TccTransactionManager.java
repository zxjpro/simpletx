package com.xiaojiezhu.simpletx.core.tcc;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.Date;

/**
 * TODO: 基于TCC的事务管理器
 * @author xiaojie.zhu
 * time 2018/12/15 18:43
 */
public class TccTransactionManager implements PlatformTransactionManager {

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        Date d = new Date();
        return null;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        System.out.println("hello data hello ");
        System.out.println("hello data hello ");
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {

    }
}
