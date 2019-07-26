### Run

 - download https://github.com/mzulis/stream-test/blob/master/bin/stream-test-0.0.1.jar
 - install java 11
 - execute ./stream-test-0.0.1.jar

### Api endpoints

http://localhost:7070/event/list/all  
http://localhost:7070/event/list/1min  
http://localhost:7070/event/list/1h  
http://localhost:7070/event/list/1d  

http://localhost:7070/statistics/event/count  
http://localhost:7070/statistics/subreddit/list/all  
http://localhost:7070/statistics/subreddit/list/top100  

http://localhost:7070/event/${id}  

### About

Build tool: Maven  
Framework: Spring, Spring-boot, Webflux  
Database: embedded MongoDB  

On startup [RedditEventSubscriber](https://github.com/mzulis/stream-test/blob/master/src/main/java/com/test/demo/pushshift/stream/RedditEventSubscriber.java)
is started in background.
It listens to http://stream.pushshift.io and saves all events in MongoDB.

As system uses embedded MongoDB, on service stop all collected data will be lost.

All /event/list/ endpoints e.g. http://localhost:7070/event/list/all returns event
metadata and path how to get all data about event e.g. http://localhost:7070/event/32351844347

### Why

There is small difference between build tools. But still I have a feeling that with maven
I can achieve result faster. There are lots of people who prefer gradle.

Webflux provides reactive programming support. And one of advantages is that we can easily run more 
than one process in parallel e.g. how it's done in [SubredditActivityDao](https://github.com/mzulis/stream-test/blob/master/src/main/java/com/test/demo/mongoDb/SubredditActivityDao.java)
in getAllSubredditsWithData() method.

MongoDb I used only because I had feeling that I will create this program faster. And program running
instructions will be shorter. 
P.S. This was the first time I used MongoDb.

### To-do if I would want this project to be good

- Create event models
- Extract event collecting process in separate project and run it in separate service
- Don't use embedded database
- Create tests
- Remove some duplicate code
- use more imagination in class and method names