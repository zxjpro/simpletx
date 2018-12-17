package com.xiaojiezhu.simpletx.protocol.dispatcher;

/**
 * @author xiaojie.zhu
 * time 2018/12/17 22:50
 */
public interface ProtocolDispatcher {

    /**
     * dispatcher handler
     * @param code protocol code
     * @return handler
     */
    ProtocolHandler dispatcher(int code);

    /**
     * register protocol handler
     * @param code protocol code
     * @param protocolHandler protocol handler
     */
    void register(int code , ProtocolHandler protocolHandler);

}
