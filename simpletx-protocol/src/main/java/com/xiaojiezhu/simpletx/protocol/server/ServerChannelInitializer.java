package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.codec.MessageDecoder;
import com.xiaojiezhu.simpletx.protocol.codec.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 21:21
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MessageDecoder());

        pipeline.addLast(new ServerChannelHandler());

        pipeline.addLast(new MessageEncoder());

    }
}