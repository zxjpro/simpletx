package com.xiaojiezhu.simpletx.server.config;

import com.xiaojiezhu.simpletx.util.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Condition;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 12:47
 */
@Getter
@Setter
public class SimpletxConfig {

    public SimpletxConfig() {
        System.setProperty(Constant.AUTHOR, Constant.AUTHOR_INFO);
        System.setProperty(Constant.SIMPLETX_SERVER , String.valueOf(true));
    }

    private String password;

    private String host = "0.0.0.0";

    private int port = 10290;

    /**
     * 事务在server的超时时间
     */
    private long expireTime = 30000;

    /**
     * netty的worker线程数量
     */
    private int workerThreadSize = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * 具体处理逻辑的默认线程数量
     */
    private int logicThreadSize = Runtime.getRuntime().availableProcessors() * 8;

}
