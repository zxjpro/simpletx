package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.exception.SyntaxRuntimeException;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:31
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<Message> {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ProtocolDispatcher protocolDispatcher;
    private final ServerContext serverContext;

    private ConnectionEventListener connectionEventListener;

    private final InputPacketManager inputPacketManager;


    private boolean first = true;


    public ServerChannelHandler(ProtocolDispatcher protocolDispatcher , ServerContext serverContext , InputPacketManager inputPacketManager) {
        this.protocolDispatcher = protocolDispatcher;
        this.serverContext = serverContext;
        this.inputPacketManager = inputPacketManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        ProtocolHandler dispatcher = this.protocolDispatcher.dispatcher(msg.getHeader().getCode());

        ConnectionContext connectionContext = serverContext.getConnectionContext(ctx.channel());
        if(!first){
            if(!connectionContext.isAuthorization()){
                connectionContext.close();
                throw new RuntimeException("not authorization exception");
            }
        }

        Class<? extends InputPacket> inputPacketClass = this.inputPacketManager.get(dispatcher.getClass());
        InputPacket inputPacket = inputPacketClass.newInstance();


        ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.getBody());
        try {
            inputPacket.read(byteBuf);
        } finally {
            byteBuf.release();
        }

        dispatcher.handler(connectionContext , inputPacket);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info(StringUtils.str("channel[ " , ctx.channel().id() + " ] is connected"));
        this.serverContext.register(ctx.channel());

        if(this.connectionEventListener != null){
            this.connectionEventListener.onChannelActive(this.serverContext.getConnectionContext(ctx.channel()));
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            if(this.connectionEventListener != null){
                this.connectionEventListener.onChannelDisconnect(this.serverContext.getConnectionContext(ctx.channel()));
            }
        } finally {

            LOG.info(StringUtils.str("channel[ " , ctx.channel().id() + " ] is disconnected"));
            this.serverContext.remove(ctx.channel());
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("channel execute error" , cause);

        if(this.connectionEventListener != null){
            this.connectionEventListener.onChannelError(this.serverContext.getConnectionContext(ctx.channel()) , cause);
        }
    }


    public void setConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionEventListener = connectionEventListener;
    }
}
