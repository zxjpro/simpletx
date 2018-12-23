package com.xiaojiezhu.simpletx.server.dispatcher.handler;

import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.ServerConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.protocol.message.MessageCreator;
import com.xiaojiezhu.simpletx.protocol.message.MessageUtil;
import com.xiaojiezhu.simpletx.server.packet.input.CreateJoinGroupInputPacket;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlockInfo;
import com.xiaojiezhu.simpletx.server.transaction.context.TransactionServerContext;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:38
 */
@AllArgsConstructor
public class CreateJoinGroupHandler implements ProtocolHandler<CreateJoinGroupInputPacket> {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final TransactionServerContext serverContext;

    @Override
    public void handler(ConnectionContext cc, int msgId , int code , CreateJoinGroupInputPacket content) {
        ServerConnectionContext connectionContext = (ServerConnectionContext) cc;

        TransactionBlockInfo transactionBlockInfo = new TransactionBlockInfo();
        transactionBlockInfo.setBeanName(content.getBeanName());
        transactionBlockInfo.setClassName(content.getClassName());
        transactionBlockInfo.setCompensate(content.isCompensate());
        transactionBlockInfo.setMethodName(content.getMethodName());
        transactionBlockInfo.setTransactionGroupId(content.getTransactionGroupId());
        transactionBlockInfo.setHasBeanName(content.isHasBeanName());
        transactionBlockInfo.setMethodParameter(content.getMethodParameter());
        transactionBlockInfo.setAppName(connectionContext.getAppName());
        transactionBlockInfo.setAppid(connectionContext.getAppid());

        TransactionBlock transactionBlock = new TransactionBlock(transactionBlockInfo, connectionContext);

        if(Constant.Client.ProtocolCode.CODE_CREATE_GROUP == code){
            LOG.debug(StringUtils.str("create transaction group:" , content.getTransactionGroupId()));
            serverContext.createTransactionGroup(content.getTransactionGroupId(), transactionBlock);

        }else if(Constant.Client.ProtocolCode.CODE_JOIN_GROUP == code){

            LOG.debug(StringUtils.str("join transaction group:" , content.getTransactionGroupId()));
            serverContext.joinTransactionGroup(content.getTransactionGroupId() , transactionBlock);
        }else{
            throw new RuntimeException("not support code : " + code);
        }

        cc.sendMessage(new MessageCreator() {
            @Override
            public Message create(ByteBuf buffer) {
                Message okMessage = MessageUtil.createOkMessage(msgId, null, buffer);
                return okMessage;
            }
        });

    }
}
