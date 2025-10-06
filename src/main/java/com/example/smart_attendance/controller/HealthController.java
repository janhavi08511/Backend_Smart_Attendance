package com.example.smart_attendance.controller;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final MongoTemplate mongoTemplate;

    public HealthController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/health")
    public String healthCheck() {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
            return "MongoDB connection successful!";
        } catch (Exception e) {
            return "MongoDB connection failed: " + e.getMessage();
        }
    }
}