package com.test.demo.mongoDb;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class RedditEventCount {
    @Id
    String event;
    Long count;
}
