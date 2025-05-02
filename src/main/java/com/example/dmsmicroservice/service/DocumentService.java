package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.model.Department;
import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.model.DocumentCategory;
import com.example.dmsmicroservice.model.User;
import com.example.dmsmicroservice.repository.DepartmentRepository;
import com.example.dmsmicroservice.repository.DocumentCategoryRepository;
import com.example.dmsmicroservice.repository.DocumentRepository;
import com.example.dmsmicroservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DepartmentRepository departmentRepository;
    private final DocumentCategoryRepository categoryRepository;
    private final UserRepository userRepository;
//    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Document createDocument(Document document, String userId) {
        // Fetch department and validate it exists
        Department department = departmentRepository.findById(document.getDepartment().getId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        // Check if user is assigned to the department
        boolean isUserAssigned = department.getUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(userId));

        if (!isUserAssigned) {
            throw new AccessDeniedException("User not assigned to this department");
        }

        // Fetch category and user
        DocumentCategory category = categoryRepository.findById(document.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Set relationships
        document.setDepartment(department);
        document.setCategory(category);
        document.setCreatedBy(user);
        document.setCreatedAt(LocalDateTime.now());

        return documentRepository.save(document);
    }




    public List<Document> getUserDocuments(String userId, String search, Long categoryId) {
        // Get only departments user is assigned to
        List<Department> departments = departmentRepository.findByUsersId(userId);

        if (departments.isEmpty()) {
            return List.of(); // Return empty list if user has no departments
        }

        if (search != null && categoryId != null) {
            DocumentCategory category = categoryRepository.findById(categoryId).orElseThrow();
            return documentRepository.findByDepartmentInAndCategoryAndTitleContaining(
                    departments, category, search);
        } else if (search != null) {
            return documentRepository.findByTitleContainingAndDepartmentIn(search, departments);
        } else if (categoryId != null) {
            DocumentCategory category = categoryRepository.findById(categoryId).orElseThrow();
            return documentRepository.findByCategoryAndDepartmentIn(category, departments);
        } else {
            return documentRepository.findByDepartmentIn(departments);
        }
    }

}




