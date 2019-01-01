package com.xiaojiezhu.simpletx.client.spring;

import com.xiaojiezhu.simpletx.client.configuration.SimpletxServerConfiguration;
import com.xiaojiezhu.simpletx.client.configuration.SimpletxServerProperties;
import com.xiaojiezhu.simpletx.client.util.DispatcherHelper;
import com.xiaojiezhu.simpletx.common.codec.*;
import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import com.xiaojiezhu.simpletx.core.TransactionAspectConfigure;
import com.xiaojiezhu.simpletx.core.handler.TransactionAspectSupport;
import com.xiaojiezhu.simpletx.core.handler.TransactionInterceptor;
import com.xiaojiezhu.simpletx.core.net.SocketTransactionGroupManager;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
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
@Import({TransactionAspectConfigure.class , SimpletxServerConfiguration.class})
public class SimpletxAutoConfigure {

    @Value("${spring.application.name}")
    private String appName;



    @Bean
    @ConditionalOnMissingBean(TransactionAspectSupport.class)
    public TransactionAspectSupport simpletxTransactionInterceptor(TransactionAttributeSource transactionAttributeSource ,ConnectionPool connectionPool){
        SimpletxContext simpletxContext = ((DefaultConnectionPool) connectionPool).getSimpletxContext();
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(simpletxContext);
        transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
        return transactionInterceptor;
    }


    @Bean
    @ConditionalOnMissingBean(ThreadExecutor.class)
    public ThreadExecutor simpletxThreadExecutor(SimpletxServerProperties simpletxServerProperties){
        return new FixThreadExecutor(simpletxServerProperties.getThreadSize());
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool connectionPool(SimpletxServerProperties properties , ThreadExecutor simpletxThreadExecutor) {
        String appid = UUID.randomUUID().toString().replace("-", "");

        DefaultConnectionPool connectionPool = new DefaultConnectionPool();
        connectionPool.setHost(properties.getHost());
        connectionPool.setPort(properties.getPort());
        connectionPool.setPassword(properties.getPassword());
        connectionPool.setMaxActive(properties.getMaxActive());

        connectionPool.setAppName(appName);
        connectionPool.setAppid(appid);

        connectionPool.setUserExecutor(simpletxThreadExecutor);

        connectionPool.start();


        SimpletxContext simpletxContext = connectionPool.getSimpletxContext();

        DispatcherHelper dispatcherHelper = new DispatcherHelper(this.appName ,appid, simpletxContext, properties);

        ProtocolDispatcher protocolDispatcher = dispatcherHelper.createProtocolDispatcher();

        connectionPool.setProtocolDispatcher(protocolDispatcher);

        return connectionPool;
    }

    @Bean
    @ConditionalOnMissingBean(TransactionGroupManager.class)
    public TransactionGroupManager transactionGroupManager(ConnectionPool connectionPool, SimpletxServerProperties simpletxServerProperties) {
        String codec = simpletxServerProperties.getCodec();
        ObjectCodec objectCodec = null;
        if(ObjectCodecs.Type.JDK.toString().equals(codec)){
            objectCodec = new JdkObjectCodec();
        }else if(ObjectCodecs.Type.JSON.toString().equals(codec)){
            objectCodec = new JSONObjectCodec();
        }else if(ObjectCodecs.Type.KRYO.toString().equals(codec)){
            objectCodec = new KryoObjectCodec();
        }else{
            throw new RuntimeException("not support serializable type : " + codec + " , check you config");
        }
        TransactionGroupManager transactionGroupManager = new SocketTransactionGroupManager(connectionPool , objectCodec);
        return transactionGroupManager;
    }


}
