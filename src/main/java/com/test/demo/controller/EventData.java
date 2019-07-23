package com.test.demo.controller;

import com.test.demo.mongoDb.RedditEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventData {
    String id;
    String type;
    String url;

    public EventData(RedditEvent redditEvent) {
        this(redditEvent.id, redditEvent.event, "/event/"+redditEvent.id);
    }
}
