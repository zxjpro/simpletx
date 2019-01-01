package com.xiaojiezhu.simpletx.protocol.dispatcher;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.packet.Packet;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 23:54
 */
public class SimpleProtocolHandler<T extends Packet> implements ProtocolHandler<T> {



    @Override
    public void handler(ConnectionContext connectionContext, int messageId, int code, T content) {

    }
}
