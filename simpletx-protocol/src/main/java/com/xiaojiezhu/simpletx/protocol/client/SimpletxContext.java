package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;

import java.util.concurrent.Executor;

/**
 * client context
 * @author xiaojie.zhu
 * time 2018/12/23 23:42
 */
public interface SimpletxContext {

    ConnectionContextHolder getConnectionContextHolder();

    FutureContainer getFutureContainer();

    InputPacketManager getInputPacketManager();

    /**
     * get the thread executor
     * @return
     */
    Executor getExecutor();
}
