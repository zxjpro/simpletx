package com.xiaojiezhu.simpletx.common.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 22:44
 */
@Getter
@Setter
@ToString
public class MethodParameter {


    private String className;

    private String beanName;

    private String methodName;

    private Object[] args;



}
