package com.xiaojiezhu.simpletx.sample.http.score.service.impl;

import com.xiaojiezhu.simpletx.sample.common.dao.db.ScoreDao;
import com.xiaojiezhu.simpletx.sample.common.dao.model.Score;
import com.xiaojiezhu.simpletx.sample.http.score.service.ScoreService;
import org.springframework.stereotype.Service;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:25
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    private ScoreDao scoreDao;

    public ScoreServiceImpl(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    @Override
    public void addScore(String userId) {
        Score score = new Score();
        score.setScore(100);
        score.setUserId(userId);

        scoreDao.insertScore(score);
    }
}
