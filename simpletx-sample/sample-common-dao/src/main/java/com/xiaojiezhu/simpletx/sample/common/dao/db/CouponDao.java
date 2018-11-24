package com.xiaojiezhu.simpletx.sample.common.dao.db;

import com.xiaojiezhu.simpletx.sample.common.dao.model.Coupon;
import org.apache.ibatis.annotations.Insert;

/**
 * @author zxj
 * time 2018/11/24 15:32
 */
public interface CouponDao {

    @Insert("INSERT INTO coupon(user_id , name_) VALUES(#{userId} , #{name})")
    void insertCoupon(Coupon coupon);
}
