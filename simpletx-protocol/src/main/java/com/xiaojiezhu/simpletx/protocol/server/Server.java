package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:25
 */
public interface Server {

    ServerContext getServerContext();

    void start() throws Exception;

    /**
     * register connection event listener
     * @param connectionEventListener
     */
    void register(ConnectionEventListener connectionEventListener);

    boolean isRunning();
}
