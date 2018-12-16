package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 20:43
 */
@AllArgsConstructor
class ReuseConnection implements Connection {

    private FixedChannelPool pool;
    private Channel channel;

    @Override
    public boolean isActive() {
        return this.channel.isActive();
    }

    @Override
    public void sendMessage(Message message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void close() throws IOException {
        this.pool.release(this.channel);
    }

    public void close0(){
        this.channel.close();
    }

}
