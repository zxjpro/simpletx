package com.xiaojiezhu.simpletx.protocol.server;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 23:25
 */
public interface Server {

    ServerContext getServerContext();

    void start() throws Exception;
}
