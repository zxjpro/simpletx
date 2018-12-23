package com.xiaojiezhu.simpletx.client.configuration;

import com.xiaojiezhu.simpletx.client.util.DispatcherHelper;
import com.xiaojiezhu.simpletx.common.codec.*;
import com.xiaojiezhu.simpletx.core.transaction.manager.SocketTransactionGroupManager;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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

        DispatcherHelper dispatcherHelper = new DispatcherHelper(properties);

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
