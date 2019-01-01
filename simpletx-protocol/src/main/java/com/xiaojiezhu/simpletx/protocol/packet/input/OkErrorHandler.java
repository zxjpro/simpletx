package com.xiaojiezhu.simpletx.protocol.packet.input;

import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;
import com.xiaojiezhu.simpletx.util.Constant;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 23:36
 */
public class OkErrorHandler implements ProtocolHandler<OkErrorPacket> {


    @Override
    public void handler(ConnectionContext connectionContext, int msgId, int code, OkErrorPacket content) {

    }
}
