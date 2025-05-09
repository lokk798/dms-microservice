package com.example.dmsmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String translatedTitle; // This will be populated by the translation service.

    @ManyToOne
    private DocumentCategory category;

    @ManyToOne
    private Department department;

    private LocalDateTime createdAt;

    @ManyToOne
    private User createdBy;

    @Column(length = 1000)
    private String fileUrl;
}
