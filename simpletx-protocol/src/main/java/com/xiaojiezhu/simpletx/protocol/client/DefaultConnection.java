package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ClientConnectionContext;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 19:02
 */
class DefaultConnection extends ClientConnectionContext implements Connection {


    public DefaultConnection(Channel channel) {
        super(channel);
    }

}
