package com.xiaojiezhu.simpletx.client.net.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
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
    public void read(ByteBuffer buffer) {
        this.connectionId = buffer.readMedium();
        this.authKey = new String(buffer.readBytesToLast());
    }
}
