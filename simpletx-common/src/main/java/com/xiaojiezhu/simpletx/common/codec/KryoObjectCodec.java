package com.xiaojiezhu.simpletx.common.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xiaojiezhu.simpletx.util.io.IOUtils;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 使用kryo序列化的方式，序列化的对象，必须含有空的构造方法，否则会出现异常
 *
 * @author xiaojie.zhu
 * time 2018/12/15 15:35
 */
public class KryoObjectCodec extends AbstractObjectCodec {

//    private static Kryo kryo = new Kryo();



    @Override
    public byte[] encode(Object object) throws IOException {
        Kryo kryo = new Kryo();

        ByteArrayOutputStream bout = null;
        Output out = null;

        byte[] bytes = null;

        try {
            bout = new ByteArrayOutputStream();
            out = new Output(bout);
            kryo.writeClassAndObject(out , object);
        } finally {
            IOUtils.close(out);
            IOUtils.close(bout);
        }

        bytes = bout.toByteArray();


        return bytes;
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> t) throws IOException, ClassNotFoundException {
        Kryo kryo = new Kryo();

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        Input input = new Input(bin);

        T t1 = null;
        t1 = (T) kryo.readClassAndObject(input);
        return t1;
    }
}
