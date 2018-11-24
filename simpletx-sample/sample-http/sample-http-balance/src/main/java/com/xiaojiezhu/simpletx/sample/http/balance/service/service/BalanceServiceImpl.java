package com.xiaojiezhu.simpletx.sample.http.balance.service.service;

import com.xiaojiezhu.simpletx.sample.common.dao.db.BalanceDao;
import com.xiaojiezhu.simpletx.sample.common.dao.model.Balance;
import com.xiaojiezhu.simpletx.sample.http.balance.service.BalanceService;
import org.springframework.stereotype.Service;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:15
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    private BalanceDao balanceDao;

    public BalanceServiceImpl(BalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    @Override
    public void addBalance(String userId) {
        Balance balance = new Balance();
        balance.setBalance(999);
        balance.setUserId(userId);

        balanceDao.insertBalance(balance);
    }
}
