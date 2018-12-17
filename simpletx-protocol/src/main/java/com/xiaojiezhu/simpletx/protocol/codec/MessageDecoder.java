package com.xiaojiezhu.simpletx.protocol.codec;

import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:08
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        while (readPacket(msg , msg.readableBytes())){
            msg.resetReaderIndex();

            Message message = new Message();
            Header header = new Header();
            header.setBodyLength(msg.readMedium());
            header.setId(msg.readMedium());
            header.setCode(msg.readInt());

            byte[] body = new byte[header.getBodyLength()];
            msg.readBytes(body);

            message.setHeader(header);
            message.setBody(body);

            out.add(message);

        }

        msg.resetReaderIndex();
        return;



    }


    private boolean readPacket(ByteBuf msg , int readableBytes){
        if(!msg.isReadable()){
            return false;
        }
        msg.markReaderIndex();

        int len = msg.readMedium();
        return readableBytes >= Header.HEADER_BYTE_LENGTH + len;
    }


}
