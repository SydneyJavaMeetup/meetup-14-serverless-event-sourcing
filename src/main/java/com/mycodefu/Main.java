package com.mycodefu;

import com.github.javafaker.Faker;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Main {

    /**
     * Simple demo of how to adapt the Publisher/Subscriber reactive streams API of MongoDB Reactive Driver
     * to the Project Reactor helper classes Mono and Flux to make it easier to work with.
     */
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("test-sydney");
        MongoCollection<Person> peopleCollection = database.getCollection("people", Person.class);

        DeleteResult deleteResult = Mono.from(peopleCollection.deleteMany(new BsonDocument())).block();
        System.out.println("Deleted " + deleteResult.getDeletedCount() + " people");

        //Create a range in project reactor, and map it to 1000 fake people
        List<Person> people = Flux
                .range(1, 1000)
                .map(Person::fake)
                .collectList().block();
        System.out.println(people);

        InsertManyResult insertManyResult = Mono.from(peopleCollection.insertMany(people)).block();
        System.out.println("Inserted " + insertManyResult.getInsertedIds().size() + " people");

        //Add a John person
        InsertManyResult insertManyResult2 = Mono.from(peopleCollection.insertMany(List.of(new Person(1, "John", "Johnson", "jj@j.com", Faker.instance().pokemon().name())))).block();
        System.out.println("Inserted " + insertManyResult2.getInsertedIds().size() + " John people");

        //Find all people with the first name "John"
        List<Person> peopleWithFirstNameJohn = Flux.from(peopleCollection.find(eq("firstName", "John"))).collectList().block();
        System.out.println("People with first name John:");
        System.out.println(peopleWithFirstNameJohn);

        System.out.println("Hello Sydney Java Meetup!");
    }
}