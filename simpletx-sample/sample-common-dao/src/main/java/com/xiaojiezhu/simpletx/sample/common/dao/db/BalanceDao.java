package com.xiaojiezhu.simpletx.sample.common.dao.db;

import com.xiaojiezhu.simpletx.sample.common.dao.model.Balance;
import org.apache.ibatis.annotations.Insert;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 15:37
 */
public interface BalanceDao {

    @Insert("INSERT INTO balance(user_id , balance_, create_time , update_time) VALUES(#{userId} , ${balance} , now() , now())")
    void insertBalance(Balance balance);
}
