package com.xiaojiezhu.simpletx.server;

import com.xiaojiezhu.simpletx.protocol.server.DefaultServer;
import com.xiaojiezhu.simpletx.protocol.server.Server;
import com.xiaojiezhu.simpletx.server.dispatcher.Dispatchers;
import com.xiaojiezhu.simpletx.server.listener.ConnectionStatusListener;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:47
 */
public class SimpletxSerer {

    public static void main(String[] args) throws InterruptedException {
        String host = "0.0.0.0";
        int port = 10290;
        int workerThreadSize = 24;

        DefaultServer server = new DefaultServer(host , port , Dispatchers.createProtocolDispatcher());
        server.setWorkerThreadSize(workerThreadSize);
        server.register(new ConnectionStatusListener());
        server.start();
    }
}
