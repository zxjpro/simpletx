package com.xiaojiezhu.simpletx.server.dispatcher;

import com.xiaojiezhu.simpletx.protocol.dispatcher.DefaultProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.server.config.SimpletxConfig;
import com.xiaojiezhu.simpletx.util.Constant;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:56
 */
@AllArgsConstructor
public class DispatcherHelper {

    private final SimpletxConfig simpletxConfig;


    public ProtocolDispatcher createProtocolDispatcher() {
        ProtocolDispatcher protocolDispatcher = new DefaultProtocolDispatcher();

        //authorization
        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_LOGIN, new AuthorizationHandler(simpletxConfig.getPassword()));

        return protocolDispatcher;
    }
}
