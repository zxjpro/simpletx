package com.xiaojiezhu.simpletx.server.dispatcher.handler;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.server.packet.input.CommitRollbackInputPacket;
import com.xiaojiezhu.simpletx.server.packet.output.CommitRollbackOutputPacket;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.TransactionGroup;
import com.xiaojiezhu.simpletx.server.transaction.context.TransactionServerContext;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:55
 */
@AllArgsConstructor
public class CommitRollbackHandler implements ProtocolHandler<CommitRollbackInputPacket> {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final TransactionServerContext serverContext;

    @Override
    public void handler(ConnectionContext c, int msgId , int code ,  CommitRollbackInputPacket content) {
        ServerConnectionContext connectionContext = (ServerConnectionContext) c;
        if(Constant.Client.ProtocolCode.CODE_COMMIT == code){
            LOG.debug(StringUtils.str("transaction group:" , content.getTransactionGroupId() , " commit"));

        }else{
            LOG.debug(StringUtils.str("transaction group:" , content.getTransactionGroupId() , " rollback"));

        }

        TransactionGroup transactionGroup = serverContext.findTransactionGroup(content.getTransactionGroupId());
        if(transactionGroup == null){
            throw new NullPointerException(StringUtils.str("can not find transaction group: " , content.getTransactionGroupId()));
        }

        List<TransactionBlock> blocks = transactionGroup.listBlock();
        if(blocks == null || blocks.size() == 0){
            throw new NullPointerException(StringUtils.str("block of transaction group is null , transaction group id:" + content.getTransactionGroupId()));
        }



        commitOrRollback(content, connectionContext, blocks);


    }

    /**
     * commit or rollback to notify all of the transaction group node
     * @param content
     * @param connectionContext
     * @param blocks
     */
    private void commitOrRollback(CommitRollbackInputPacket content, ServerConnectionContext connectionContext, List<TransactionBlock> blocks) {
        String operaStr = null;
        int protocolCode = -1;
        if(content.isCommit()){
            operaStr = " commit ";
            protocolCode = Constant.Server.ProtocolCode.CODE_NOTIFY_COMMIT;
        }else{
            operaStr = " rollback ";
            protocolCode = Constant.Server.ProtocolCode.CODE_NOTIFY_ROLLBACK;
        }


        StringBuilder sb = new StringBuilder();
        sb.append(operaStr).append(" transaction group success , groupId: ").append(content.getTransactionGroupId());
        sb.append(" root transaction block [ ").append(connectionContext.getAppName()).append(" : ").append(connectionContext.getAppid());
        sb.append(" : ");
        for (TransactionBlock block : blocks) {
            ServerConnectionContext cc = block.getConnectionContext();
            if (block.getTransactionBlockInfo().isRootTransaction()) {

                sb.append(block.getTransactionBlockInfo().getMethodName()).append(" ] .");
                sb.append("join blocks [ ");
            }

            if (!cc.isActive()) {
                LOG.warn(StringUtils.str(operaStr, "the transaction to meet a disconnect connection, appName: ",
                        cc.getAppName(), " ,appid:", cc.getAppid(), " , transaction group id: ", content.getTransactionGroupId()));
                //TODO: 在集群节点，马上发起补偿。 全部宕机，则持久化参数，在重新启动后，发起补偿

            } else {
                if (cc != connectionContext) {
                    sb.append(" { ").append(cc.getAppName()).append(" : ").append(cc.getAppid()).append(" : ");
                    sb.append(block.getTransactionBlockInfo().getMethodName());
                    sb.append(" } ");

                    notifyMessage(content, protocolCode, cc);
                } else {
                    System.out.println("删除此行代码");
                }
            }
        }

        sb.append(" ] ");

        LOG.debug(sb.toString());
    }

    private void notifyMessage(CommitRollbackInputPacket content, int protocolCode, ServerConnectionContext cc) {
        CommitRollbackOutputPacket packet = new CommitRollbackOutputPacket(content.isCommit(), content.getTransactionGroupId());
        int finalProtocolCode = protocolCode;
        cc.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                return MessageUtil.createMessage(MessageIdGenerator.getInstance().next(),
                        finalProtocolCode, buffer, packet);
            }
        });
    }
}
