package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;

/**
 * client context
 * @author xiaojie.zhu
 * time 2018/12/23 23:42
 */
public interface SimpletxContext {

    ConnectionContextHolder getConnectionContextHolder();
}
