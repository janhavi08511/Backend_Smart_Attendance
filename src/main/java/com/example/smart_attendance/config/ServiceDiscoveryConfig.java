package com.example.smart_attendance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "api.service")
@Getter
@Setter
public class ServiceDiscoveryConfig {
    private String host = "localhost";
    private int port = 8081;
    private String basePath = "";
    private int timeout = 30;

    public String getBaseUrl() {
        return String.format("http://%s:%d%s", host, port, basePath);
    }
}