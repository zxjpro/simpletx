package com.xiaojiezhu.simpletx.core.net.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import lombok.Getter;
import lombok.Setter;

/**
 * receive server command to invoke local transaction
 *
 * @author xiaojie.zhu
 * time 2018-12-31 12:31
 */
public class CommitRollbackInputPacket implements InputPacket {

    /**
     * true: commit transaction group
     * false: rollback transaction group
     */
    @Getter
    private boolean commit;

    /**
     * transaction group id
     */
    @Getter
    private String transactionGroupId;

    @Setter
    @Getter
    private int messageId;

    @Override
    public void read(ByteBuffer byteBuf) {

        this.commit = byteBuf.readBoolean();
        this.transactionGroupId = new String(byteBuf.readBytesToLast());

    }
}
