package com.xiaojiezhu.simpletx.util.http;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HttpRequest {


    HttpResponse requests() throws IOException;

    String request() throws IOException;



}
