package com.xiaojiezhu.simpletx.server.transaction;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:53
 */
@Getter
@Setter
public class TransactionBlockInfo {


    private boolean rootTransaction;
    private boolean compensate;

    private byte[] methodParameter;

    private String transactionGroupId;

    /**
     * 是否把spring bean的名称传过来
     */
    private boolean hasBeanName;

    private String beanName;

    private String className;

    private String methodName;

    private String appName;

    private String appid;
}
