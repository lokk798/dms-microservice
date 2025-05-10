package com.example.dmsmicroservice.repository;

import com.example.dmsmicroservice.model.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
}
