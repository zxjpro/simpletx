package com.xiaojiezhu.simpletx.protocol.packet;

/**
 * 接收自来对方的响应消息，这种消息来自于自身向对方发过请求的消息
 * @author xiaojie.zhu
 * time 2018/12/25 23:07
 */
public interface ResponseInputPacket extends InputPacket {

    /**
     * 获取自身向对方发送的请求消息id
     * @return
     */
    int getResponseMessageId();
}
