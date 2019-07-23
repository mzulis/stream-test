package com.test.demo.mongoDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class RedditEventDao {
    @Autowired
    ReactiveMongoTemplate template;

    public Mono<RedditEvent> findById(String id) {
        return template.findById(id, ServerSentEvent.class)
                .map(RedditEvent::new);
    }

    public Flux<RedditEvent> findAll() {
        return template.findAll(ServerSentEvent.class)
                .map(RedditEvent::new);
    }

    public Flux<RedditEvent> findLastMin() {
        long oneMinuteInSec = 60;
        return findSecInPast(oneMinuteInSec);
    }

    public Flux<RedditEvent> findLastHour() {
        long oneHourInSec = 60 * 60;
        return findSecInPast(oneHourInSec);
    }

    public Flux<RedditEvent> findLast24Hour() {
        long hours24InSec = 60 * 60 * 24;
        return findSecInPast(hours24InSec);
    }

    public Flux<RedditEvent> findSecInPast(long secondsInPast) {
        Query query = new Query();
        long epochInPast = Instant.now().minusSeconds(secondsInPast).getEpochSecond();
        query.addCriteria(Criteria.where("data.created_utc").gte(epochInPast));
        return template.find(query, ServerSentEvent.class)
                .map(RedditEvent::new);
    }
}
