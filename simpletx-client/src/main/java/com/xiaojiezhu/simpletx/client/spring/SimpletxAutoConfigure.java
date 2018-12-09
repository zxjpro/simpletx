package com.xiaojiezhu.simpletx.client.spring;

import com.xiaojiezhu.simpletx.core.TransactionAspectConfigure;
import com.xiaojiezhu.simpletx.core.handler.TransactionAspectSupport;
import com.xiaojiezhu.simpletx.core.handler.TransactionInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 22:24
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(TransactionAspectConfigure.class)
public class SimpletxAutoConfigure {


    @Bean
    @ConditionalOnMissingBean(TransactionAspectSupport.class)
    public TransactionAspectSupport simpletxTransactionInterceptor(TransactionAttributeSource transactionAttributeSource){
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
        return transactionInterceptor;
    }


}
