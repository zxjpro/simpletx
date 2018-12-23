package com.xiaojiezhu.simpletx.protocol.message;


import io.netty.buffer.ByteBuf;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 11:34
 */
@FunctionalInterface
public interface MessageCreator {

    Message create(ByteBuf buffer);

}
