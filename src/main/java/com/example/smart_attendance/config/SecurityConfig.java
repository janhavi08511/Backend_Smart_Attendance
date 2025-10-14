package com.example.smart_attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // ✅ Allow public access to login, signup, and attendance marking
                        .requestMatchers(
                                "/api/teacher/login",
                                "/api/teacher/signup",
                                "/api/attendence/{attendanceId}/active",
                                "/api/attendence/mark",
<<<<<<< HEAD
                                "api/student/mark"
=======
                                "/api/student/mark"
>>>>>>> 8e9bef8d6ac2081e7f9c038a5fed51536e17582d

                        ).permitAll()
                        // ✅ All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}