package com.xiaojiezhu.simpletx.util.http;

import java.util.Map;

public interface HeaderManager {

    Map<String,String> getHeaders();

    void addHeader(String name , String value);
}
