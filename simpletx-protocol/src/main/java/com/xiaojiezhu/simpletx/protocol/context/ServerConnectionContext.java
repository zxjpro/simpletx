package com.xiaojiezhu.simpletx.protocol.context;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:06
 */
public interface ServerConnectionContext extends ConnectionContext {

    String getAppName();

    String getAppid();

}
