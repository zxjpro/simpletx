package com.xiaojiezhu.simpletx.protocol.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 22:07
 */
@Setter
@Getter
public class Message {

    private Header header;

    private byte[] body;
}
