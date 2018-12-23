package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.DefaultConnectionContextHolder;

import java.nio.channels.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:24
 */
class DefaultServerContext extends DefaultConnectionContextHolder implements ServerContext{


    @Override
    public void disconnect(int id) {
    }
}
