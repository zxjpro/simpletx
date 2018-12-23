package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.DefaultConnectionContextHolder;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 23:43
 */
public class DefaultSimpletxContext implements SimpletxContext {

    private final ConnectionContextHolder connectionContextHolder = new DefaultConnectionContextHolder();

    @Override
    public ConnectionContextHolder getConnectionContextHolder() {
        return this.connectionContextHolder;
    }
}
