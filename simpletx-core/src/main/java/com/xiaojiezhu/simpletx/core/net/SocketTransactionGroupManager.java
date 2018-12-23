package com.xiaojiezhu.simpletx.core.net;

import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.exception.SocketRuntimeException;
import com.xiaojiezhu.simpletx.core.net.packet.output.CommitRollbackOutputPacket;
import com.xiaojiezhu.simpletx.core.net.packet.output.CreateJoinGroupOutputPacket;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupInvokeFuture;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupInvokeStatus;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.Connection;
import com.xiaojiezhu.simpletx.protocol.client.ConnectionPool;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.io.IOUtils;
import io.netty.buffer.ByteBuf;
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
    public void createGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter) {
        createJoinGroup(true , transactionGroupId,transactionInfo, methodParameter);
    }

    @Override
    public void joinGroup(String transactionGroupId , TransactionInfo transactionInfo, MethodParameter methodParameter) {
        createJoinGroup(false , transactionGroupId, transactionInfo , methodParameter);

    }


    /**
     * create or join group
     * @param transactionInfo
     * @param methodParameter
     * @return msgId
     */
    private int createJoinGroup(boolean create, String transactionGroupId, TransactionInfo transactionInfo, MethodParameter methodParameter) {
        int msgId = MessageIdGenerator.getInstance().next();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();


            CreateJoinGroupOutputPacket packet = new CreateJoinGroupOutputPacket(transactionInfo.getMethodAttribute().isCompensate() , methodParameter , this.objectCodec , transactionGroupId);

            int code = -1;
            if(create){
                code = Constant.Client.ProtocolCode.CODE_CREATE_GROUP;
            }else{
                code = Constant.Client.ProtocolCode.CODE_JOIN_GROUP;

            }

            int finalCode = code;
            connection.sendMessage(new MessageCreator() {
                @Override
                public Message create(ByteBuf buffer) {
                    Message message = MessageUtil.createMessage(msgId, finalCode, buffer, packet);
                    return message;
                }
            });


        } catch (IOException e) {
            if(create){
                LOG.error("createGroup fail , detail : " + methodParameter.toString() , e);
            }else{
                LOG.error("joinGroup fail , detail : " + methodParameter.toString() , e);
            }
            throw new SocketRuntimeException(e);
        } finally {
            IOUtils.close(connection);
        }

        return msgId;
    }



    @Override
    public TransactionGroupInvokeStatus status() {
        return null;
    }

    @Override
    public TransactionGroupInvokeFuture notifyCommit(String transactionGroupId) {
        commitRollback(transactionGroupId,true);
        return null;
    }

    @Override
    public TransactionGroupInvokeFuture notifyRollback(String transactionGroupId) {
        commitRollback(transactionGroupId,false);
        return null;
    }

    private int commitRollback(String transactionId , boolean commit){
        int msgId = MessageIdGenerator.getInstance().next();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();


            CommitRollbackOutputPacket packet = new CommitRollbackOutputPacket(commit, transactionId);

            int code = -1;
            if(commit){
                code = Constant.Client.ProtocolCode.CODE_COMMIT;
            }else{
                code = Constant.Client.ProtocolCode.CODE_ROLLBACK;
            }

            int finalCode = code;
            connection.sendMessage(new MessageCreator() {
                @Override
                public Message create(ByteBuf buffer) {
                    Message message = MessageUtil.createMessage(msgId, finalCode, buffer, packet);
                    return message;
                }
            });


        } catch (IOException e) {
            if(commit){
                LOG.error("commit transaction fail" , e);
            }else{
                LOG.error("rollback transaction fail" , e);
            }
            throw new SocketRuntimeException(e);
        } finally {
            IOUtils.close(connection);
        }

        return msgId;
    }
}
