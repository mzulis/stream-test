package com.test.demo.mongoDb;

import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;

import java.util.Map;

@AllArgsConstructor
public class RedditEvent {
    public String id;
    public String event;
    public Map<String, Object> data;

    public RedditEvent(ServerSentEvent event) {
        super();
        this.id = event.id();
        this.event = event.event();
        this.data = (Map<String, Object>) event.data();
    }
}
