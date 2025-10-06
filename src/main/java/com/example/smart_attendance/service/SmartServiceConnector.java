package com.example.smart_attendance.service;


import com.example.smart_attendance.config.ServiceDiscoveryConfig;
import com.example.smart_attendance.Exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmartServiceConnector {

    private final RestTemplate restTemplate;
    private final ServiceDiscoveryConfig config;
    private final List<String> fallbackHosts = Arrays.asList("localhost", "127.0.0.1", "0.0.0.0", "[::1]");

    public <T> T executeWithFallback(String endpoint, Class<T> responseType) {
        List<String> hostsToTry = new ArrayList<>();
        hostsToTry.add(config.getHost());
        hostsToTry.addAll(fallbackHosts);

        for (String host : hostsToTry) {
            try {
                String url = buildUrl(host, config.getPort(), config.getBasePath() + endpoint);
                log.info("Trying to connect to: {}", url);

                ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("✅ Successfully connected to: {}", host);
                    return response.getBody();
                }

            } catch (ResourceAccessException e) {
                log.warn("Connection failed to host: {} - {}", host, e.getMessage());
            }
        }

        throw new ServiceUnavailableException("All connection attempts failed for endpoint: " + endpoint);
    }

    private String buildUrl(String host, int port, String path) {
        return String.format("http://%s:%d%s", host, port, path);
    }
}
