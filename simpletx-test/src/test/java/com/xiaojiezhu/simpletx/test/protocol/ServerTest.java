package com.xiaojiezhu.simpletx.test.protocol;

import com.xiaojiezhu.simpletx.protocol.server.Server;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:37
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(8888);
        server.start();
    }
}
