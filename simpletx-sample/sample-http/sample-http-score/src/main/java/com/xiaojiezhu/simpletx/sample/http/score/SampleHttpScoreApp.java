package com.xiaojiezhu.simpletx.sample.http.score;

import com.xiaojiezhu.simpletx.client.annotation.EnableSimpletxTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:20
 */
@MapperScan("com.xiaojiezhu.simpletx.sample.common.dao.db")
@SpringBootApplication
@EnableSimpletxTransaction
public class SampleHttpScoreApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpScoreApp.class , args);
    }
}
