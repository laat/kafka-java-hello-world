package com.example.shared;

public class Person {
    String firstName;
    String lastName;

    public Person() {
    }

    public Person(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.firstName;
    }

    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }
}