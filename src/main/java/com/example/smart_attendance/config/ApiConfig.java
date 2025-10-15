package com.example.smart_attendance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiConfig {
    private String baseUrl = "http://192.168.159.253:8081";
    private String teachersEndpoint = "/teacher";

    public String getTeachersUrl() {
        return baseUrl + teachersEndpoint;
    }
}