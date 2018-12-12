package com.xiaojiezhu.simpletx.common.node;

import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * @author xiaojie.zhu
 * time 2018/12/12 23:11
 */
@AllArgsConstructor
@Setter
public class SimpleNodeBlock implements NodeBlock {

    private String name;

    @Override
    public String nodeName() {
        return null;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
