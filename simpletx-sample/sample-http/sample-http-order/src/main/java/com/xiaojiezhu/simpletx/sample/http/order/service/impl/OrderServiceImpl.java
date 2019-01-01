package com.xiaojiezhu.simpletx.sample.http.order.service.impl;

import com.xiaojiezhu.simpletx.core.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.sample.http.order.service.OrderService;
import com.xiaojiezhu.simpletx.util.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HttpClient httpClient;


    @Override
    @TxTransactional
    public void createOrder(String userId) throws IOException {
        //券
        String r1 = httpClient.builder().url("http://localhost:8081/coupon/addCoupon").addParameter("userId", userId).build().request();

        //支付工程
        String r2 = httpClient.builder().url("http://localhost:8082/pay/pay").addParameter("userId", userId).build().request();

    }

    @Override
    @TxTransactional
    public void createOrder2(String userId) throws IOException {
        //券
        String r1 = httpClient.builder().url("http://localhost:8081/coupon/addCoupon").addParameter("userId", userId).build().request();

    }
}
