package com.xiaojiezhu.simpletx.server.util;

import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 17:15
 */
public class Banner {

    public static void showBanner(){
        InputStream inputStream = Banner.class.getResourceAsStream("/META-INF/banner");
        try {
            String text = IOUtils.toString(inputStream, Charset.defaultCharset());
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }

        System.out.println();
    }
}
