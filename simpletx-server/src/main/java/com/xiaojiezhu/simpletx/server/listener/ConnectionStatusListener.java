package com.xiaojiezhu.simpletx.server.listener;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.protocol.server.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import com.xiaojiezhu.simpletx.server.util.Constant;
import com.xiaojiezhu.simpletx.server.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.server.util.RandomUtil;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:20
 */
public class ConnectionStatusListener implements ConnectionEventListener {

    @Override
    public void onChannelActive(ConnectionContext connectionContext) {
        String key = RandomUtil.randomString();
        connectionContext.set(Constant.ConnectionSession.AUTH_KEY , key);

        Message message = MessageUtil.createMessage(MessageIdGenerator.getInstance().next(), Constant.ProtocolCode.CODE_AUTH_KEY, key.getBytes());

        connectionContext.channel().writeAndFlush(message);
    }

    @Override
    public void onChannelDisconnect(ConnectionContext connectionContext) {

    }

    @Override
    public void onChannelError(ConnectionContext connectionContext, Throwable throwable) {
        connectionContext.channel().close();
    }
}
