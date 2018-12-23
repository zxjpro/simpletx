package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.codec.MessageDecoder;
import com.xiaojiezhu.simpletx.protocol.codec.MessageEncoder;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.context.SimpleInputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author xiaojie.zhu
 * time 2018/12/16 18:23
 */
public class SimpleChannelPoolHandler implements ChannelPoolHandler {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final AtomicLong COUNT = new AtomicLong(0);

    private final ProtocolDispatcher protocolDispatcher;
    private final SimpletxContext simpletxContext;

    private final InputPacketManager inputPacketManager = new SimpleInputPacketManager();

    public SimpleChannelPoolHandler(ProtocolDispatcher protocolDispatcher , SimpletxContext simpletxContext) {
        this.protocolDispatcher = protocolDispatcher;
        this.simpletxContext = simpletxContext;
    }

    @Override
    public void channelReleased(Channel channel) throws Exception {
        LOG.debug(StringUtils.str("channel release , id : " , channel.id()));
        this.COUNT.decrementAndGet();
    }

    @Override
    public void channelAcquired(Channel channel) throws Exception {
        LOG.debug(StringUtils.str("channel be acquired , id : " , channel.id()));
        this.COUNT.incrementAndGet();
    }

    @Override
    public void channelCreated(Channel channel) throws Exception {
        LOG.debug(StringUtils.str("channel is created , id : " + channel.id()));

        SocketChannel sc = (SocketChannel) channel;
        sc.config().setKeepAlive(true);
        sc.config().setTcpNoDelay(true);
        ChannelPipeline pipeline = sc.pipeline();

        pipeline.addLast(new MessageDecoder());

        pipeline.addLast(new ClientChannelHandler(this.protocolDispatcher , this.simpletxContext , this.inputPacketManager));

        pipeline.addLast(new MessageEncoder());


        this.COUNT.incrementAndGet();
    }

    public int getActive(){
        return (int) this.COUNT.get();
    }
}
