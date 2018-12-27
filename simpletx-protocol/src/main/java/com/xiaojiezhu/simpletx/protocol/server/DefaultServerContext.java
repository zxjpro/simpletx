package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.DefaultConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.channel.Channel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:24
 */
public class DefaultServerContext extends DefaultConnectionContextHolder implements ServerContext{


    @Override
    public void disconnect(int id) {
        ServerConnectionContext serverConnectionContext = this.find(id);
        if(serverConnectionContext == null){
            throw new NullPointerException(StringUtils.str(id , " not found"));
        }

        try {
            serverConnectionContext.close();
        } catch (IOException e) {
            throw new RuntimeException("disconnection fail" , e);
        }

    }

    @Override
    public ServerConnectionContext find(int id) {
        ConcurrentHashMap<Channel, ConnectionContext> connectionContextMap = getConnectionContextMap();
        Collection<ConnectionContext> values = connectionContextMap.values();
        for (ConnectionContext connectionContext : values) {
            try {
                int cid = connectionContext.getId();
                if(cid == id){
                    return (ServerConnectionContext) connectionContext;
                }
            } catch (Exception e) {

            }
        }

        return null;
    }

    @Override
    public List<ServerConnectionContext> findConnectionByAppName(String appName) {
        return null;
    }

    @Override
    public ServerConnectionContext findConnectionByAppNameAndAppid(String appName, String appid) {
        return null;
    }
}
