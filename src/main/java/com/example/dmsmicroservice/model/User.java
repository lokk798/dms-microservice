package com.example.dmsmicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_table")
public class User {
    // I am assuming that the ID is the username.
    // To store ID from auth service.
    @Id
    private String id;

    // No need to implement the other fields since user data
    // is managed by the Authentication Microservice.
}
