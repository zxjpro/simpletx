package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.StringUtils;
import com.xiaojiezhu.simpletx.util.asserts.Asserts;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 10:44
 */
public class DefaultConnectionContextHolder implements ConnectionContextHolder {

    protected final ConcurrentHashMap<Channel , ConnectionContext> connectionContextMap = new ConcurrentHashMap<>();

    private boolean server;

    public DefaultConnectionContextHolder() {
        if(System.getProperty(Constant.SIMPLETX_SERVER) != null){
            this.server = true;
        }else{
            this.server = false;
        }
    }

    protected final ConcurrentHashMap<Channel , ConnectionContext> getConnectionContextMap(){
        return this.connectionContextMap;
    }


    @Override
    public void remove(Channel channel) {
        ConnectionContext connectionContext = this.connectionContextMap.get(channel);
        if(connectionContext == null){
            throw new NullPointerException(StringUtils.str("The channel ",channel , " is not found connection context"));
        }

        this.connectionContextMap.remove(channel);
    }

    @Override
    public int connectionSize() {
        return this.connectionContextMap.size();
    }

    @Override
    public ConnectionContext getConnectionContext(Channel channel) {
        Asserts.assertNotNull(channel , "channel can not be null");
        ConnectionContext connectionContext = this.connectionContextMap.get(channel);
        if(connectionContext == null){

            synchronized (channel){
                if(connectionContext == null){
                    if(this.server){
                        connectionContext = new DefaultServerConnectionContext(channel);
                    }else{
                        connectionContext = new ClientConnectionContext(channel);

                    }
                    this.connectionContextMap.put(channel , connectionContext);

                }
            }
        }

        return connectionContext;
    }
}
