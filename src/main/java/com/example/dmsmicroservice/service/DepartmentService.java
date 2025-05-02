package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.model.Department;
import com.example.dmsmicroservice.model.User;
import com.example.dmsmicroservice.repository.DepartmentRepository;
import com.example.dmsmicroservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> listAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department existing = departmentRepository.findById(id).orElseThrow();
        existing.setName(updatedDepartment.getName());

        return departmentRepository.save(existing);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public Department assignUser(Long departmentId, String userId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.save(new User(userId))); // create stub if not exists

        department.getUsers().add(user);
        return departmentRepository.save(department);
    }

    public Department unassignUser(Long departmentId, String userId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        department.getUsers().remove(user);
        return departmentRepository.save(department);
    }


}
