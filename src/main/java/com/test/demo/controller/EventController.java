package com.test.demo.controller;

import com.test.demo.mongoDb.RedditEvent;
import com.test.demo.mongoDb.RedditEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    RedditEventDao redditEventDao;

    @GetMapping("/{id}")
    private Mono<ResponseEntity<RedditEvent>> getById(@PathVariable String id) {
        return redditEventDao.findById(id)
                .map(ResponseEntity::ok);
    }
}
