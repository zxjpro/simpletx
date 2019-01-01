package com.xiaojiezhu.simpletx.sample.http.coupon.service.impl;

import com.xiaojiezhu.simpletx.core.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.sample.common.dao.db.CouponDao;
import com.xiaojiezhu.simpletx.sample.common.dao.model.Coupon;
import com.xiaojiezhu.simpletx.sample.http.coupon.service.CouponService;
import org.springframework.stereotype.Service;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:32
 */
@Service
public class CouponServiceImpl implements CouponService {

    private CouponDao couponDao;

    public CouponServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @TxTransactional
    @Override
    public void addCoupon(String userId) {
        Coupon coupon = new Coupon();
        coupon.setName("coupon");
        coupon.setUserId(userId);

        couponDao.insertCoupon(coupon);

        //执行25次以后，就开始阻塞
        //int i = 1 / 0;
    }
}
