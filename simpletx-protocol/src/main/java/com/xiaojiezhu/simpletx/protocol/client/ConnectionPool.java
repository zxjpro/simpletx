package com.xiaojiezhu.simpletx.protocol.client;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 18:31
 */
public interface ConnectionPool {


    Connection getConnection() throws IOException;

    /**
     * 当前激活数量
     * @return
     */
    int getActive();

    /**
     * 最大可激活数量
     * @return
     */
    int getMaxActive();

}
