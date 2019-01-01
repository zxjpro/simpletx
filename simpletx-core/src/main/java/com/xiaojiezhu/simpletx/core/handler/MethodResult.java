package com.xiaojiezhu.simpletx.core.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2019-01-01 19:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class MethodResult {

    private Object result;

    private Throwable throwable;
}
