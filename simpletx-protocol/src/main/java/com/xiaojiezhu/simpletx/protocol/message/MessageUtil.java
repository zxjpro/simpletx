package com.xiaojiezhu.simpletx.protocol.message;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:26
 */
public class MessageUtil {


    public static Message createMessage(int id , int code , byte[] bytes){
        Header header = new Header(id , code , bytes.length);
        Message message = new Message(header , bytes);
        return message;
    }

    public static Message createMessage(int id , int code , OutputPacket outputPacket){
        ByteBuf buffer = Unpooled.buffer();
        return createMessage(id , code ,buffer, outputPacket);
    }

    public static Message createMessage(int id , int code , ByteBuf byteBuf ,  OutputPacket outputPacket){
        outputPacket.write(new ByteBuffer(byteBuf));

        byte[] buf = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(buf);

        return createMessage(id , code , buf);
    }

    public static Message createOkMessage(int responseMsgId , String msg , ByteBuf byteBuf) {
        OkErrorPacket packet = new OkErrorPacket(true , responseMsgId , msg);
        Message message = createMessage(MessageIdGenerator.getInstance().next(), Constant.Server.ProtocolCode.CODE_OK_ERROR, byteBuf,packet);
        return message;
    }

    public static Message createErrorMessage(int responseMsgId , String msg , ByteBuf byteBuf) {
        OkErrorPacket packet = new OkErrorPacket(false , responseMsgId , msg);
        Message message = createMessage(MessageIdGenerator.getInstance().next(), Constant.Server.ProtocolCode.CODE_OK_ERROR, byteBuf,packet);
        return message;
    }

}
