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
        msg.markReaderIndex();

        int readableBytes = msg.readableBytes();
        if(!readPacket(msg , readableBytes)){
            msg.resetReaderIndex();
            return;
        }else{
            msg.resetReaderIndex();
            ByteBuf byteBuf = msg.retainedDuplicate();
            msg.skipBytes(msg.readableBytes());

            Message message = new Message();
            Header header = new Header();
            header.setBodyLength(byteBuf.readMedium());
            header.setId(byteBuf.readMedium());
            header.setCode(byteBuf.readInt());

            byte[] body = new byte[header.getBodyLength()];
            byteBuf.readBytes(body);

            message.setHeader(header);
            message.setBody(body);

            out.add(message);

            byteBuf.release();
        }
    }


    private boolean readPacket(ByteBuf msg , int readableBytes){
        int len = msg.readMedium();
        return readableBytes >= Header.HEADER_BYTE_LENGTH + len;
    }


}
