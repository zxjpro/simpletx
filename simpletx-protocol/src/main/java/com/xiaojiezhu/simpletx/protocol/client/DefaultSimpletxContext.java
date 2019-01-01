package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.DefaultConnectionContextHolder;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 23:43
 */
public class DefaultSimpletxContext implements SimpletxContext {


    @Setter
    private InputPacketManager inputPacketManager;
    @Setter
    private Executor executor;

    @Getter
    @Setter
    private String appName;

    @Getter
    @Setter
    private String appid;


    @Setter
    private ConnectionContextHolder connectionContextHolder = new DefaultConnectionContextHolder();

    @Setter
    private FutureContainer futureContainer = new ExpireFutureContainer();



    @Override
    public ConnectionContextHolder getConnectionContextHolder() {
        return this.connectionContextHolder;
    }

    @Override
    public FutureContainer getFutureContainer() {
        return this.futureContainer;
    }

    @Override
    public InputPacketManager getInputPacketManager() {
        return this.inputPacketManager;
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }
}
