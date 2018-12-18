package com.xiaojiezhu.simpletx.test.protocol;

import com.alibaba.fastjson.JSON;
import com.xiaojiezhu.simpletx.protocol.client.Connection;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.test.coec.Person;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:40
 */
public class ClientTest {

    public static void main(String[] args) throws IOException {
        DefaultConnectionPool pool = new DefaultConnectionPool();
        pool.setHost("localhost");
        pool.setPort(8888);
        pool.setPassword("hello");
        pool.setMaxActive(3);


        for (int i = 0; i < 13; i++) {
            Connection connection = pool.getConnection();
            Message message = createMessage(i);

            connection.sendMessage(message);

            connection.close();
        }


    }

    private static Message createMessage(int id) throws UnsupportedEncodingException {
        Person p = new Person();
        //p.setParent(Arrays.asList("李四","王五"));
        p.setName("张三");
        p.setHeight(2.22);
        p.setId(id);

        String s = JSON.toJSONString(p);

        byte[] bytes = s.getBytes("UTF-8");

        Message message = new Message();
        Header header = new Header();
        header.setBodyLength(bytes.length);
        header.setId(1995);
        header.setCode(1029);
        message.setHeader(header);
        message.setBody(bytes);
        return message;
    }
}
