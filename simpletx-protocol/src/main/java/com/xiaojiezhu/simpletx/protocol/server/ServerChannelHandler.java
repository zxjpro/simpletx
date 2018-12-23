package com.xiaojiezhu.simpletx.protocol.server;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.server.event.ConnectionEventListener;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


        ByteBuffer byteBuf = new ByteBuffer(Unpooled.wrappedBuffer(msg.getBody()));
        try {
            inputPacket.read(byteBuf);
        } finally {
            byteBuf.release();
        }

        Header header = msg.getHeader();

        try {
            dispatcher.handler(connectionContext , header.getId() , header.getCode() , inputPacket);
        } catch (Throwable e) {
            connectionContext.sendMessage(new MessageCreator() {
                @Override
                public Message create(ByteBuf buffer) {
                    return MessageUtil.createErrorMessage(header.getId() , e.getMessage() , buffer);
                }
            });

            LOG.error(dispatcher.getClass() + " handler error" , e);
        }
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
