package com.xiaojiezhu.simpletx.protocol.common;

import com.xiaojiezhu.simpletx.protocol.client.FutureContainer;
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
import lombok.AllArgsConstructor;

import java.util.concurrent.Executor;

/**
 * @author xiaojie.zhu
 * time 2018-12-29 22:48
 */
@AllArgsConstructor
public abstract class AbstractProtocolChannelHandler extends SimpleChannelInboundHandler<Message> {
    private final ProtocolDispatcher protocolDispatcher;
    private final InputPacketManager inputPacketManager;
    protected final Executor executor;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        ProtocolHandler protocolHandler = this.protocolDispatcher.dispatcher(msg.getHeader().getCode());

        Class<? extends InputPacket> inputPacketClass = this.inputPacketManager.get(protocolHandler.getClass());

        InputPacket inputPacket = inputPacketClass.newInstance();

        ByteBuffer byteBuf = new ByteBuffer(Unpooled.wrappedBuffer(msg.getBody()));
        try {
            inputPacket.read(byteBuf);
        } finally {
            byteBuf.release();
        }


        this.channelRead1(ctx , protocolHandler , msg.getHeader() , inputPacket);
    }

    /**
     * 处理接收对方的返回数据，执行回调，这个起因是先向对方发送了消息，然后再接收对方的响应
     * @param futureContainer
     * @param inputPacket
     */
    protected void invokeCallback(FutureContainer futureContainer , ResponseInputPacket inputPacket){
        int responseMessageId = inputPacket.getResponseMessageId();
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
    }

    protected abstract void channelRead1(ChannelHandlerContext ctx, ProtocolHandler dispatcher, Header header , InputPacket inputPacket);
}
