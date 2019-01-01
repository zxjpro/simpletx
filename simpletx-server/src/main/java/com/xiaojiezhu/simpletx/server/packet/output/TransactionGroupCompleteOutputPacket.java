package com.xiaojiezhu.simpletx.server.packet.output;

import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.common.node.Node;
import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 20:10
 */
@AllArgsConstructor
@Getter
@Setter
public class TransactionGroupCompleteOutputPacket implements OutputPacket {

    private final ObjectCodec objectCodec;

    private boolean success;
    private int responseMessageId;

    private List<Node> successNode;
    private List<Node> errorNode;

    public TransactionGroupCompleteOutputPacket(ObjectCodec objectCodec) {
        this.objectCodec = objectCodec;
    }

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(this.success);
        byteBuf.writeInt(this.responseMessageId);

        if(successNode != null && successNode.size() > 0){
            byte[] successBytes = encode(this.successNode);
            byteBuf.writeLengthBytes(successBytes);

            if(!this.success){

                if(errorNode == null || errorNode.size() == 0){
                    throw new NullPointerException("when success is false , the error node can not be empty");
                }

                byte[] errorBytes = encode(this.errorNode);
                byteBuf.writeLengthBytes(errorBytes);

            }

        }
    }


    private byte[] encode(List<Node> nodes) {
        if(nodes == null || nodes.size() == 0){
            throw new NullPointerException("node can not be null");
        }

        byte[] bytes = new byte[0];
        try {
            bytes = this.objectCodec.encode(nodes);
        } catch (IOException e) {
            throw new RuntimeException("object encode error " , e);
        }
        return bytes;
    }

}
