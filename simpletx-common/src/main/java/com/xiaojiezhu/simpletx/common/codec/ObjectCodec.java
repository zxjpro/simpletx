package com.xiaojiezhu.simpletx.common.codec;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:07
 */
public interface ObjectCodec {

    byte[] encode(Object object) throws IOException;

    <T> T decode(byte[] bytes , Class<T> t) throws IOException, ClassNotFoundException;
}
