package com.example.dmsmicroservice.controller;

import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.repository.DocumentRepository;
import com.example.dmsmicroservice.service.DocumentPublisher;
import com.example.dmsmicroservice.service.DocumentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private final DocumentPublisher documentPublisher;

    // Endpoint for creating a document
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PostMapping("/{id}/translate/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> translateTitle(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String sourceLang,
            @RequestParam String targetLang) {

        Optional<Document> documentOpt = documentRepository.findById(id);

        System.err.println("trying to translate title of document");

        if (documentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Document document = documentOpt.get();
        System.err.println("sending the document to be translated in sendForTranslate function");
        documentPublisher.sendForTranslation(document, sourceLang, targetLang);

        return ResponseEntity.ok("Translation requested for document ID " + id);
}
}