package com.xiaojiezhu.simpletx.core.transaction.manager;

import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.exception.SocketRuntimeException;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.protocol.client.Connection;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 17:34
 */
public class SocketTransactionGroupManager implements TransactionGroupManager {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ConnectionPool connectionPool;

    private final ObjectCodec objectCodec;

    public SocketTransactionGroupManager(ConnectionPool connectionPool , ObjectCodec objectCodec) {
        this.connectionPool = connectionPool;
        this.objectCodec = objectCodec;
    }

    @Override
    public void createGroup(TransactionInfo transactionInfo, MethodParameter methodParameter) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();

            int msgId = MessageIdGenerator.getInstance().next();
            byte[] bytes = objectCodec.encode(methodParameter);

            //MessageUtil.createMessage(msgId , Constant.Client.ProtocolCode.CODE_CREATE_GROUP)

        } catch (IOException e) {
            LOG.error("createGroup fail , detail : " + methodParameter.toString() , e);
            throw new SocketRuntimeException(e);
        } finally {
            IOUtils.close(connection);
        }
    }

    @Override
    public void joinGroup(TransactionInfo transactionInfo, MethodParameter methodParameter) {

    }

    @Override
    public TransactionGroupInvokeStatus status() {
        return null;
    }

    @Override
    public TransactionGroupInvokeFuture notifyCommit() {
        return null;
    }

    @Override
    public TransactionGroupInvokeFuture notifyRollback() {
        return null;
    }
}
