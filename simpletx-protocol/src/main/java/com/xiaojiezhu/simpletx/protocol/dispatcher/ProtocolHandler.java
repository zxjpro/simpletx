package com.xiaojiezhu.simpletx.protocol.dispatcher;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.packet.Packet;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 22:54
 */
public interface ProtocolHandler<T extends Packet> {

    /**
     * handler protocol
     * @param connectionContext connection context
     * @param content protocol body
     */
    void handler(ConnectionContext connectionContext , T content);
}
