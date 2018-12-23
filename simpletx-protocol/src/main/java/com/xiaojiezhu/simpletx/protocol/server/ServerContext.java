package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 10:51
 */
public interface ServerContext extends ConnectionContextHolder {

    void disconnect(int id);

    ServerConnectionContext find(int id);

    List<ServerConnectionContext> findConnectionByAppName(String appName);

    ServerConnectionContext findConnectionByAppNameAndAppid(String appName , String appid);

}
