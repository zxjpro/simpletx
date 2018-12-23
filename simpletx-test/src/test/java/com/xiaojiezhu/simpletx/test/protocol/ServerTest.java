package com.xiaojiezhu.simpletx.test.protocol;

import com.xiaojiezhu.simpletx.protocol.server.DefaultServer;
import com.xiaojiezhu.simpletx.protocol.server.Server;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:37
 */
public class ServerTest {

    public static void main(String[] args) throws Exception {
        Server server = new DefaultServer(8888 );

        server.start();
    }
}
