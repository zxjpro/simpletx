package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 12:21
 */
@AllArgsConstructor
public class SimpleMessageSender implements MessageSender {
    protected final Channel channel;

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

    protected Channel getChannel(){
        return this.channel;
    }
}
