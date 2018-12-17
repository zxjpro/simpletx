package com.xiaojiezhu.simpletx.protocol.dispatcher;

import com.xiaojiezhu.simpletx.protocol.server.ConnectionContext;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 22:54
 */
public interface ProtocolHandler {

    /**
     * handler protocol
     * @param connectionContext connection context
     * @param content protocol body byte array
     */
    void handler(ConnectionContext connectionContext , byte[] content);
}
