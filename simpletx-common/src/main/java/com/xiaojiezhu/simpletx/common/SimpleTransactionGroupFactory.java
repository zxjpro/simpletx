package com.xiaojiezhu.simpletx.common;

import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 22:02
 */
public class SimpleTransactionGroupFactory extends TransactionGroupFactory {

    @Override
    public String generateGroupId() {
        return UUID.randomUUID().toString().replace("-" , "");
    }
}
