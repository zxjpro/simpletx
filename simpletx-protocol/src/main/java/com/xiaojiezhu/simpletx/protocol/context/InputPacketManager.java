package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 10:43
 */
public interface InputPacketManager {

    void register(Class<?> classes , Class<? extends InputPacket> packet);

    Class<? extends InputPacket> get(Class<?> classes);
}
