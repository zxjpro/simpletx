package com.xiaojiezhu.simpletx.sample.http.coupon;

import com.xiaojiezhu.simpletx.client.annotation.EnableSimpletxTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaojie.zhu
 */
@MapperScan("com.xiaojiezhu.simpletx.sample.common.dao.db")
@SpringBootApplication
@EnableSimpletxTransaction
public class SampleHttpCouponApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpCouponApp.class , args);
    }
}
