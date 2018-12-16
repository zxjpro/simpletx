package com.xiaojiezhu.simpletx.protocol.codec;

import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:22
 */
public class MessageEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        Header header = msg.getHeader();
        buffer.writeMedium(header.getBodyLength());
        buffer.writeMedium(header.getId());
        buffer.writeInt(header.getCode());
        buffer.writeBytes(msg.getBody());

        out.add(buffer);

    }

}
