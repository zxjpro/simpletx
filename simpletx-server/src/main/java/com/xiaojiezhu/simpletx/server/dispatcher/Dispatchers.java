package com.xiaojiezhu.simpletx.server.dispatcher;

import com.xiaojiezhu.simpletx.protocol.dispatcher.DefaultProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:56
 */
public class Dispatchers {

    public static ProtocolDispatcher createProtocolDispatcher(){
        ProtocolDispatcher protocolDispatcher = new DefaultProtocolDispatcher();

        //authorization
        protocolDispatcher.register(100 , new AuthorizationHandler());

        return protocolDispatcher;
    }
}
