package com.xiaojiezhu.simpletx.smaple.http.pay.service;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:02
 */
public interface PayService {

    void pay(String userId) throws IOException;
}
