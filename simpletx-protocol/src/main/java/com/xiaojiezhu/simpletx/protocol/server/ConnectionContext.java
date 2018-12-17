package com.xiaojiezhu.simpletx.protocol.server;

import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:20
 */
public interface ConnectionContext {

    long connectTime();


    Channel channel();

}
