package com.xiaojiezhu.simpletx.core.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * this is a empty transaction manager
 * @author xiaojie.zhu
 * time 2019-01-01 13:54
 */
public class EmptyTransactionManager implements PlatformTransactionManager {
    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return null;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {

    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {

    }
}
