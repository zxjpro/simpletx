package com.xiaojiezhu.simpletx.sample.http.coupon.controller;


import com.xiaojiezhu.simpletx.sample.http.coupon.service.CouponService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {

    private CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @RequestMapping("/addCoupon")
    public String addCoupon(@RequestParam("userId")String userId){
        couponService.addCoupon(userId);
        return "COUPON SUCCESS";
    }
}
