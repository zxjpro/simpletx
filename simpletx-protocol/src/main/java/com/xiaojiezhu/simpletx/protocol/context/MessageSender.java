package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 12:21
 */
public interface MessageSender {

    /**
     * send message
     * @param message
     */
    void sendMessage(Message message);

    /**
     * send message by message creator , it contains a byte buffer
     * @param messageCreator
     */
    void sendMessage(MessageCreator messageCreator);
}
