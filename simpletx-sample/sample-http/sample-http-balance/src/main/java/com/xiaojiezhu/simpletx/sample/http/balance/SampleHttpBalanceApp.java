package com.xiaojiezhu.simpletx.sample.http.balance;

import com.xiaojiezhu.simpletx.client.annotation.EnableSimpletxTransaction;
import com.xiaojiezhu.simpletx.core.TransactionAspectConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:09
 */
@MapperScan("com.xiaojiezhu.simpletx.sample.common.dao.db")
@SpringBootApplication
@EnableSimpletxTransaction
public class SampleHttpBalanceApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleHttpBalanceApp.class , args);
    }
}
