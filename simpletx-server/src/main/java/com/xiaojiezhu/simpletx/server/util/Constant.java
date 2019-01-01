package com.xiaojiezhu.simpletx.server.util;

import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 21:10
 */
public class Constant {

    /**
     * default object codec
     */
    public static final ObjectCodec OBJECT_CODEC = new KryoObjectCodec();
}
