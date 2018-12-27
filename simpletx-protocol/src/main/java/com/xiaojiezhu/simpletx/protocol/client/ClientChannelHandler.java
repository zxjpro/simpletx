package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import com.xiaojiezhu.simpletx.protocol.future.FutureListener;
import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.packet.ResponseInputPacket;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:34
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<Message> {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ProtocolDispatcher protocolDispatcher;
    private final SimpletxContext simpletxContext;

    private final InputPacketManager inputPacketManager;

    private final Executor executor;

    private final ConnectionContextHolder connectionContextHolder;

    private boolean auth = false;

    public ClientChannelHandler(ProtocolDispatcher protocolDispatcher, SimpletxContext simpletxContext) {
        this.protocolDispatcher = protocolDispatcher;
        this.simpletxContext = simpletxContext;
        this.inputPacketManager = simpletxContext.getInputPacketManager();
        this.executor = simpletxContext.getExecutor();
        this.connectionContextHolder = this.simpletxContext.getConnectionContextHolder();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        ProtocolHandler dispatcher = this.protocolDispatcher.dispatcher(msg.getHeader().getCode());

        Class<? extends InputPacket> inputPacketClass = this.inputPacketManager.get(dispatcher.getClass());

        InputPacket inputPacket = inputPacketClass.newInstance();

        ByteBuffer byteBuf = new ByteBuffer(Unpooled.wrappedBuffer(msg.getBody()));
        try {
            inputPacket.read(byteBuf);
        } finally {
            byteBuf.release();
        }

        if(inputPacket instanceof ResponseInputPacket){
            int responseMessageId = ((ResponseInputPacket) inputPacket).getResponseMessageId();
            FutureContainer futureContainer = this.simpletxContext.getFutureContainer();
            FutureCondition futureCondition = futureContainer.find(responseMessageId);
            if(futureCondition == null){
                throw new NullPointerException("not found futureCondition , id : "  + responseMessageId);
            }
            futureContainer.remove(responseMessageId);
            futureCondition.setValue(inputPacket);
            futureCondition.signalAll();

            final FutureListener futureListener = futureCondition.getFutureListener();
            if(futureListener != null){
                this.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        futureListener.complete(futureCondition.getFuture());
                    }
                });
            }

        }else{
            Header header = msg.getHeader();

            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    dispatcher.handler(connectionContextHolder.getConnectionContext(ctx.channel()), header.getId() , header.getCode() , inputPacket);
                }
            });

        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //this.connectionContextHolder.register(ctx.channel());
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
