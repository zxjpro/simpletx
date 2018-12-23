package com.xiaojiezhu.simpletx.client.util;

import com.xiaojiezhu.simpletx.client.configuration.SimpletxServerProperties;
import com.xiaojiezhu.simpletx.client.net.LoginHandler;
import com.xiaojiezhu.simpletx.client.net.OkErrorHandler;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.DefaultProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.util.Constant;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 11:01
 */
@AllArgsConstructor
public class DispatcherHelper {

    private String appName;
    private String appid;
    private SimpletxContext simpletxContext;
    private SimpletxServerProperties properties;

    public ProtocolDispatcher createProtocolDispatcher() {
        ProtocolDispatcher protocolDispatcher = new DefaultProtocolDispatcher();

        //random auth key
        protocolDispatcher.register(Constant.Server.ProtocolCode.CODE_AUTH_KEY, new LoginHandler(this.appName, this.appid, properties.getPassword()));

        protocolDispatcher.register(Constant.Server.ProtocolCode.CODE_OK , new OkErrorHandler(this.simpletxContext));

        return protocolDispatcher;
    }
}
