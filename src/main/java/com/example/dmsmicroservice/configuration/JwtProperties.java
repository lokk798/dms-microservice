package com.example.dmsmicroservice.configuration;



import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    @Value("${jwt.secret-key}")
    private String secret;
    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";
    private long expirationTime = 86400000; // 1 day
}
