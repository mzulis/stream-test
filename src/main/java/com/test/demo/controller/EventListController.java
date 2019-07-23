package com.test.demo.controller;

import com.test.demo.mongoDb.RedditEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/event/list")
public class EventListController {

    @Autowired
    RedditEventDao redditEventDao;

    @GetMapping("/all")
    private Mono<ResponseEntity<List<EventData>>> getAll() {
        return redditEventDao.findAll()
                .map(EventData::new)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/1min")
    private Mono<ResponseEntity<List<EventData>>> getOneMin() {
        return redditEventDao.findLastMin()
                .map(EventData::new)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/1h")
    private Mono<ResponseEntity<List<EventData>>> getOneHour() {
        return redditEventDao.findLastHour()
                .map(EventData::new)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/1d")
    private Mono<ResponseEntity<List<EventData>>> getOneDay() {
        return redditEventDao.findLast24Hour()
                .map(EventData::new)
                .collectList()
                .map(ResponseEntity::ok);
    }
}
