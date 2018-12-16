package com.xiaojiezhu.simpletx.protocol.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:02
 */
@Getter
@Setter
public class Header {

    public static final int HEADER_BYTE_LENGTH = 3 + 3 + 4;


    /**
     * message body length , 3 byte
     */
    private int bodyLength;

    /**
     * message id , 3 byte
     */
    private int id;


    /**
     * 4 byte
     */
    private int code;

}
