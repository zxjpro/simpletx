package com.xiaojiezhu.simpletx.server.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 12:47
 */
@Getter
@Setter
public class SimpletxConfig {

    private String host = "0.0.0.0";

    private int port = 10290;

    private int workerThreadSize = Runtime.getRuntime().availableProcessors() * 4;

    private String password;
}
