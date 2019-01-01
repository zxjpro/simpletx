package com.xiaojiezhu.simpletx.core.net.packet.input;

import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.common.node.Node;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.ResponseInputPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 20:32
 */
public class TransactionGroupCompleteInputPacket implements ResponseInputPacket {

    private ObjectCodec objectCodec;

    @Getter
    private boolean success;
    @Getter
    private int responseMessageId;

    @Getter
    private List<Node> successNode;
    @Getter
    private List<Node> errorNode;


    public TransactionGroupCompleteInputPacket() {
        this.objectCodec = SimpletxTransactionUtil.getObjectCodec();
    }

    public TransactionGroupCompleteInputPacket(ObjectCodec objectCodec) {
        this.objectCodec = objectCodec;
    }

    @Override
    public int getResponseMessageId() {
        return this.responseMessageId;
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        this.success = byteBuf.readBoolean();
        this.responseMessageId = byteBuf.readInt();

        if(byteBuf.readableBytes() > 0){
            byte[] successBytes = byteBuf.readLengthBytes();
            this.successNode = decode(successBytes);

            if(!this.success){
                byte[] errorBytes = byteBuf.readLengthBytes();
                this.errorNode = decode(errorBytes);
            }
        }
    }


    private List<Node> decode(byte[] bytes) {
        if(bytes == null || bytes.length == 0){
            throw new NullPointerException("node can not be null");
        }

        try {
            List<Node> decode = this.objectCodec.decode(bytes, ArrayList.class);

            return decode;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("object decode error " , e);
        }
    }

}
