package com.xiaojiezhu.simpletx.client.net;

import com.xiaojiezhu.simpletx.client.net.packet.input.LoginInputPacket;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.security.DigestUtil;
import lombok.AllArgsConstructor;

/**
 *
 * channelId    3       int
 * authKey      last    String
 *
 * @author xiaojie.zhu
 * time 2018/12/22 11:03
 */
@AllArgsConstructor
public class LoginHandler implements ProtocolHandler<LoginInputPacket> {


    private String password;

    @Override
    public void handler(ConnectionContext connectionContext, LoginInputPacket content) {

        connectionContext.set(Constant.Client.ConnectionSession.ID, content.getConnectionId());

        String pwd = DigestUtil.sha256Hex(content.getAuthKey() + this.password);

        int msgId = MessageIdGenerator.getInstance().next();

        Message message = MessageUtil.createMessage(msgId, Constant.Client.ProtocolCode.CODE_LOGIN, pwd.getBytes());

        connectionContext.sendMessage(message);


    }
}
