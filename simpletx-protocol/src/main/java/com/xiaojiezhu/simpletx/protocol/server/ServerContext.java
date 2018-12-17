package com.xiaojiezhu.simpletx.protocol.server;

import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:18
 */
public interface ServerContext {

    void register(Channel channel);


    void remove(Channel channel);

    int connectionSize();

    ConnectionContext getConnectionContext(Channel channel);
}
