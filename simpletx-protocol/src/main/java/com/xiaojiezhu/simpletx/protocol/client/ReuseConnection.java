package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ClientConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 20:43
 */
class ReuseConnection implements Connection {

    private FixedChannelPool pool;
    private ConnectionContext connectionContext;

    public ReuseConnection(FixedChannelPool pool , ConnectionContext connectionContext) {
        this.pool = pool;
        this.connectionContext = connectionContext;
    }


    @Override
    public void close() throws IOException {
        this.pool.release(this.connectionContext.channel());
    }

    public void close0() throws IOException {
        this.connectionContext.close();
    }

    @Override
    public boolean isActive() {
        return this.connectionContext.isActive();
    }

    @Override
    public void sendMessage(Message message) {
        this.connectionContext.sendMessage(message);
    }

    @Override
    public void sendMessage(MessageCreator messageCreator) {
        this.connectionContext.sendMessage(messageCreator);
    }
}
