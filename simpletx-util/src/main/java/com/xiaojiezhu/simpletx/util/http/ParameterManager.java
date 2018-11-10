package com.xiaojiezhu.simpletx.util.http;

import java.util.Map;

public interface ParameterManager {

    Map<String,String> getParameters();

    void addParameter(String name , String value);
}
