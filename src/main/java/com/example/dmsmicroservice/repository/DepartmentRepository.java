package com.example.dmsmicroservice.repository;

import com.example.dmsmicroservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByUsersId(String userId);
}
