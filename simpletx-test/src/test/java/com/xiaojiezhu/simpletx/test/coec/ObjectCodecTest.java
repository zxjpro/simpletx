package com.xiaojiezhu.simpletx.test.coec;

import com.xiaojiezhu.simpletx.common.codec.JSONObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.JdkObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:44
 */
public class ObjectCodecTest {


    @Test
    public void run() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("jdk ... start");
        //System.out.println(runJdk());
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

        System.out.println("kyro - thread ... start");
        System.out.println(runKryoThread());
        System.out.println("kyro - thread ... end");
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

    private long runKryoThread() throws IOException, ClassNotFoundException, InterruptedException {
        long s = System.currentTimeMillis();
        final ObjectCodec objectCodec = new KryoObjectCodec();

        int threadSize = 100;
        final CountDownLatch cd = new CountDownLatch(threadSize);

        for (int i = 0; i < threadSize; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        test(objectCodec , 10000);
                        cd.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }).start();
        }
        cd.await();

        long e = System.currentTimeMillis();

        return e - s;
    }

    public void test(ObjectCodec objectCodec) throws IOException, ClassNotFoundException {

        test(objectCodec , 1000000);
    }

    public void test(ObjectCodec objectCodec , int size) throws IOException, ClassNotFoundException {

        for (int i = 0; i < size; i++) {
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
        p.setDate(new Date());
        return p;
    }
}
