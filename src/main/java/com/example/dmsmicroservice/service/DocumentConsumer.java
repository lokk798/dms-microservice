package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.model.Document;
import com.example.dmsmicroservice.repository.DocumentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer {

    private final DocumentRepository documentRepository;
    private final ObjectMapper objectMapper;

    /**
     * Listens for translated document titles from the translation service
     * @param message The message containing the translated title
     */
    @KafkaListener(topics = "translation-responses", groupId = "translation-group")
    public void handleTranslatedTitle(String message) {
        try {
            System.err.println("weeeeeee we are the in the final step, the second consumerrrr");
            System.err.println(message);
            log.info("Received translated title: {}", message);
            
            // Parse the message
            Map<String, Object> translationResponse = objectMapper.readValue(message, Map.class);

            System.err.println("parsing the message");
            System.err.println(translationResponse);

            if(translationResponse == null){
                System.err.println("NOOOOOoooooOOO the translationResponse is empty (the mapping)");
            }
            
            Long documentId = Long.valueOf(translationResponse.get("documentId").toString());
            String translatedTitle = (String) translationResponse.get("translatedText");

            System.err.println("extracting values, which are:");
            System.err.println(documentId);
            System.err.println(translatedTitle);
            
            // Update the document with the translated title
            Optional<Document> optionalDocument = documentRepository.findById(documentId);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                document.setTitleTranslation(translatedTitle);
                documentRepository.save(document);
                log.info("Updated document ID {} with translated title: {}", documentId, translatedTitle);
                System.err.println("Updated document ID {} with translated title: {}");
            } else {
                log.warn("Document with ID {} not found for translation update", documentId);
                System.err.println("we couldn't update the doc translation");
            }
            
        } catch (JsonProcessingException e) {
            log.error("Error deserializing translated title message: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error handling translated title: {}", e.getMessage(), e);
        }
    }
}
