package com.xiaojiezhu.simpletx.core.net;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/24 23:27
 */
@AllArgsConstructor
@Getter
public class OkErrorResult {

    private boolean success;

    private String message;
}
