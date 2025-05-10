package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.model.Document;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("translation-requests")
    private String translateRequestTopic;
    
    /**
     * Sends a document title for translation to the translation service
     * @param document The document containing the title to translate
     */
    public void sendForTranslation(Document document, String sourceLang, String targetLang) {
        try {
            System.err.println("we are inside sendForTranslation function");
            Map<String, Object> message = new HashMap<>();
            message.put("documentId", document.getId());
            message.put("text", document.getTitle());
            message.put("requestId", UUID.randomUUID().toString());
            message.put("sourceLanguage", sourceLang);
            message.put("targetLanguage", targetLang);
            
            String payload = objectMapper.writeValueAsString(message);
            
            log.info("Sending document title for translation: {}", document.getTitle());

            System.err.println("we are going to send the doc throw kafka");
            kafkaTemplate.send(translateRequestTopic, document.getId().toString(), payload);
            log.info("Document title sent successfully to topic: {}", translateRequestTopic);
        } catch (JsonProcessingException e) {
            log.error("Error serializing document translation request: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error sending document translation request: {}", e.getMessage(), e);
        }
    }
}