package com.xiaojiezhu.simpletx.protocol.server.event;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:00
 */
public interface ConnectionEventListener {


    /**
     * 连接被激活时
     */
    void onChannelActive(ConnectionContext connectionContext);

    /**
     * 断开连接时
     * @param connectionContext
     */
    void onChannelDisconnect(ConnectionContext connectionContext);

    void onChannelError(ConnectionContext connectionContext , Throwable throwable);
}
