package com.xiaojiezhu.simpletx.sample.common.dao.model;

import java.util.Date;

public class Balance {

    private int id;

    private String userId;

    private int balance;

    private Date createTime;

    private Date updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
