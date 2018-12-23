package com.xiaojiezhu.simpletx.server.dispatcher.handler;

import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.server.packet.input.AuthorizationInputPacket;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.StringUtils;
import com.xiaojiezhu.simpletx.util.security.DigestUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 20:00
 */
@AllArgsConstructor
public class AuthorizationHandler implements ProtocolHandler<AuthorizationInputPacket> {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private String password;

    @Override
    public void handler(ConnectionContext connectionContext,int msgId , int code , AuthorizationInputPacket content) {
        String authKey = (String) connectionContext.get(Constant.Server.ConnectionSession.AUTH_KEY);
        String sign = DigestUtil.sha256Hex(authKey + this.password);

        if(sign.equals(content.getPassword())){
            LOG.info(StringUtils.str(connectionContext.remoteIpAddress() , " login success"));
            connectionContext.set(Constant.Server.ConnectionSession.LOGIN_SUCCESS , true);
            connectionContext.set(Constant.Server.ConnectionSession.APP_NAME , content.getAppName());
            connectionContext.set(Constant.Server.ConnectionSession.APPID , content.getAppid());

            connectionContext.sendMessage(new MessageCreator() {
                @Override
                public Message create(ByteBuf buffer) {
                    return MessageUtil.createOkMessage(msgId , null ,buffer);
                }
            });
        }else{
            LOG.info(StringUtils.str(connectionContext.remoteIpAddress() , " login fail"));
            connectionContext.close();
        }

    }
}
