package com.test.demo.controller;

import com.test.demo.mongoDb.RedditEventCount;
import com.test.demo.mongoDb.RedditEventCounterDao;
import com.test.demo.mongoDb.SubredditActivity;
import com.test.demo.mongoDb.SubredditActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

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
    private Mono<ResponseEntity<Collection<SubredditActivity>>> getAllSubreddits() {
        return subredditActivityDao.getAllSubredditsWithData()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/subreddit/list/top100")
    private Mono<ResponseEntity<List<SubredditActivity>>> getTop100Subreddits() {
        return subredditActivityDao.getAllSubredditsWithData()
                .map(l -> l.stream()
                        .sorted(comparing(SubredditActivity::getTotal).reversed())
                        .limit(100)
                        .collect(toList()))
                .map(ResponseEntity::ok);
    }
}
