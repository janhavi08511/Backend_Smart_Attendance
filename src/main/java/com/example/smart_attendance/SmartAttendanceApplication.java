package com.example.smart_attendance;

import com.example.smart_attendance.config.ApiConfig; // 1. Import your config class
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties; // 2. Import this

@SpringBootApplication
@EnableConfigurationProperties(ApiConfig.class) // 3. Add this annotation
public class SmartAttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartAttendanceApplication.class, args);
    }
}