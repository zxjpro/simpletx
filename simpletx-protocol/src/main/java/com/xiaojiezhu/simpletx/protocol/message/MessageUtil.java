package com.xiaojiezhu.simpletx.protocol.message;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:26
 */
public class MessageUtil {


    public static Message createMessage(int id , int code , byte[] bytes){
        Header header = new Header(id , code , bytes.length);
        Message message = new Message(header , bytes);
        return message;
    }
}
