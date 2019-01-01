package com.xiaojiezhu.simpletx.smaple.http.pay.service.impl;

import com.xiaojiezhu.simpletx.core.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.smaple.http.pay.service.PayService;
import com.xiaojiezhu.simpletx.util.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:03
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private HttpClient httpClient;


    @Override
    @TxTransactional
    public void pay(String userId) throws IOException {

        //余额
        String r1 = httpClient.builder().url("http://localhost:8083/balance/addBalance2").addParameter("userId", userId).build().request();

        //积分
        String r2 = httpClient.builder().url("http://localhost:8084/score/addScore").addParameter("userId", userId).build().request();


    }
}
