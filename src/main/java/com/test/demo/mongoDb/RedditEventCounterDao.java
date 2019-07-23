package com.test.demo.mongoDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class RedditEventCounterDao {
    @Autowired
    ReactiveMongoTemplate template;

    public Flux<RedditEventCount> eventStatistics() {
        GroupOperation groupByEventType = group("event").count().as("count");
        TypedAggregation<ServerSentEvent> typedAggregation = newAggregation(ServerSentEvent.class, groupByEventType);

        return template.aggregate(typedAggregation, RedditEventCount.class);
    }
}
