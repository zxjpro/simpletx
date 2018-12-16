package com.xiaojiezhu.simpletx.client.spring;

import com.xiaojiezhu.simpletx.core.TransactionAspectConfigure;
import com.xiaojiezhu.simpletx.core.handler.TransactionAspectSupport;
import com.xiaojiezhu.simpletx.core.handler.TransactionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 22:24
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(TransactionAspectConfigure.class)
public class SimpletxAutoConfigure {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @ConditionalOnMissingBean(TransactionAspectSupport.class)
    public TransactionAspectSupport simpletxTransactionInterceptor(TransactionAttributeSource transactionAttributeSource){

        String id = UUID.randomUUID().toString().replace("-" , "");

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(appName,id);
        transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
        return transactionInterceptor;
    }


}
