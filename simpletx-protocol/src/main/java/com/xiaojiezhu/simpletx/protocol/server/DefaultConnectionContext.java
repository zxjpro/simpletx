package com.xiaojiezhu.simpletx.protocol.server;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:33
 */
class DefaultConnectionContext implements ConnectionContext {

    private final ConcurrentHashMap<Object , Object> session = new ConcurrentHashMap<>();

    private final Channel channel;

    /**
     * this object create time , is also connect time
     */
    private long createTime;

    public DefaultConnectionContext(Channel channel) {
        this.channel = channel;
        this.createTime = System.currentTimeMillis();
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


}
