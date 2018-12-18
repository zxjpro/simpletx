package com.xiaojiezhu.simpletx.test.coec;

import com.xiaojiezhu.simpletx.common.codec.JSONObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.JdkObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 20:15
 */
public class SizeCodecTest {

    @Test
    public void test() throws IOException {
        Person person = createPerson(1);

        ObjectCodec jdkCodec = new JdkObjectCodec();
        ObjectCodec jsonCodec = new JSONObjectCodec();
        ObjectCodec kryoCodec = new KryoObjectCodec();


        System.out.println("jdk:" + jdkCodec.encode(person).length);
        System.out.println("json:" + jsonCodec.encode(person).length);
        System.out.println("kryo:" + kryoCodec.encode(person).length);

        byte[] encode = kryoCodec.encode(person);
        System.out.println(new String(encode));
    }

    private Person createPerson(int id) {
        Person p = new Person();
        p.setId(id);
        p.setHeight(20.2);
        p.setName("小张");
        List<String> parent = new LinkedList<>();
        parent.add("李四");
        parent.add("王五");
        //p.setParent(parent);

        return p;
    }
}
