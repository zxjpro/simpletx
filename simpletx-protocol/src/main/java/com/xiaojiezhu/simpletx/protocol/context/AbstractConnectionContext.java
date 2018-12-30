package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.SimpleMessageSender;
import io.netty.channel.Channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 12:38
 */
public abstract class AbstractConnectionContext extends SimpleMessageSender implements ConnectionContext {

    /**
     * this object create time , is also connect time
     */
    private long createTime;

    private String remoteIpAddress;


    private final ConcurrentHashMap<Object , Object> session = new ConcurrentHashMap<>();

    public AbstractConnectionContext(Channel channel) {
        super(channel);

        this.createTime = System.currentTimeMillis();
        this.remoteIpAddress = ((InetSocketAddress)channel.remoteAddress()).getAddress().toString();

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
    public long connectTime() {
        return this.createTime;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public String remoteIpAddress() {
        return this.remoteIpAddress;
    }


    @Override
    public void close()throws IOException {
        this.channel.close();
    }

    @Override
    public boolean isActive() {
        return this.channel.isActive();
    }
}
