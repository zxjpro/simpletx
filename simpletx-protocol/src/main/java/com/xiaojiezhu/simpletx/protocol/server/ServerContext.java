package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;

import java.nio.channels.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 10:51
 */
public interface ServerContext extends ConnectionContextHolder {

    void disconnect(int id);
}
