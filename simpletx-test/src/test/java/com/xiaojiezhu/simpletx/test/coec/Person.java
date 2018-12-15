package com.xiaojiezhu.simpletx.test.coec;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/15 15:45
 */
@Getter
@Setter
@ToString
public class Person implements Serializable {

    private int id;

    private String name;

    private Double height;

    private List<String> parent;

}
