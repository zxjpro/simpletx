package com.xiaojiezhu.simpletx.server.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 23:52
 */
public class AuthorizationInputPacket implements InputPacket {

    @Getter
    private String appName;
    @Getter
    private String appid;

    @Getter
    private String password;

    @Override
    public void read(ByteBuffer byteBuf) {
        this.appName = byteBuf.readStringEndZero();
        this.appid = byteBuf.readStringEndZero();
        this.password = new String(byteBuf.readBytesToLast());
    }

}
