package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.EventLoopGroupUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.function.BinaryOperator;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 21:14
 */
public class Server {

    private String host;
    private int port;

    public Server(int port) {
        this("0.0.0.0" , port);
    }

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = EventLoopGroupUtil.create(1);
        //TODO: worker 的线程数量
        EventLoopGroup workerGroup = EventLoopGroupUtil.create();

        bootstrap.group(bossGroup , workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(EpollChannelOption.TCP_CORK, true);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.childHandler(new ServerChannelInitializer());

        try {
            ChannelFuture future = bootstrap.bind(host, port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
