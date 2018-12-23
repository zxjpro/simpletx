package com.xiaojiezhu.simpletx.common.codec;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 19:36
 */
public class ObjectCodecs {

    public static enum Type{
        JDK("jdk"),

        JSON("json"),

        KRYO("kryo");

        private String value;

        Type(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return this.value;
        }
    }
}
