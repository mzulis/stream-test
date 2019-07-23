package com.test.demo.controller;

import com.test.demo.mongoDb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    RedditEventCounterDao redditEventCounterDao;
    @Autowired
    SubredditActivityDao subredditActivityDao;

    @GetMapping("/event/count")
    private Mono<ResponseEntity<List<RedditEventCount>>> getEventCount() {
        return redditEventCounterDao.eventStatistics()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/subreddit/list/all")
    private Mono<ResponseEntity<List<SubredditActivity>>> getAllSubreddits() {
        return subredditActivityDao.getAllSubredditsData()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/subreddit/list/all/jou")
    private Mono<ResponseEntity<List<SubredditActivity>>> getAllSubredditsJou() {
        return subredditActivityDao.getAllSubreddits()
                .collectList()
                .map(ResponseEntity::ok);
    }
}
