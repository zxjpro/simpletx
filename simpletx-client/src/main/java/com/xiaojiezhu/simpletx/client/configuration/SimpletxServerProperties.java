package com.xiaojiezhu.simpletx.client.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:59
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "simpletx.server")
public class SimpletxServerProperties implements Serializable {

    private String host;

    private int port;

    private String password;

    /**
     * connect simpletx server max active
     */
    private int maxActive;

    /**
     * Serializable type \n
     * jdk use default jdk serializable \n
     * json use fastjson serializable \n
     * kryo use kryo serializable \n
     */
    private String codec = "kryo";

    /**
     * 从最佳性能来说，分配的线程数量，等于最高并发数量即可，默认值cpu core * 2
     */
    private int threadSize = Runtime.getRuntime().availableProcessors() * 2;


}
