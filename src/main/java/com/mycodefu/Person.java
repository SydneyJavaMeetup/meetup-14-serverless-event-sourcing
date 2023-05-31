package com.mycodefu;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

public record Person(int staffId, String firstName, String lastName, String email, String pokemon) {
    private static Faker faker = Faker.instance();
    public static Person fake(int id) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress(firstName.toLowerCase());
        String pokemon = faker.pokemon().name();
        Person person = new Person(id, firstName, lastName, email, pokemon);
        return person;
    }
}
