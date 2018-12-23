package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.codec.MessageDecoder;
import com.xiaojiezhu.simpletx.protocol.codec.MessageEncoder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.context.SimpleInputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 21:21
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ProtocolDispatcher protocolDispatcher;

    private final ServerContext serverContext;

    private ConnectionEventListener connectionEventListener;

    private final InputPacketManager inputPacketManager = new SimpleInputPacketManager();

    public ServerChannelInitializer(ProtocolDispatcher protocolDispatcher , ServerContext serverContext) {
        this.protocolDispatcher = protocolDispatcher;
        this.serverContext = serverContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MessageDecoder());

        ServerChannelHandler serverChannelHandler = new ServerChannelHandler(this.protocolDispatcher, this.serverContext , this.inputPacketManager);
        serverChannelHandler.setConnectionEventListener(this.connectionEventListener);
        pipeline.addLast(serverChannelHandler);

        pipeline.addLast(new MessageEncoder());

    }


    public void setConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionEventListener = connectionEventListener;
    }
}
