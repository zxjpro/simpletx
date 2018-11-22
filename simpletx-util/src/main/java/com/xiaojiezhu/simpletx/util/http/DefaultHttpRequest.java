package com.xiaojiezhu.simpletx.util.http;

import com.xiaojiezhu.simpletx.util.asserts.Asserts;
import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * use jdk default UrlConnection
 */
class DefaultHttpRequest implements HttpRequest{

    private HttpClient.Builder builder;

    private HttpURLConnection conn;

    public DefaultHttpRequest(HttpClient.Builder builder) {
        this.builder = builder;
    }

    @Override
    public HttpResponse requests() throws IOException {
        this.init();

        if(this.builder.getPostByteArray() != null){
            OutputStream outputStream = null;
            try {
                outputStream = this.conn.getOutputStream();
                outputStream.write(this.builder.getPostByteArray());
            } finally {
                IOUtils.close(outputStream);
            }
        }

        return new DefaultHttpResponse(this.conn , this.builder.getCharset());
    }

    @Override
    public String request() throws IOException {
        HttpResponse response = null;
        try {
            response = this.requests();
            String text = response.text();
            return text;
        } finally {
            IOUtils.close(response);
        }
    }




    private void init()throws IOException{
        Asserts.assertNotBlank(this.builder.getUrl() , "url can not be empty");
        URL net = new URL(this.builder.getUrl());
        this.conn = (HttpURLConnection) net.openConnection();
        if(this.builder.isGet()){
            this.conn.setRequestMethod(RequestMethod.GET.toString());
        }else{
            this.conn.setRequestMethod(RequestMethod.POST.toString());
            this.conn.setDoOutput(true);
        }
        this.conn.setDoInput(true);

        if(this.builder.getHeaderManager() != null){
            Iterator<Map.Entry<String, String>> iterator = this.builder.getHeaderManager().getHeaders().entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> entry = iterator.next();
                this.conn.setRequestProperty(entry.getKey() , entry.getValue());
            }
        }

        if(this.builder.getConnectionTimeout() != -1){
            this.conn.setConnectTimeout(this.builder.getConnectionTimeout());
        }
        if(this.builder.getReadTimeout() != -1){
            this.conn.setReadTimeout(this.builder.getReadTimeout());
        }

        this.conn.setRequestProperty("Content-Type" , this.builder.getContentType());

        this.conn.connect();


    }
}
