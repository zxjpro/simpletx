package com.xiaojiezhu.simpletx.client.net.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 00:09
 */
public class LoginInputPacket implements InputPacket {

    @Getter
    private int connectionId;
    @Getter
    private String authKey;

    @Override
    public void read(ByteBuf buffer) {
        this.connectionId = buffer.readMedium();
        byte[] buf = new byte[buffer.readableBytes()];
        buffer.readBytes(buf);

        this.authKey = new String(buf);
    }
}
