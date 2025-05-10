package com.example.dmsmicroservice.controller;

import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.repository.DocumentRepository;
import com.example.dmsmicroservice.service.DocumentPublisher;
import com.example.dmsmicroservice.service.DocumentService;
import com.example.dmsmicroservice.service.StorageClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document management and upload operations")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final StorageClient storageClient;
    @Autowired
    private final DocumentPublisher documentPublisher;

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

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFileToDocument(
            @RequestParam("documentId") Long documentId,
            @RequestParam("file") MultipartFile file
    ){
        try{
            // Upload the file to the storage service
            String fileUrl = storageClient.uploadFileAndGetUrl(file);

            // Save document entity with fileUrl
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found !"));

            document.setFileUrl(fileUrl);
            documentRepository.save(document);

            return ResponseEntity.ok(document);


        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
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