package com.example.dmsmicroservice.repository;

import com.example.dmsmicroservice.model.Department;
import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.model.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByDepartmentIn(List<Department> departments);

    List<Document> findByTitleContainingAndDepartmentIn(String title, List<Department> departments);

    List<Document> findByCategoryAndDepartmentIn(DocumentCategory category, List<Department> departments);

    List<Document> findByDepartmentInAndCategoryAndTitleContaining(List<Department> departments, DocumentCategory category, String title);

}
