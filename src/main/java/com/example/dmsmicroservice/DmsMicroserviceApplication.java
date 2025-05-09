package com.example.dmsmicroservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Document Microservice API",
                version = "1.0",
                description = "Handles metadata management and file uploads via the S3 microservice",
                contact = @Contact(name = "Loukmane DAOUDI", email = "loukmane.daoudi@ensia.edu.dz")
        )
)
@SpringBootApplication
public class DmsMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmsMicroserviceApplication.class, args);
    }

}
