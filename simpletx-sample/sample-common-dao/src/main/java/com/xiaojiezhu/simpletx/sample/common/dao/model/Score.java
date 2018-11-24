package com.xiaojiezhu.simpletx.sample.common.dao.model;

public class Score {

    private int id;

    private String userId;

    private int score;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }
}
