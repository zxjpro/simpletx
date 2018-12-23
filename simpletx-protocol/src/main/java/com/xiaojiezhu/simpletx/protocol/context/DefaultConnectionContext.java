package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.client.Connection;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:33
 */
public class DefaultConnectionContext implements ConnectionContext {

    private final ConcurrentHashMap<Object , Object> session = new ConcurrentHashMap<>();

    private final Channel channel;

    /**
     * this object create time , is also connect time
     */
    private long createTime;

    private String remoteIpAddress;

    private boolean authorization;

    /**
     * channel id
     */
    private int id = -1;

    public DefaultConnectionContext(Channel channel) {
        this.channel = channel;
        this.createTime = System.currentTimeMillis();

        this.remoteIpAddress = ((InetSocketAddress)channel.remoteAddress()).getAddress().toString();
    }

    @Override
    public long connectTime() {
        return this.createTime;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public Object get(Object key) {
        return this.session.get(key);
    }

    @Override
    public void set(Object key, Object value) {
        this.session.put(key , value);
    }

    @Override
    public void sendMessage(Message message) {
        this.channel.writeAndFlush(message);
    }

    @Override
    public void sendMessage(MessageCreator messageCreator) {
        ByteBuf buffer = this.channel.alloc().buffer();
        Message message = messageCreator.create(buffer);
        this.sendMessage(message);
    }

    @Override
    public String remoteIpAddress() {
        return this.remoteIpAddress;
    }

    @Override
    public void close() {
        this.channel.close();
    }

    @Override
    public boolean isAuthorization() {
        if(this.authorization){
            return true;
        }else{
            if(null != get(Constant.Server.ConnectionSession.LOGIN_SUCCESS)){
                this.authorization = true;
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public int getId() {
        if(this.id == -1){
            Object id = this.get(Constant.Server.ConnectionSession.ID);
            if(id == null){
                throw new RuntimeException("id not init");
            }
            this.id = Integer.parseInt(String.valueOf(id));
        }
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.channel.isActive();
    }


}
