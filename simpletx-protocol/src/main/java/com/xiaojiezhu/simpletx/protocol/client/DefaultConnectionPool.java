package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.EventLoopGroupUtil;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.DefaultConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 18:33
 */
public class DefaultConnectionPool implements ConnectionPool {
    private static final long SLOW_TIME = 1000;
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    @Getter
    @Setter
    private ProtocolDispatcher protocolDispatcher;

    @Getter
    @Setter
    private String host;

    @Getter
    @Setter
    private int port;

    @Setter
    private String password;

    @Setter
    @Getter
    private int maxActive = 10;

    private FixedChannelPool pool;

    private SimpleChannelPoolHandler poolHandler;

    private ConnectionContextHolder connectionContextHolder;

    @Override
    public Connection getConnection() throws IOException {
        if(this.pool == null){
            synchronized (this){
                if(this.pool == null){
                    this.init();
                }
            }
        }
        long startTime = System.currentTimeMillis();
        Future<Channel> future = pool.acquire();
        Channel channel;
        try {
            channel = future.get();
        } catch (Exception e) {
            throw new IOException("get simpletx-server connection fail" , e);
        }
        ReuseConnection connection = new ReuseConnection(this.pool , channel);
        long endTime = System.currentTimeMillis();

        long useTime = endTime - startTime;
        if(useTime > SLOW_TIME){
            LOG.warn("get simpletx-server connection slow , use time : " + useTime);
        }
        return connection;
    }

    @Override
    public int getActive() {
        if (this.poolHandler == null) {
            return 0;
        }
        return this.poolHandler.getActive();
    }

    private void init(){
        this.check();

        this.connectionContextHolder = new DefaultConnectionContextHolder();

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = EventLoopGroupUtil.create();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.remoteAddress(this.host , this.port);

        this.poolHandler = new SimpleChannelPoolHandler(this.protocolDispatcher , this.connectionContextHolder);
        pool = new FixedChannelPool(bootstrap , poolHandler , this.maxActive);

    }

    private void check(){
        if(StringUtils.isEmpty(this.host)){
            throw new NullPointerException("host not set");
        }
        if(this.port == 0){
            throw new NullPointerException("port not set");
        }
        if(StringUtils.isEmpty(this.password)){
            throw new NullPointerException("password not set");
        }
        if(this.maxActive <= 0){
            throw new IllegalArgumentException("macActive must > 0");
        }
        if(this.protocolDispatcher == null){
            throw new NullPointerException("the protocol dispatcher is not set");
        }
    }


}
