package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.exception.SyntaxRuntimeException;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:34
 */
@AllArgsConstructor
public class ClientChannelHandler extends SimpleChannelInboundHandler<Message> {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ProtocolDispatcher protocolDispatcher;
    private final ConnectionContextHolder connectionContextHolder;

    private final InputPacketManager inputPacketManager;



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        ProtocolHandler dispatcher = this.protocolDispatcher.dispatcher(msg.getHeader().getCode());

        Class<? extends InputPacket> inputPacketClass = this.inputPacketManager.get(dispatcher.getClass());

        InputPacket inputPacket = inputPacketClass.newInstance();

        ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.getBody());
        try {
            inputPacket.read(byteBuf);
        } finally {
            byteBuf.release();
        }

        dispatcher.handler(this.connectionContextHolder.getConnectionContext(ctx.channel()) , inputPacket);
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.connectionContextHolder.register(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.connectionContextHolder.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("channel invoke error", cause);

        ctx.channel().close();
    }
}
