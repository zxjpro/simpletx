package com.xiaojiezhu.simpletx.util.http;

import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

class InputStreamEntity implements Entity {

    private InputStream inputStream;

    public InputStreamEntity(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public byte[] toBytes() {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
