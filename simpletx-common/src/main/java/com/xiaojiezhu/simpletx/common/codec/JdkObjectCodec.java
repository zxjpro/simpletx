package com.xiaojiezhu.simpletx.common.codec;

import com.xiaojiezhu.simpletx.util.asserts.Asserts;
import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.*;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:11
 */
public class JdkObjectCodec extends AbstractObjectCodec {

    @Override
    public byte[] encode(Object object) throws IOException {
        Asserts.assertNotNull(object , "object can not be null");
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(arrayOutputStream);
            out.writeObject(object);
        } finally {
            IOUtils.close(out);
        }
        return arrayOutputStream.toByteArray();
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> t) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream = null;
        Object o = null;

        try {
            inputStream = new ObjectInputStream(bin);
            o = inputStream.readObject();
        } finally {
            IOUtils.close(inputStream);
        }


        return (T) o;
    }
}
