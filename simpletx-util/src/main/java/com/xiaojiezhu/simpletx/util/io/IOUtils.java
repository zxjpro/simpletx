package com.xiaojiezhu.simpletx.util.io;

import java.io.*;
import java.nio.charset.Charset;

public class IOUtils {


    public static String toString(InputStream in , Charset charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in , charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line);
        }

        return sb.toString();
    }

    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeable = null;
            }
        }
    }

    public static void copy(InputStream in , OutputStream out) throws IOException {
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) != -1){
            out.write(buf , 0 , len);
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(inputStream , out);
        return out.toByteArray();
    }

}
