package com.test.demo.mongoDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SubredditActivityDao {
    @Autowired
    ReactiveMongoTemplate template;

    public Mono<SubredditActivity> subredditRcCount(String subreddit_id) {
        Aggregation typedAggregation = newAggregation(
                match(where("event").is("rc")),
                match(where("data.subreddit_id").is(subreddit_id)),
                group("data.subreddit_id").count().as("rcCount"));

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class).next();
    }

    public Mono<SubredditActivity> subredditRsCount(String subreddit_id) {
        Aggregation typedAggregation = newAggregation(
                match(where("event").is("rs")),
                match(where("data.subreddit_id").is(subreddit_id)),
                group("data.subreddit_id").count().as("rsCount"));

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class).next();
    }

    public Flux<SubredditActivity> getAllSubredditsData() {
        return getAllSubreddits().flatMap(subreddit -> Mono.zip(
                subredditRcCount(subreddit.id),
                subredditRsCount(subreddit.id)
                ).map(a -> {
                    subreddit.rcCount = a.getT1().rcCount;
                    subreddit.rsCount = a.getT2().rsCount;
                    return subreddit;
                })
        );
    }

    public Flux<SubredditActivity> getAllSubreddits() {
        Aggregation typedAggregation = newAggregation(
                match(where("event").in("rs", "rc")),
                group("data.subreddit_id").last("data.subreddit").as("name")
        );

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class);
    }
}
