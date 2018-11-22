package com.xiaojiezhu.simpletx.util.http;

import com.xiaojiezhu.simpletx.util.asserts.Asserts;
import com.xiaojiezhu.simpletx.util.http.exception.HttpRequestException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    @Setter
    @Getter
    private HeaderHandler headerHandler;

    @Setter
    @Getter
    private ParameterHandler parameterHandler;

    @Setter
    @Getter
    private Charset defaultCharset = Charset.forName("UTF-8");

    @Setter
    @Getter
    private boolean urlEncoding = true;

    @Getter
    @Setter
    private int connectionTimeout = -1;

    @Getter
    @Setter
    private int readTimeout = -1;




    public Builder builder(){
        return new Builder();
    }


    public class Builder{
        @Getter
        private String url;
        @Getter
        private boolean get = true;
        @Getter
        private Charset charset = defaultCharset;
        @Getter
        private String contentType = ContentType.TEXT_HTML.toString();
        @Getter
        private HeaderManager headerManager;
        @Getter
        private byte[] postByteArray;
        @Getter
        private ParameterManager parameterManager;

        @Getter
        private ParameterHandler parameterHandler;
        @Getter
        private HeaderHandler headerHandler;

        @Getter
        private int connectionTimeout = -1;

        @Getter
        private int readTimeout = -1;

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder get(){
            this.get = true;
            return this;
        }

        public Builder post(){
            this.get = false;
            return this;
        }

        public Builder charset(Charset charset){
            this.charset = charset;
            return this;
        }
        public Builder connectionTimeout(int connectionTimeout){
            this.connectionTimeout = connectionTimeout;
            return this;
        }
        public Builder readTimeout(int readTimeout){
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder addHeader(String key , String value){
            if(this.headerManager == null){
                this.headerManager = new DefaultHeaderManager();
            }
            this.headerManager.addHeader(key , value);
            return this;
        }

        public Builder contentType(ContentType contentType){
            this.contentType = contentType.toString() + ";charset=" + this.charset.displayName();
            return this;
        }

        public Builder contentType(String contentType){
            this.contentType = contentType;
            return this;
        }

        public Builder postEntity(Entity entity){
            this.checkPostEntity();
            Asserts.assertNotNull(entity , "postEntry can not be null");
            this.postByteArray = entity.toBytes();
            return this;
        }

        public Builder postEntity(String text){
            this.checkPostEntity();
            Asserts.assertNotBlank(text , "param can not be null");
            this.postByteArray = text.getBytes(charset);
            return this;
        }

        public Builder postEntity(byte[] bytes){
            this.checkPostEntity();
            Asserts.assertNotNull(bytes , "param can not be null");
            this.postByteArray = bytes;
            return this;
        }

        private void checkPostEntity(){
            if(this.get){
                throw new HttpRequestException("设置postEntity，必须使用post请求");
            }
            if(this.postByteArray != null){
                throw new HttpRequestException("你无法重复设置postEntity");
            }
        }


        public Builder addParameter(String name , String value){
            if(this.parameterManager == null){
                this.parameterManager = new DefaultParameterManager();
            }
            this.parameterManager.addParameter(name , value);
            return this;
        }

        private String buildUrl() throws UnsupportedEncodingException {
            if(this.parameterManager == null){
                return this.url;
            }else{
                StringBuffer sb = new StringBuffer(this.url);
                boolean first = true;
                Iterator<Map.Entry<String, String>> iterator = this.parameterManager.getParameters().entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, String> entry = iterator.next();
                    if(first){
                        if(this.url.contains("?")){
                            sb.append("&");
                            first = false;
                        }else{
                            sb.append("?");
                        }
                    }else{
                        sb.append("&");
                    }

                    sb.append(entry.getKey()).append("=");
                    String value = entry.getValue();
                    if(HttpClient.this.urlEncoding){
                        value = URLEncoder.encode(value , charset.displayName());
                    }

                    sb.append(value);
                }

                return sb.toString();
            }
        }


        public HttpRequest build() throws IOException {
            this.url = this.buildUrl();

            this.headerHandler = HttpClient.this.headerHandler;
            this.parameterHandler = HttpClient.this.parameterHandler;
            if(this.connectionTimeout == -1){
                this.connectionTimeout = HttpClient.this.connectionTimeout;
            }
            if(this.readTimeout == -1){
                this.readTimeout = HttpClient.this.readTimeout;
            }

            //do header handler
            if(this.getParameterHandler() != null){
                if(this.parameterManager == null){
                    this.parameterManager = new DefaultParameterManager();
                }
                this.getParameterHandler().handler(this.parameterManager);
            }
            if(this.getHeaderHandler() != null){
                if(this.headerManager == null){
                    this.headerManager = new DefaultHeaderManager();
                }
                this.getHeaderHandler().handler(this.headerManager);
            }

            return new DefaultHttpRequest(this);
        }

    }
}
