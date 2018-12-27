package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.MessageSender;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;

import java.io.Closeable;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 18:07
 */
public interface Connection extends MessageSender , Closeable {

    boolean isActive();

}
