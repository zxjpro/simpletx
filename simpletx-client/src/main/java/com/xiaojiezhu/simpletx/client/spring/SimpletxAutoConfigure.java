package com.xiaojiezhu.simpletx.client.spring;

import com.xiaojiezhu.simpletx.client.configuration.SimpletxServerConfiguration;
import com.xiaojiezhu.simpletx.client.configuration.SimpletxServerProperties;
import com.xiaojiezhu.simpletx.client.configuration.enter.HttpEnterConfigure;
import com.xiaojiezhu.simpletx.client.util.DispatcherHelper;
import com.xiaojiezhu.simpletx.common.codec.*;
import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import com.xiaojiezhu.simpletx.core.TransactionAspectConfigure;
import com.xiaojiezhu.simpletx.core.handler.TransactionAspectSupport;
import com.xiaojiezhu.simpletx.core.handler.TransactionInterceptor;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.net.SocketTransactionGroupManager;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.util.http.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
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
    public TransactionAspectSupport simpletxTransactionInterceptor(ConnectionPool connectionPool) {
        TransactionAttributeSource transactionAttributeSource = new AnnotationTransactionAttributeSource();
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
            //同时也作为simpletx组件间自己通信的序列化方式
            SimpletxTransactionUtil.setObjectCodec(objectCodec);

        }else{
            throw new RuntimeException("not support serializable type : " + codec + " , check you config");
        }
        TransactionGroupManager transactionGroupManager = new SocketTransactionGroupManager(connectionPool , objectCodec);
        return transactionGroupManager;
    }

    /**
     * http request
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(Servlet.class)
    public FilterRegistrationBean<HttpEnterConfigure> httpFilterEnterTransaction(){
        FilterRegistrationBean<HttpEnterConfigure> bean = new FilterRegistrationBean<>(new HttpEnterConfigure());
        bean.addUrlPatterns("/*");

        return bean;
    }

    /**
     * 这个bean可以不注入，只要拿到httpClient就好，也可以使用其它的http客户端
     * @return
     */
    @Bean
    public HttpClient simpletxDefaultHttpclient(){
        return SimpletxTransactionUtil.getHttpClient();
    }


}
