package com.xiaojiezhu.simpletx.server.config;

import com.xiaojiezhu.simpletx.util.Constant;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 12:47
 */
@Getter
@Setter
public class SimpletxConfig {

    public SimpletxConfig() {
        System.setProperty(Constant.AUTHOR , Constant.AUTHOR_INFO);
    }

    private String host = "0.0.0.0";

    private int port = 10290;

    private int workerThreadSize = Runtime.getRuntime().availableProcessors() * 4;

    private String password;
}
