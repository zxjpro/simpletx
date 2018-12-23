package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 10:37
 */
public interface ConnectionContextHolder {

    void register(Channel channel);


    void remove(Channel channel);

    int connectionSize();

    ConnectionContext getConnectionContext(Channel channel);
}
