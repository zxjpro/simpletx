package com.xiaojiezhu.simpletx.server;

import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import com.xiaojiezhu.simpletx.protocol.client.ExpireFutureContainer;
import com.xiaojiezhu.simpletx.protocol.server.DefaultServer;
import com.xiaojiezhu.simpletx.server.config.SimpletxConfig;
import com.xiaojiezhu.simpletx.server.config.SimpletxConfigLoader;
import com.xiaojiezhu.simpletx.server.dispatcher.DispatcherHelper;
import com.xiaojiezhu.simpletx.server.listener.ConnectionStatusListener;
import com.xiaojiezhu.simpletx.server.transaction.context.DefaultTransactionServerContext;
import com.xiaojiezhu.simpletx.server.transaction.context.TransactionServerContext;
import com.xiaojiezhu.simpletx.server.util.Banner;
import com.xiaojiezhu.simpletx.util.StringUtils;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:47
 */
public class SimpletxServer {

    public static void main(String[] args) throws InterruptedException {
        Banner.showBanner();
        initLog4j2();
        SimpletxConfig simpletxConfig = SimpletxConfigLoader.loadConfig(SimpletxConfigLoader.getConfPath());



        DefaultServer server = new DefaultServer(simpletxConfig.getHost(), simpletxConfig.getPort());

        ThreadExecutor threadExecutor = new FixThreadExecutor(simpletxConfig.getLogicThreadSize());
        TransactionServerContext serverContext = new DefaultTransactionServerContext(threadExecutor , new ExpireFutureContainer());
        server.setServerContext(serverContext);

        DispatcherHelper dispatcherHelper = new DispatcherHelper(serverContext , simpletxConfig);

        server.setProtocolDispatcher(dispatcherHelper.createProtocolDispatcher());


        server.setWorkerThreadSize(simpletxConfig.getWorkerThreadSize());
        server.register(new ConnectionStatusListener());

        Logger logger = LoggerFactory.getLogger(SimpletxServer.class);
        logger.info(StringUtils.str("simpletx to start , bind:" , simpletxConfig.getHost() + " , port:" + simpletxConfig.getPort()));

        server.start();
    }


    private static void initLog4j2() {
        System.setProperty("log.path",new File(SimpletxConfigLoader.getConfPath()).getParent());
        File log4jFile = new File(SimpletxConfigLoader.getConfPath() + "log4j2.xml");
        try {
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
            Configurator.initialize(null, source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
