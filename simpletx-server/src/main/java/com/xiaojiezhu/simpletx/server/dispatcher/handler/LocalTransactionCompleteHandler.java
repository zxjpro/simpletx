package com.xiaojiezhu.simpletx.server.dispatcher.handler;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.server.packet.input.LocalTransactionInputCompletePacket;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 19:56
 */
public class LocalTransactionCompleteHandler implements ProtocolHandler<LocalTransactionInputCompletePacket> {
    @Override
    public void handler(ConnectionContext connectionContext, int messageId, int code, LocalTransactionInputCompletePacket content) {

    }
}
