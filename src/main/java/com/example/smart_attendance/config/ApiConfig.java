package com.example.smart_attendance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiConfig {
    private String baseUrl = "http://localhost:8081";
    private String teachersEndpoint = "/teachers";

    public String getTeachersUrl() {
        return baseUrl + teachersEndpoint;
    }
}