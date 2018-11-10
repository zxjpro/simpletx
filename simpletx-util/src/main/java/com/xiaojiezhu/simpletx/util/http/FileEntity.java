package com.xiaojiezhu.simpletx.util.http;

import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.*;

class FileEntity implements Entity {

    private File file;

    public FileEntity(File file) {
        this.file = file;
    }
    public FileEntity(String path){
        this(new File(path));
    }

    @Override
    public byte[] toBytes() {
        InputStream in = null;
        byte[] buf;
        try {
            in = new FileInputStream(file);
            buf = IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(in);
        }

        return buf;
    }
}
