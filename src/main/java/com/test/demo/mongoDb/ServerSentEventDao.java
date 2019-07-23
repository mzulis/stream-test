package com.test.demo.mongoDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ServerSentEventDao {
    @Autowired
    ReactiveMongoTemplate template;

    public Mono<ServerSentEvent> save(ServerSentEvent account) {
        return template.save(account);
    }
}
