package com.xiaojiezhu.simpletx.protocol.server;

import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:24
 */
class DefaultServerContext implements ServerContext {
    @Override
    public void register(Channel channel) {

    }

    @Override
    public void remove(Channel channel) {

    }

    @Override
    public int connectionSize() {
        return 0;
    }

    @Override
    public ConnectionContext getConnectionContext(Channel channel) {
        return null;
    }
}
