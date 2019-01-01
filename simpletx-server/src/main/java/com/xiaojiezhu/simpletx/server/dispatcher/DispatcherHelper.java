package com.xiaojiezhu.simpletx.server.dispatcher;

import com.xiaojiezhu.simpletx.protocol.dispatcher.DefaultProtocolDispatcher;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import com.xiaojiezhu.simpletx.server.config.SimpletxConfig;
import com.xiaojiezhu.simpletx.server.dispatcher.handler.AuthorizationHandler;
import com.xiaojiezhu.simpletx.server.dispatcher.handler.GlobalCommitRollbackHandler;
import com.xiaojiezhu.simpletx.server.dispatcher.handler.CreateJoinGroupHandler;
import com.xiaojiezhu.simpletx.server.dispatcher.handler.LocalTransactionCompleteHandler;
import com.xiaojiezhu.simpletx.server.transaction.context.TransactionServerContext;
import com.xiaojiezhu.simpletx.util.Constant;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:56
 */
@AllArgsConstructor
public class DispatcherHelper {

    private final TransactionServerContext serverContext;
    private final SimpletxConfig simpletxConfig;


    public ProtocolDispatcher createProtocolDispatcher() {
        ProtocolDispatcher protocolDispatcher = new DefaultProtocolDispatcher();

        //authorization
        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_LOGIN, new AuthorizationHandler(simpletxConfig.getPassword()));

        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_CREATE_GROUP , new CreateJoinGroupHandler(serverContext));
        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_JOIN_GROUP , new CreateJoinGroupHandler(serverContext));

        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_COMMIT , new GlobalCommitRollbackHandler(serverContext));
        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_ROLLBACK, new GlobalCommitRollbackHandler(serverContext));

        protocolDispatcher.register(Constant.Client.ProtocolCode.CODE_COMPLETE_LOCAL_TRANSACTION , new LocalTransactionCompleteHandler());

        return protocolDispatcher;
    }
}
