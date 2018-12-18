package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:24
 */
class DefaultServerContext implements ServerContext {

    private final ConcurrentHashMap<Channel , ConnectionContext> connectionContextMap = new ConcurrentHashMap<>();

    public DefaultServerContext() {

    }

    @Override
    public void register(Channel channel) {
        synchronized (channel){
            if(connectionContextMap.get(channel) != null){
                throw new RuntimeException("register repeat , the channel context is exists");
            }

            this.connectionContextMap.put(channel , new DefaultConnectionContext(channel));
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
