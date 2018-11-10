package com.xiaojiezhu.simpletx.util.http;

class ByteArrayEntity implements Entity {

    private byte[] buf;

    public ByteArrayEntity(byte[] buf) {
        this.buf = buf;
    }

    @Override
    public byte[] toBytes() {
        return this.buf;
    }
}
