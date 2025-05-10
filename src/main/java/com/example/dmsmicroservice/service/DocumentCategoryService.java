package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.model.DocumentCategory;
import com.example.dmsmicroservice.repository.DocumentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentCategoryService {

    private final DocumentCategoryRepository categoryRepository;

    public DocumentCategory createCategory(DocumentCategory documentCategory) {
        return categoryRepository.save(documentCategory);
    }

    public List<DocumentCategory> listAllDocumentCategories() {
        return categoryRepository.findAll();
    }

    public DocumentCategory updateCategory(Long id, DocumentCategory updatedCategory) {
        DocumentCategory existing = categoryRepository.findById(id).orElseThrow();
        existing.setName(updatedCategory.getName());

        return categoryRepository.save(existing);

    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
