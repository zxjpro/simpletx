package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.common.AbstractProtocolChannelHandler;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Header;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.packet.ResponseInputPacket;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:34
 */
public class ClientChannelHandler extends AbstractProtocolChannelHandler {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final SimpletxContext simpletxContext;

    private final ConnectionContextHolder connectionContextHolder;

    private boolean auth = false;

    public ClientChannelHandler(ProtocolDispatcher protocolDispatcher, SimpletxContext simpletxContext) {
        super(protocolDispatcher , simpletxContext.getInputPacketManager() , simpletxContext.getExecutor());
        this.simpletxContext = simpletxContext;
        this.connectionContextHolder = this.simpletxContext.getConnectionContextHolder();
    }


    @Override
    protected void channelRead1(ChannelHandlerContext ctx, ProtocolHandler protocolHandler ,  Header header, InputPacket inputPacket) {
        if(inputPacket instanceof ResponseInputPacket){
            super.invokeCallback(simpletxContext.getFutureContainer() , (ResponseInputPacket) inputPacket);

        }else{

            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    protocolHandler.handler(connectionContextHolder.getConnectionContext(ctx.channel()), header.getId() , header.getCode() , inputPacket);
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
