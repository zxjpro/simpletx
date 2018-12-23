package com.xiaojiezhu.simpletx.client.net;

import com.xiaojiezhu.simpletx.client.net.packet.input.LoginInputPacket;
import com.xiaojiezhu.simpletx.client.net.packet.output.LoginOutputPacket;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.security.DigestUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

/**
 *
 * authKey      last    String
 * @author xiaojie.zhu
 * time 2018/12/22 11:03
 */
@AllArgsConstructor
public class LoginHandler implements ProtocolHandler<LoginInputPacket> {

    private String appName;
    private String appid;
    private String password;

    @Override
    public void handler(ConnectionContext connectionContext, int msgId , int code , LoginInputPacket content) {
        connectionContext.set(Constant.Client.ConnectionSession.ID, content.getConnectionId());
        String pwd = DigestUtil.sha256Hex(content.getAuthKey() + this.password);
        LoginOutputPacket packet = new LoginOutputPacket(this.appName , this.appid , pwd);

        connectionContext.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                Message message = MessageUtil.createMessage(MessageIdGenerator.getInstance().next(), Constant.Client.ProtocolCode.CODE_LOGIN,buffer, packet);
                return message;
            }
        });


    }
}
