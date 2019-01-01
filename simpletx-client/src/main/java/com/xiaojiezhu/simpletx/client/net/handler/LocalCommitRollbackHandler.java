package com.xiaojiezhu.simpletx.client.net.handler;

import com.xiaojiezhu.simpletx.core.net.packet.input.CommitRollbackInputPacket;
import com.xiaojiezhu.simpletx.protocol.client.FutureContainer;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 12:40
 */
@AllArgsConstructor
public class LocalCommitRollbackHandler implements ProtocolHandler<CommitRollbackInputPacket> {

    private SimpletxContext simpletxContext;

    @Override
    public void handler(ConnectionContext connectionContext, int messageId, int code, CommitRollbackInputPacket content) {
        content.setMessageId(messageId);

        FutureContainer futureContainer = simpletxContext.getFutureContainer();

        FutureCondition futureCondition = futureContainer.find(content.getTransactionGroupId());

        futureCondition.setValue(content);
        futureCondition.signal();

        if(futureCondition.getNum() <= 0){
            futureContainer.remove(content.getTransactionGroupId());
        }
    }
}
