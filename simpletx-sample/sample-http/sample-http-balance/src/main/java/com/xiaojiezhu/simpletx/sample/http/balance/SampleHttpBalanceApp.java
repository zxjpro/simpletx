package com.xiaojiezhu.simpletx.sample.http.balance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:09
 */
@MapperScan("com.xiaojiezhu.simpletx.sample.common.dao.db")
@SpringBootApplication
public class SampleHttpBalanceApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpBalanceApp.class , args);
    }
}
