package com.xiaojiezhu.simpletx.core.transaction;

import com.xiaojiezhu.simpletx.core.TransactionMethodAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 16:15
 */
@AllArgsConstructor
@Getter
public class TransactionInfo {

    private TransactionMethodAttribute methodAttribute;

    private PlatformTransactionManager transactionManager;

    private TransactionAttribute transactionAttribute;

    private TransactionStatus status;

    /**
     * 根事务，最先发起者
     */
    private boolean rootTransaction;


}
