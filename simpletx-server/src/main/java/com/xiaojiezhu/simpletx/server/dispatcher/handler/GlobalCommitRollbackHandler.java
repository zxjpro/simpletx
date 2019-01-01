package com.xiaojiezhu.simpletx.server.dispatcher.handler;

import com.xiaojiezhu.simpletx.common.node.Node;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.protocol.future.Futures;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.server.packet.input.GlobalCommitRollbackInputPacket;
import com.xiaojiezhu.simpletx.server.packet.input.LocalTransactionInputCompletePacket;
import com.xiaojiezhu.simpletx.server.packet.output.CommitRollbackOutputPacket;
import com.xiaojiezhu.simpletx.server.packet.output.TransactionGroupCompleteOutputPacket;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.TransactionGroup;
import com.xiaojiezhu.simpletx.server.transaction.context.TransactionServerContext;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:55
 */
public class GlobalCommitRollbackHandler implements ProtocolHandler<GlobalCommitRollbackInputPacket> {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final TransactionServerContext serverContext;
    private long expireTime = -1;

    public GlobalCommitRollbackHandler(TransactionServerContext serverContext) {
        this.serverContext = serverContext;
        this.expireTime = Long.parseLong(System.getProperty(Constant.Server.ConnectionSession.EXPIRE_TIME));
    }

    @Override
    public void handler(ConnectionContext c, int messageId , int code ,  GlobalCommitRollbackInputPacket content) {
        ServerConnectionContext connectionContext = (ServerConnectionContext) c;
        logger(code, content.getTransactionGroupId());

        TransactionGroup transactionGroup = findTransactionGroup(content);

        List<TransactionBlock> blocks = transactionGroup.listBlock();
        if(blocks == null || blocks.size() == 0){
            throw new NullPointerException(StringUtils.str("block of transaction group is null , transaction group id:" + content.getTransactionGroupId()));
        }

        //TODO: 这里应该等待所有的节点全部通知返回处理成功后，再通知发起者成功
        int notifyNodeSize = blocks.size() - 1;

        //notify all nodes to commit or rollback local transaction , and wait result
        List<LocalTransactionInputCompletePacket> packets = notifyTransactionGroup(content, connectionContext, blocks);

        TransactionGroupCompleteOutputPacket packet = new TransactionGroupCompleteOutputPacket(com.xiaojiezhu.simpletx.server.util.Constant.OBJECT_CODEC);
        packet.setSuccess(packets.size() == notifyNodeSize);
        packet.setResponseMessageId(messageId);


        if(notifyNodeSize == 0){
            LOG.warn(StringUtils.str("transaction group [ ", transactionGroup.getTransactionId(), " ] just one transaction node , if there is only one forever,",
                    "use local transaction will be greater performance to simpletx-transaction"));

        }else{
            List<Node> successNode = getSuccessNode(packets);
            List<Node> errorNode = null;

            int size = getLength(packets);
            if(size != notifyNodeSize){
                //TODO: 出现了等待返回的结果数量不一致的情况，也就是有部分节点，并没有返回本地事务执行的结果,需要进入补偿计划

                errorNode = getNotCompleteNode(blocks, packets);
            }
            packet.setSuccessNode(successNode);
            packet.setErrorNode(errorNode);

        }

        connectionContext.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                return MessageUtil.createMessage(MessageIdGenerator.getInstance().next() , Constant.Server.ProtocolCode.CODE_TRANSACTION_GROUP_COMPLETE ,
                        buffer , packet);
            }
        });


    }

    /**
     * 通知事务组，并且等待返回结果
     * @param content
     * @param connectionContext
     * @param blocks
     * @return
     */
    private List<LocalTransactionInputCompletePacket> notifyTransactionGroup(GlobalCommitRollbackInputPacket content, ServerConnectionContext connectionContext, List<TransactionBlock> blocks) {
        int futureId = MessageIdGenerator.getInstance().next();

        Future<Object> future = Futures.createListValueFuture(this.serverContext.getFutureContainer(), futureId, blocks.size() - 1);

        // notify all node of the group commit or rollback local transaction
        commitOrRollbackGroup(futureId , content, connectionContext, blocks);


        try {
            future.await(expireTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
            //TODO: 超时，需要处理
        }

        Object value = future.getNow();

        List<LocalTransactionInputCompletePacket> packets = (List<LocalTransactionInputCompletePacket>) value;
        return packets;
    }


    private List<Node> getSuccessNode(List<LocalTransactionInputCompletePacket> packets){
        List<Node> nodes = new ArrayList<>();

        for (LocalTransactionInputCompletePacket packet : packets) {
            if(packet.isSuccess()){
                nodes.add(new Node(packet.getAppName() , packet.getAppid()));
            }
        }

        return nodes;
    }

    /**
     * find not complete node
     * @param blocks
     * @param packets
     * @return
     */
    private List<Node> getNotCompleteNode(List<TransactionBlock> blocks , List<LocalTransactionInputCompletePacket> packets){
        List<Node> nodes = new ArrayList<>();

        for (LocalTransactionInputCompletePacket packet : packets) {
            String appName = packet.getAppName();
            String appid = packet.getAppid();
            if(!containInBlocks(blocks , appName , appid)){
                nodes.add(new Node(appName , appid));
            }else{
                if(!packet.isSuccess()){
                    nodes.add(new Node(appName , appid));
                }
            }
        }

        return nodes;
    }

    /**
     * the block list is contain node { appName , appid}
     * @param blocks
     * @param appName
     * @param appid
     * @return
     */
    private boolean containInBlocks(List<TransactionBlock> blocks , String appName , String appid){
        for (TransactionBlock block : blocks) {
            ServerConnectionContext cc = block.getConnectionContext();
            if(appName.equals(cc.getAppName()) && appid.equals(cc.getAppid())){

                return true;
            }
        }

        return false;
    }

    /**
     * notify root transaction client to transaction group is complete
     * @param messageId
     * @param connectionContext
     */
    protected void notifyTransactionSuccess(int messageId, ServerConnectionContext connectionContext) {
        connectionContext.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                return MessageUtil.createOkMessage(messageId , null , buffer);
            }
        });
    }

    private int getLength(List<LocalTransactionInputCompletePacket> packets) {
        if(packets == null){
            return 0;
        }else{
            return packets.size();
        }
    }

    private TransactionGroup findTransactionGroup(GlobalCommitRollbackInputPacket content) {
        TransactionGroup transactionGroup = serverContext.findTransactionGroup(content.getTransactionGroupId());
        if(transactionGroup == null){
            throw new NullPointerException(StringUtils.str("can not find transaction group: " , content.getTransactionGroupId()));
        }
        return transactionGroup;
    }

    private void logger(int code, String transactionGroupId) {
        if(Constant.Client.ProtocolCode.CODE_COMMIT == code){
            LOG.debug(StringUtils.str("transaction group:" , transactionGroupId , " commit"));

        }else{
            LOG.debug(StringUtils.str("transaction group:" , transactionGroupId , " rollback"));

        }
    }

    /**
     * commit or rollback to notify all of the transaction group node
     * @param futureId
     * @param content
     * @param connectionContext
     * @param blocks
     */
    private void commitOrRollbackGroup(int futureId , GlobalCommitRollbackInputPacket content, ServerConnectionContext connectionContext, List<TransactionBlock> blocks) {
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
        sb.append(operaStr).append(" transaction group success , groupId: ").append(content.getTransactionGroupId()).append(" , ");
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

                    notifyMessage(futureId , content, protocolCode, cc);
                }
            }
        }

        sb.append(" ] ");

        LOG.debug(sb.toString());
    }

    /**
     * 通知指节点，提交，或者回滚本地事务
     * @param futureId
     * @param content
     * @param protocolCode
     * @param cc
     */
    private void notifyMessage(int futureId , GlobalCommitRollbackInputPacket content, int protocolCode, ServerConnectionContext cc) {
        CommitRollbackOutputPacket packet = new CommitRollbackOutputPacket(content.isCommit(), content.getTransactionGroupId());
        int finalProtocolCode = protocolCode;
        cc.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                return MessageUtil.createMessage(futureId,
                        finalProtocolCode, buffer, packet);
            }
        });
    }
}
