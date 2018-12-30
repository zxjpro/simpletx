package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.message.Message;
import com.xiaojiezhu.simpletx.util.Constant;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 19:33
 */
public class ClientConnectionContext extends AbstractConnectionContext {

    private boolean authorization;


    /**
     * channel id
     */
    private int id = -1;

    public ClientConnectionContext(Channel channel) {
        super(channel);

    }


    @Override
    public void sendMessage(Message message) {
        if(Constant.Client.ProtocolCode.CODE_LOGIN != message.getHeader().getCode()){
            this.loginSuccess();
        }

        super.sendMessage(message);
    }



    private void loginSuccess(){
        if(!this.isAuthorization()){
            while (!this.isAuthorization()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }





    @Override
    public boolean isAuthorization() {
        if(this.authorization){
            return true;
        }else{
            if(null != get(Constant.Client.ConnectionSession.LOGIN_SUCCESS)){
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
            Object id = this.get(Constant.Client.ConnectionSession.ID);
            if(id == null){
                throw new RuntimeException("id not init");
            }
            this.id = Integer.parseInt(String.valueOf(id));
        }
        return this.id;
    }




}
