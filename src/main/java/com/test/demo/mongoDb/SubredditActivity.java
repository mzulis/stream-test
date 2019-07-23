package com.test.demo.mongoDb;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SubredditActivity {
    @Id
    String id;
    String name;
    Long rcCount;
    Long rsCount;
    Long total;
}
