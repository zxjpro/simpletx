package com.xiaojiezhu.simpletx.smaple.http.pay.controller;

import com.xiaojiezhu.simpletx.smaple.http.pay.service.PayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 15:54
 */
@RestController
public class PayController {

    private PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @RequestMapping("/pay")
    public String pay(@RequestParam("userId") String userId) throws IOException {
        payService.pay(userId);
        return "PAY_SUCCESS";
    }


}
