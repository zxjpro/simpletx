package com.xiaojiezhu.simpletx.util.http;

import java.nio.charset.Charset;

class StringEntity implements Entity {

    private String content;
    private Charset charset;

    public StringEntity(String content, Charset charset) {
        this.content = content;
        this.charset = charset;
    }

    @Override
    public byte[] toBytes() {
        return this.content.getBytes(this.charset);
    }
}
