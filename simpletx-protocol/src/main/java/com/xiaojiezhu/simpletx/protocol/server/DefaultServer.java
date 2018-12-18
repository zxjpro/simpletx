package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.EventLoopGroupUtil;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 21:14
 */
public class DefaultServer implements Server{

    private String host;
    private int port;

    @Getter
    private int workerThreadSize = -1;

    private ProtocolDispatcher protocolDispatcher;

    private ServerContext serverContext;

    private ConnectionEventListener connectionEventListener;

    private boolean started;

    public DefaultServer(int port , ProtocolDispatcher protocolDispatcher) {
        this("0.0.0.0" , port , protocolDispatcher);
    }

    public DefaultServer(String host, int port , ProtocolDispatcher protocolDispatcher) {
        this.host = host;
        this.port = port;
        this.protocolDispatcher = protocolDispatcher;

        this.serverContext = new DefaultServerContext();
    }

    @Override
    public ServerContext getServerContext() {
        return this.serverContext;
    }

    @Override
    public void start() throws InterruptedException {
        if(this.started){
            throw new RuntimeException("this server is running");
        }
        this.started = true;

        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = EventLoopGroupUtil.create(1);
        EventLoopGroup workerGroup = null;
        if(this.workerThreadSize == -1){
            workerGroup = EventLoopGroupUtil.create();
        }else{
            workerGroup = EventLoopGroupUtil.create(workerThreadSize);
        }

        bootstrap.group(bossGroup , workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(EpollChannelOption.TCP_CORK, true);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        ServerChannelInitializer serverChannelInitializer = new ServerChannelInitializer(this.protocolDispatcher, this.serverContext);
        serverChannelInitializer.setConnectionEventListener(this.connectionEventListener);
        bootstrap.childHandler(serverChannelInitializer);

        try {
            ChannelFuture future = bootstrap.bind(host, port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void register(ConnectionEventListener connectionEventListener) {
        this.check();
        this.connectionEventListener = connectionEventListener;
    }

    public void setWorkerThreadSize(int workerThreadSize) {
        this.check();
        this.workerThreadSize = workerThreadSize;
    }

    @Override
    public boolean isRunning() {
        return this.started;
    }

    private void check(){
        if(isRunning()){
            throw new RuntimeException("this server is running");
        }
    }


}
