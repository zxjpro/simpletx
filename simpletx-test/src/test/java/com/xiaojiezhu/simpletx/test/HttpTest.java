package com.xiaojiezhu.simpletx.test;

import com.xiaojiezhu.simpletx.util.http.*;
import org.junit.Test;

import java.io.IOException;

public class HttpTest {


    @Test
    public void testBaidu() throws IOException {
        HttpClient httpClient = new HttpClient();

        HttpClient.Builder builder = httpClient.builder();

        String request = builder.url("http://www.baidu.com")
                .contentType(ContentType.TEXT_HTML)
                .build().request();


        System.out.println(request);
    }


    @Test
    public void test() throws IOException {
        HttpClient httpClient = new HttpClient();

        HttpClient.Builder builder = httpClient.builder();

        String request = builder.url("http://localhost:8080/")
                .post()
                .addParameter("username" , "admin")
                .addParameter("username" , "root")
                .contentType(ContentType.TEXT_HTML)
                .postEntity("this is post data")
                .build().request();


        System.out.println(request);

    }


    @Test
    public void test2() throws IOException {
        HttpClient httpClient = new HttpClient();
        httpClient.setHeaderHandler(new HeaderHandler() {
            @Override
            public void handler(HeaderManager headerManager) {
                headerManager.addHeader("password" , "123456");
            }
        });

        HttpClient.Builder builder = httpClient.builder();

        HttpResponse response = builder.url("http://localhost:8080/")
                .post()
                .addParameter("username", "admin")
                .addParameter("username", "root")
                .contentType(ContentType.TEXT_HTML)
                .postEntity("this is post data")
                .build().requests();


        System.out.println(response.text());
        System.out.println(response.getStatus());
        System.out.println(response.getHeaderKeys());
        for (String headerKey : response.getHeaderKeys()) {
            System.out.println(response.getHeader(headerKey));
        }

    }
}
