package com.xiaojiezhu.simpletx.sample.http.score.controller;

import com.xiaojiezhu.simpletx.sample.http.score.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 16:22
 */
@RestController
public class ScoreController {

    private ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @RequestMapping("/addScore")
    public String addScore(@RequestParam("userId")String userId) {
        scoreService.addScore(userId);
        return "SUCCESS";
    }
}
