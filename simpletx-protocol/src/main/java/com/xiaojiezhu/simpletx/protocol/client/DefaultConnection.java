package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 19:02
 */
@AllArgsConstructor
class DefaultConnection implements Connection {

    private Channel channel;

    @Override
    public boolean isActive() {
        return this.channel.isActive();
    }

    @Override
    public void sendMessage(Message message) {
        this.channel.writeAndFlush(message);
    }

    @Override
    public void sendMessage(MessageCreator messageCreator) {
        ByteBuf buffer = this.channel.alloc().buffer();
        Message message = messageCreator.create(buffer);

        this.sendMessage(message);
    }

    @Override
    public void close() throws IOException {
        this.channel.close();
    }
}
