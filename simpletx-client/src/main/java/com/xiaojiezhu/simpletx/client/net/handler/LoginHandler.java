package com.xiaojiezhu.simpletx.client.net.handler;

import com.xiaojiezhu.simpletx.client.exception.AuthorizationException;
import com.xiaojiezhu.simpletx.client.net.packet.input.LoginInputPacket;
import com.xiaojiezhu.simpletx.client.net.packet.output.LoginOutputPacket;
import com.xiaojiezhu.simpletx.core.exception.SocketRuntimeException;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.protocol.future.Futures;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.security.DigestUtil;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * authKey      last    String
 * @author xiaojie.zhu
 * time 2018/12/22 11:03
 */
public class LoginHandler implements ProtocolHandler<LoginInputPacket> {

    private final SimpletxContext simpletxContext;
    private final String appName;
    private final String appid;
    private final String password;

    public LoginHandler(SimpletxContext simpletxContext, String appName, String appid, String password) {
        this.simpletxContext = simpletxContext;
        this.appName = appName;
        this.appid = appid;
        this.password = password;

    }

    @Override
    public void handler(ConnectionContext connectionContext, int msgId , int code , LoginInputPacket content) {
        connectionContext.set(Constant.Client.ConnectionSession.ID, content.getConnectionId());
        String pwd = DigestUtil.sha256Hex(content.getAuthKey() + this.password);
        LoginOutputPacket packet = new LoginOutputPacket(this.appName , this.appid , pwd);

        final int messageId = MessageIdGenerator.getInstance().next();
        final Future<OkErrorPacket> future = Futures.createFuture(this.simpletxContext.getFutureContainer() , messageId);

        connectionContext.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                Message message = MessageUtil.createMessage(messageId, Constant.Client.ProtocolCode.CODE_LOGIN,buffer, packet);
                return message;
            }
        });
        try {
            future.await(10000 , TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException e) {
            throw new SocketRuntimeException("login simpletx-server timeout ");
        }

        OkErrorPacket okErrorPacket = future.getNow();
        if(okErrorPacket.isOk()){
            connectionContext.set(Constant.Client.ConnectionSession.LOGIN_SUCCESS , true);
        }else{
            throw new AuthorizationException("login fail , " + okErrorPacket.getMessage());
        }

    }
}
