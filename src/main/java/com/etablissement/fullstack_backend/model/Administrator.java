package com.etablissement.fullstack_backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    // Default constructor
    public Administrator() {
        super();
    }

    // Constructor with fields
    public Administrator(String username, String password, String email, String firstName, String lastName) {
        super(username, password, email, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Administrator{" + super.toString() + "}";
    }
}
