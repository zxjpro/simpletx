package com.xiaojiezhu.simpletx.sample.http.order.controller;

import com.xiaojiezhu.simpletx.sample.http.order.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/createOrder")
    public String createOrder(@RequestParam("userId") String userId) throws IOException {
        this.orderService.createOrder(userId);
        return "SUCCESS";
    }
}
