package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.util.Constant;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:07
 */
public class DefaultServerConnectionContext extends AbstractConnectionContext implements ServerConnectionContext {

    /**
     * a project name
     */
    private String appName;
    /**
     * project instance id
     */
    private String appid;

    private boolean authorization;

    private int id;

    public DefaultServerConnectionContext(Channel channel) {
        super(channel);
    }


    @Override
    public boolean isAuthorization() {
        if(this.authorization){
            return true;
        }else{
            if(null != get(Constant.Server.ConnectionSession.LOGIN_SUCCESS)){
                this.authorization = true;
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public int getId() {
        if(this.id == -1){
            Object id = this.get(Constant.Server.ConnectionSession.ID);
            if(id == null){
                throw new RuntimeException("id not init");
            }
            this.id = Integer.parseInt(String.valueOf(id));
        }
        return this.id;
    }

    @Override
    public String getAppName() {
        if(this.appName == null){
            Object o = this.get(Constant.Server.ConnectionSession.APP_NAME);
            if(o == null){
                throw new NullPointerException("app name not init");
            }
            this.appName = String.valueOf(o);
        }
        return this.appName;
    }

    @Override
    public String getAppid() {
        if(this.appid == null){
            Object o = this.get(Constant.Server.ConnectionSession.APPID);
            if(o == null){
                throw new NullPointerException("appid not init");
            }
            this.appid = String.valueOf(o);
        }
        return this.appid;
    }
}
