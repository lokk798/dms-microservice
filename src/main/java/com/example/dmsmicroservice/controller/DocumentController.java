package com.example.dmsmicroservice.controller;

import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // Endpoint for creating a document
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Document> createDocument(@RequestBody Document document, @RequestParam String userId) {
        try {
            Document createdDocument = documentService.createDocument(document, userId);
            return ResponseEntity.status(201).body(createdDocument);
        } catch (Exception e) {
            // Exception handling for invalid entities or failed creation
            return ResponseEntity.status(400).body(null);
        }
    }

    // Endpoint for getting user documents based on search and category
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Document>> getUserDocuments(
            @RequestParam String userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        try {
            List<Document> documents = documentService.getUserDocuments(userId, search, categoryId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            // Handle possible errors like entity not found or other issues
            return ResponseEntity.status(400).body(null);
        }
    }
}