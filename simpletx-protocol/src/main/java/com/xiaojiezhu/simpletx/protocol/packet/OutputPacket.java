package com.xiaojiezhu.simpletx.protocol.packet;


/**
 * @author xiaojie.zhu
 * time 2018/12/22 23:31
 */
public interface OutputPacket extends Packet {


    void write(ByteBuffer byteBuf);

}
