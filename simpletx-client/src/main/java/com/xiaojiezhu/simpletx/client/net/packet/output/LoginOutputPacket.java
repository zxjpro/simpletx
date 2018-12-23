package com.xiaojiezhu.simpletx.client.net.packet.output;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 14:59
 */
@AllArgsConstructor
public class LoginOutputPacket implements OutputPacket {

    private String appName;
    private String appid;

    private String password;

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeStringEndZero(this.appName);
        byteBuf.writeStringEndZero(this.appid);
        byteBuf.writeBytes(password.getBytes());



    }
}
