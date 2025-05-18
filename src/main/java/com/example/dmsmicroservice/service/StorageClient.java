package com.example.dmsmicroservice.service;

import com.example.dmsmicroservice.utils.MultipartInputStreamFileResource;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.IOException;


@Service
public class StorageClient {
    private final RestTemplate restTemplate;

    public StorageClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String uploadFileAndGetUrl(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity("http://localhost:8083/api/storage/upload", requestEntity, String.class);

        // Get pre-signed URL
        String url = restTemplate.getForObject(
                "http://localhost:8083/api/storage/url?fileName=" + file.getOriginalFilename(),
                String.class
        );

        return url;
    }
}



