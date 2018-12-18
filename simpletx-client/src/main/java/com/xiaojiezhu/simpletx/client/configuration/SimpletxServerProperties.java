package com.xiaojiezhu.simpletx.client.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:59
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "simpletx.server")
public class SimpletxServerProperties {

    private String host;

    private int port;

    private String password;

    private int maxActive;


}
