package com.xiaojiezhu.simpletx.test.coec;

import com.xiaojiezhu.simpletx.common.codec.JSONObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.JdkObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:44
 */
public class ObjectCodecTest {


    @Test
    public void run() throws IOException, ClassNotFoundException {
        System.out.println("jdk ... start");
        System.out.println(runJdk());
        System.out.println("jdk ... end");
        System.out.println();

        System.out.println("json ... start");
        System.out.println(runJSON());
        System.out.println("json ... end");
        System.out.println();

        System.out.println("kyro ... start");
        System.out.println(runKryo());
        System.out.println("kyro ... end");
        System.out.println();

    }

    private long runJdk() throws IOException, ClassNotFoundException {
        long s = System.currentTimeMillis();
        test(new JdkObjectCodec());
        long e = System.currentTimeMillis();

        return e - s;
    }

    private long runJSON() throws IOException, ClassNotFoundException {
        long s = System.currentTimeMillis();
        test(new JSONObjectCodec());
        long e = System.currentTimeMillis();

        return e - s;
    }

    private long runKryo() throws IOException, ClassNotFoundException {
        long s = System.currentTimeMillis();
        test(new KryoObjectCodec());
        long e = System.currentTimeMillis();

        return e - s;
    }

    public void test(ObjectCodec objectCodec) throws IOException, ClassNotFoundException {

        for (int i = 0; i < 1000000; i++) {
            Person person = createPerson(i);

            byte[] bytes = objectCodec.encode(person);
            Person decode = objectCodec.decode(bytes, Person.class);


        }







    }

    private Person createPerson(int id) {
        Person p = new Person();
        p.setId(id);
        p.setHeight(20.2);
        p.setName("张三");
        List<String> parent = new LinkedList<>();
        parent.add("李四");
        parent.add("王五");
        p.setParent(parent);

        return p;
    }
}
