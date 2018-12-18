package com.xiaojiezhu.simpletx.test.coec;

import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.util.io.IOUtils;
import io.netty.channel.pool.FixedChannelPool;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 20:22
 */
public class DifferentVersionCodec {


    private File file = new File("/Users/zxj/tmp/person.bin");

    private ObjectCodec objectCodec = new KryoObjectCodec();

    @Test
    public void testWrite() throws IOException {

        byte[] bytes = objectCodec.encode(createPerson(12));

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    @Test
    public void testRead() throws IOException, ClassNotFoundException {
        Person person = objectCodec.decode(IOUtils.toByteArray(new FileInputStream(file)), Person.class);
        System.out.println(person);
    }


    private Person createPerson(int id) {
        Person p = new Person();
        p.setId(id);
        p.setHeight(20.2);
        p.setName("小李");
        List<String> parent = new LinkedList<>();
        parent.add("李四");
        parent.add("王五");
        //p.setParent(parent);

        return p;
    }
}
