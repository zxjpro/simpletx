package com.xiaojiezhu.simpletx.common.codec;

import com.xiaojiezhu.simpletx.common.define.Constant;

import java.nio.charset.Charset;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:23
 */
public abstract class AbstractObjectCodec implements ObjectCodec {

    protected final Charset CHARSET;

    public AbstractObjectCodec() {
        this.CHARSET = Constant.UTF8;
    }

    public AbstractObjectCodec(Charset charset) {
        this.CHARSET = charset;
    }
}
