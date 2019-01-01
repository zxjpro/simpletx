package com.xiaojiezhu.simpletx.sample.http.order;

import com.xiaojiezhu.simpletx.client.annotation.EnableSimpletxTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSimpletxTransaction
public class SampleHttpOrderApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpOrderApp.class , args);
    }
}
