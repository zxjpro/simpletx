package com.xiaojiezhu.simpletx.protocol.dispatcher;

import com.xiaojiezhu.simpletx.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 22:58
 */
public class DefaultProtocolDispatcher implements ProtocolDispatcher{
    private final ConcurrentHashMap<Integer , ProtocolHandler> HANDLERS = new ConcurrentHashMap<>();

    @Override
    public ProtocolHandler dispatcher(int code) {
        ProtocolHandler protocolHandler = HANDLERS.get(code);
        if(protocolHandler == null){
            throw new NullPointerException(StringUtils.str("not found code " , code , " protocol handler"));
        }

        return protocolHandler;
    }

    @Override
    public void register(int code, ProtocolHandler protocolHandler) {
        if(HANDLERS.get(code) != null){
            throw new RuntimeException(StringUtils.str("exists code ", code , " protocol handler"));
        }

        HANDLERS.put(code , protocolHandler);
    }
}
