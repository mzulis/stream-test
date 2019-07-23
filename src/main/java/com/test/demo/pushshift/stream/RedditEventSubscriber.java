package com.test.demo.pushshift.stream;

import com.test.demo.mongoDb.ServerSentEventDao;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

import java.util.Map;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class RedditEventSubscriber {
    Logger logger = Logger.getLogger(RedditEventSubscriber.class);
    @Autowired
    ServerSentEventDao serverSentEventDao;

    @PostConstruct
    public void init() {
        WebClient.create().get()
                .uri("http://stream.pushshift.io")
                .accept(TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(ServerSentEvent.class)
                .flatMap(a -> serverSentEventDao.save(a))
                .repeat()
                .subscribe(e -> logger.info(e.event() + " " + e.id()),
                        Throwable::printStackTrace)
        ;
    }
}
