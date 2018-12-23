package com.xiaojiezhu.simpletx.server.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 23:52
 */
public class AuthorizationInputPacket implements InputPacket {

    @Getter
    private String password;

    @Override
    public void read(ByteBuf byteBuf) {
        byte[] buf = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buf);

        this.password = new String(buf);
    }

}
