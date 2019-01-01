package com.xiaojiezhu.simpletx.smaple.http.pay;

import com.xiaojiezhu.simpletx.client.annotation.EnableSimpletxTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 15:52
 */
@SpringBootApplication
@EnableSimpletxTransaction
public class SampleHttpPayApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpPayApp.class , args);
    }
}
