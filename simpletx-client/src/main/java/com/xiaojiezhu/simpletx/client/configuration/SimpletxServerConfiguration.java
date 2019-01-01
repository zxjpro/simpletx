package com.xiaojiezhu.simpletx.client.configuration;

import com.xiaojiezhu.simpletx.client.util.DispatcherHelper;
import com.xiaojiezhu.simpletx.common.codec.*;
import com.xiaojiezhu.simpletx.common.executor.FixThreadExecutor;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import com.xiaojiezhu.simpletx.core.net.SocketTransactionGroupManager;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.DefaultConnectionPool;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:51
 */
public class SimpletxServerConfiguration {


}
