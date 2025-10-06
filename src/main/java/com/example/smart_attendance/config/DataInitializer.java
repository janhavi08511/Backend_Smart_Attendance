package com.example.smart_attendance.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            System.out.println("Application started successfully!");
            // Remove MongoDB-dependent code temporarily
        };
    }
}