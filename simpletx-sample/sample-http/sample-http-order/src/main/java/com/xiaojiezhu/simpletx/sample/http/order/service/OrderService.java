package com.xiaojiezhu.simpletx.sample.http.order.service;

import java.io.IOException;

public interface OrderService {


    void createOrder(String userId) throws IOException;
}
