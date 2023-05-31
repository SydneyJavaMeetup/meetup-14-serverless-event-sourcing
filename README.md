# Sydney Java Meet-Up #12
## Reactive Programming in Java!
https://www.meetup.com/sydney-java/events/290471569/

### Program
6pm - Networking/Drinks/Food  
6:30pm - Event Intro and Job Shouts  
6:35pm - Reactive Coding in Java  
Presenting reactive programming as a paradigm using Java and going through the advantages, use cases and example implementation.
by Dana Dabbagh + Reynaldo Rojas, Technical Leads @ Lexicon  
7:05pm - 25 minute break  
7:30pm - Intro to the Reactive MongoDB Java Driver  
A quick tour of what you can do with MongoDB's reactive driver. Building a microservice with JAX-RS async and reactive Mongo.  
8pm Event Finish

### Intro
Program

Quick acknowledgement of:
* MongoDB for hosting us!

Ethos, vibe
* openness
* inclusiveness
* learning through code, the meetup is organised by coders for coders, with a focus on practical skills and real experiences
* no experts, we're a community of peers all learning together
* supportiveness, we're here for each other

### Job Shouts
“Before we get into the tech talks, are there any job shoutouts?”


# Talks

## Reactive Coding in Java
Over to Dana and Reynaldo!

## Intro to the Reactive Java Driver for MongoDB
Why use Reactive to access a database? 

### Creating Hello World Java 19

IntelliJ -> New Project
- Java 19
- Maven
- Group ID and Artifact ID

##### Basic POM - Always Add Unit Tests!
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.mycodefu</groupId>
  <artifactId>mongo-reactive</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>mongo-reactive</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>19</maven.compiler.source>
    <maven.compiler.target>19</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.13.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```


##### Driver
https://www.mongodb.com/docs/drivers/reactive-streams/  
https://github.com/mongodb/mongo-java-driver/tree/master/driver-reactive-streams  

Add Mongo Driver:
```
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-reactivestreams</artifactId>
    <version>4.8.0</version>
</dependency>
```

Note: Comes with a transitive dependency on Project Reactor - you can use the awesome APIs to abstract away the cumbersome Publisher/Subscriber interfaces.
https://github.com/reactor/reactor-core

```
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
    <version>3.5.0</version>
</dependency>
```

e.g. read from the cursor in batches
```
package com.mycodefu;

import com.github.javafaker.Faker;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.BsonDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        MongoCollection<Person> collection = mongoClient.getDatabase("test").getCollection("people", Person.class);

        DeleteResult deleteResult = Mono.from(collection.deleteMany(new BsonDocument())).block();
        assert(Objects.requireNonNull(deleteResult).wasAcknowledged());

        List<Person> people = Flux.range(0, 101).map(i -> new Person(Faker.instance().name().fullName())).collectList().block();
        InsertManyResult result = Mono.from(collection.insertMany(people)).block();
        assert(Objects.requireNonNull(result).wasAcknowledged());

        Flux.from(collection.find()).buffer(20).doOnNext(System.out::println).blockLast();

        System.out.println("Hello Sydney Java Meetup!");
    }
}
```


You could also use Faker for fun for test data names:
```
<dependency>
    <groupId>com.github.javafaker</groupId>
    <artifactId>javafaker</artifactId>
    <version>1.0.2</version>
</dependency>
```

More useful example:  
https://github.com/Jett59/reactive-mongo-test
