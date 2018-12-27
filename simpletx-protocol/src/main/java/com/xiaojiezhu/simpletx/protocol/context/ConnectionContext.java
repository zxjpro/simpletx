package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import io.netty.channel.Channel;

import java.io.Closeable;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:20
 */
public interface ConnectionContext extends MessageSender, Closeable {

    /**
     * the connect time
     * @return
     */
    long connectTime();


    /**
     * the native channel
     * @return
     */
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



    /**
     * get the remove ip address
     * @return
     */
    String remoteIpAddress();


    /**
     * the connection is login success
     * @return
     */
    boolean isAuthorization();

    /**
     * get channel id
     * @return
     */
    int getId();

    /**
     * the connection is active , if false ,the connection is disable
     * @return
     */
    boolean isActive();




}
