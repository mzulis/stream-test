package com.test.demo.mongoDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SubredditActivityDao {
    @Autowired
    ReactiveMongoTemplate template;


    public Mono<Map<String, SubredditActivity>> subredditsWithRcCount() {
        Aggregation typedAggregation = newAggregation(
                match(where("event").is("rc")),
                group("data.subreddit_id").count().as("rcCount"));

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class)
                .reduce(new HashMap<>(), (map, subreddit) -> {
                    map.put(subreddit.id, subreddit);
                    return map;
                });
    }

    public Mono<Map<String, SubredditActivity>> subredditsWithRsCount() {
        Aggregation typedAggregation = newAggregation(
                match(where("event").is("rs")),
                group("data.subreddit_id").count().as("rsCount"));

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class)
                .reduce(new HashMap<>(), (map, subreddit) -> {
                    map.put(subreddit.id, subreddit);
                    return map;
                });
    }

    public Flux<SubredditActivity> getAllSubreddits() {
        Aggregation typedAggregation = newAggregation(
                match(where("event").in("rs", "rc")),
                group("data.subreddit_id").last("data.subreddit").as("name")
        );

        return template.aggregate(typedAggregation, template.getCollectionName(ServerSentEvent.class), SubredditActivity.class);
    }

    public Mono<List<SubredditActivity>> getAllSubredditsWithData() {
        return Mono.zip(
                getAllSubreddits().collectList(),
                subredditsWithRcCount(),
                subredditsWithRsCount()
        ).map(r -> {
            List<SubredditActivity> all = r.getT1();
            Map<String, SubredditActivity> onlyRc = r.getT2();
            Map<String, SubredditActivity> onlyRs = r.getT3();

            for (SubredditActivity s : all) {
                Long rcCount = onlyRc.containsKey(s.id) ? onlyRc.get(s.id).rcCount : 0;
                Long rsCount = onlyRs.containsKey(s.id) ? onlyRs.get(s.id).rsCount : 0;

                s.rcCount = rcCount;
                s.rsCount = rsCount;
                s.total = rcCount + rsCount;
            }

            return all;
        });
    }
}
