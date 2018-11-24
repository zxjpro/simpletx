package com.xiaojiezhu.simpletx.sample.common.dao.db;

import com.xiaojiezhu.simpletx.sample.common.dao.model.Score;
import org.apache.ibatis.annotations.Insert;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 15:37
 */
public interface ScoreDao {


    @Insert("INSERT INTO score(user_id,score) values(#{userId} , ${score})")
    void insertScore(Score score);
}
