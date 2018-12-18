package com.xiaojiezhu.simpletx.client.configuration;

import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:51
 */
public class SimpletxServerConfiguration {


    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool connectionPool(SimpletxServerProperties properties) {
        DefaultConnectionPool connectionPool = new DefaultConnectionPool();
        connectionPool.setHost(properties.getHost());
        connectionPool.setPort(properties.getPort());
        connectionPool.setPassword(properties.getPassword());
        connectionPool.setMaxActive(properties.getMaxActive());
        return connectionPool;
    }

}
