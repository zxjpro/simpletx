package com.xiaojiezhu.simpletx.core.net.packet.output;

import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import com.xiaojiezhu.simpletx.util.StringUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 11:12
 */
@AllArgsConstructor
public class CreateJoinGroupOutputPacket implements OutputPacket {

    /**
     * 是否补偿
     */
    private final boolean compensate;

    private final MethodParameter methodParameter;

    private final ObjectCodec objectCodec;

    private final String transactionGroupId;


    /**
     * 是否带有请求参数 1
     * transactionGroupId StringToZero
     * methodParameter byteToLast
     * @param byteBuf
     */
    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(this.compensate);
        byteBuf.writeStringEndZero(this.transactionGroupId);

        if(StringUtils.isEmpty(methodParameter.getBeanName())){
            byteBuf.writeBoolean(false);
        }else{
            byteBuf.writeBoolean(true);
            byteBuf.writeStringEndZero(methodParameter.getBeanName());
        }

        byteBuf.writeStringEndZero(methodParameter.getClassName());
        byteBuf.writeStringEndZero(methodParameter.getMethodName());


        if(this.compensate){
            if(methodParameter.getArgs() != null && methodParameter.getArgs().length > 0){
                byteBuf.writeBoolean(true);
                byte[] bytes = null;
                try {
                    bytes = this.objectCodec.encode(methodParameter.getArgs());
                } catch (IOException e) {
                    throw new RuntimeException("write bytes error" , e);
                }

                byteBuf.writeBytes(bytes);
            }else{
                byteBuf.writeBoolean(false);
            }

        }
    }
}
