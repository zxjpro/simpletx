package com.xiaojiezhu.simpletx.protocol.packet;


/**
 * @author xiaojie.zhu
 * time 2018/12/22 23:23
 */
public interface InputPacket extends Packet {


    void read(ByteBuffer byteBuf);


}
