package com.xiaojiezhu.simpletx.server.listener;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import com.xiaojiezhu.simpletx.util.ConnectionIdGenerator;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.server.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:20
 */
public class ConnectionStatusListener implements ConnectionEventListener {

    @Override
    public void onChannelActive(ConnectionContext connectionContext) {
        String key = RandomUtil.randomString();
        connectionContext.set(Constant.Server.ConnectionSession.AUTH_KEY , key);

        int connectionId = ConnectionIdGenerator.getInstance().next();

        connectionContext.set(Constant.Server.ConnectionSession.ID , connectionId);

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeMedium(connectionId);
        buffer.writeBytes(key.getBytes());

        byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);

        buffer.release();

        int msgId = MessageIdGenerator.getInstance().next();

        Message message = MessageUtil.createMessage(msgId, Constant.Server.ProtocolCode.CODE_AUTH_KEY , array);

        connectionContext.sendMessage(message);
    }

    @Override
    public void onChannelDisconnect(ConnectionContext connectionContext) {

    }

    @Override
    public void onChannelError(ConnectionContext connectionContext, Throwable throwable) {
        connectionContext.channel().close();
    }
}
