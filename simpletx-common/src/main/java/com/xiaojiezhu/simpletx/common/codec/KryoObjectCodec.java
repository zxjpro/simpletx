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



    private final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            return kryo;
        }
    };
    private final ThreadLocal<Output> outputLocal = new ThreadLocal<Output>();
    private final ThreadLocal<Input> inputLocal = new ThreadLocal<Input>();





    @Override
    public byte[] encode(Object object) throws IOException {

        ByteArrayOutputStream bout = null;
        Output out = null;

        byte[] bytes = null;

        bout = new ByteArrayOutputStream();
        out = getOutput(bout);
        kryoLocal.get().writeClassAndObject(out , object);
        out.flush();

        bytes = bout.toByteArray();


        return bytes;
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> t) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        Input input = getInput(bin);

        T t1 = null;
        t1 = (T) kryoLocal.get().readClassAndObject(input);
        return t1;
    }

    private Output getOutput(ByteArrayOutputStream bout){
        Output output = outputLocal.get();
        if(output == null){
            output = new Output(4096 , 4096);
            outputLocal.set(output);
        }
        if(bout != null){
            output.setOutputStream(bout);
        }
        return output;

    }

    private Input getInput(ByteArrayInputStream bin){
        Input input = inputLocal.get();
        if(input == null){
            input = new Input(4096);
            inputLocal.set(input);
        }

        if(bin != null){
            input.setInputStream(bin);
        }

        return input;

    }
}
