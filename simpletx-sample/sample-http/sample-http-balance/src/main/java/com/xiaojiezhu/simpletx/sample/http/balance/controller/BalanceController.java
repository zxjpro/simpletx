package com.xiaojiezhu.simpletx.sample.http.balance.controller;

import com.xiaojiezhu.simpletx.sample.http.balance.service.BalanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:14
 */
@RestController
public class BalanceController {

    private BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @RequestMapping("/addBalance")
    public String balance(@RequestParam("userId") String userId){
        balanceService.addBalance(userId);
        return "BALANCE_SUCCESS";
    }

    @RequestMapping("/addBalance2")
    public String addBalance2(@RequestParam("userId")String userId){
        balanceService.addBalance2(userId);
        return "BALANCE2_SUCCESS";
    }
}
