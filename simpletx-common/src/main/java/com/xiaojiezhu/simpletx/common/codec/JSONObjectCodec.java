package com.xiaojiezhu.simpletx.common.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaojiezhu.simpletx.util.asserts.Asserts;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:19
 */
public class JSONObjectCodec extends AbstractObjectCodec {

    @Override
    public byte[] encode(Object object) throws IOException {
        Asserts.assertNotNull(object , "object can not be null");
        String s = JSON.toJSONString(object);
        byte[] bytes = s.getBytes(this.CHARSET);

        return bytes;
    }

    /**
     * 此方法在反序列化带有泛型参数时，会有出现无法映射泛型正确类型的情况,此时建议使用此方法的重载方法
     * @param bytes
     * @param t
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public <T> T decode(byte[] bytes, Class<T> t) throws IOException, ClassNotFoundException {
        Object o = JSON.parseObject(bytes, t);
        return (T) o;
    }


    /**
     * 能正确反序列化结应的泛型结构
     * @param bytes
     * @param reference
     * @param <T>
     * @return
     */
    public <T> T decode(byte[] bytes , TypeReference<T> reference){
        Object o = JSON.parseObject(bytes, (Type) reference);
        return (T) o;
    }
}
