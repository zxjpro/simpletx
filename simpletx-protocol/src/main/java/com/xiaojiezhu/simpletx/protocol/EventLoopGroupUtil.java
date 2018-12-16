package com.xiaojiezhu.simpletx.protocol;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 18:11
 */
public class EventLoopGroupUtil {

    public static EventLoopGroup create(){
        try {
            EpollEventLoopGroup group = new EpollEventLoopGroup();
            return group;
        } catch (Throwable e) {
            NioEventLoopGroup group = new NioEventLoopGroup();
            return group;
        }
    }

    public static EventLoopGroup create(int threadSize){
        try {
            EpollEventLoopGroup group = new EpollEventLoopGroup(threadSize);
            return group;
        } catch (Throwable e) {
            NioEventLoopGroup group = new NioEventLoopGroup(threadSize);
            return group;
        }
    }



}
