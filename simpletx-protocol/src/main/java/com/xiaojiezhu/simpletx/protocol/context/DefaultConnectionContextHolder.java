package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.StringUtils;
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
        if(System.getProperty(Constant.AUTHOR) != null){
            this.server = true;
        }else{
            this.server = false;
        }
    }

    protected final ConcurrentHashMap<Channel , ConnectionContext> getConnectionContextMap(){
        return this.connectionContextMap;
    }

    @Override
    public void register(Channel channel) {
        synchronized (channel){
            if(connectionContextMap.get(channel) != null){
                throw new RuntimeException("register repeat , the channel context is exists");
            }

            if(this.server){
                this.connectionContextMap.put(channel , new DefaultServerConnectionContext(channel));
            }else{
                this.connectionContextMap.put(channel , new DefaultConnectionContext(channel));
            }
        }

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
        ConnectionContext connectionContext = this.connectionContextMap.get(channel);
        if(connectionContext == null){
            throw new NullPointerException(StringUtils.str("The channel ",channel , " is not found connection context"));
        }
        return connectionContext;
    }
}
