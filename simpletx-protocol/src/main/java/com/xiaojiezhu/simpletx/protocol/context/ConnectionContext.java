package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:20
 */
public interface ConnectionContext {

    long connectTime();


    Channel channel();

    /**
     * get value by key from connection context
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     * set value to connection context
     * @param key
     * @param value
     */
    void set(Object key ,Object value);

    void sendMessage(Message message);

    String remoteIpAddress();

    void close();

    boolean isAuthorization();




}
