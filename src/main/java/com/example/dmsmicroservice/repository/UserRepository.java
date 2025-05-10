package com.example.dmsmicroservice.repository;

import com.example.dmsmicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
