package com.xiaojiezhu.simpletx.client.net;

import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;
import com.xiaojiezhu.simpletx.util.Constant;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 23:36
 */
@AllArgsConstructor
public class OkErrorHandler implements ProtocolHandler<OkErrorPacket> {

    private final SimpletxContext simpletxContext;

    @Override
    public void handler(ConnectionContext connectionContext, int msgId, int code, OkErrorPacket content) {

/*        boolean ok = false;
        if(Constant.Server.ProtocolCode.CODE_OK == code){
            ok = true;
        }else if(Constant.Server.ProtocolCode.CODE_ERROR == code){
            ok = false;
        }else{
            throw new RuntimeException("not support code:" + code);
        }*/


    }
}
