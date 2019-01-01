package com.xiaojiezhu.simpletx.sample.http.order.configuration;

import com.xiaojiezhu.simpletx.core.transaction.EmptyTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author xiaojie.zhu
 * time 2019-01-01 13:58
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public PlatformTransactionManager emptyTransactionManager(){
        return new EmptyTransactionManager();
    }
}

