package com.example.smart_attendance.config;

import com.example.smart_attendance.service.TeacherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class provides the essential beans required by Spring Security for authentication.
 */
@Configuration
public class ApplicationConfig {

    private final TeacherService teacherService;

    // Spring will inject the TeacherService bean here.
    public ApplicationConfig(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * ✅ FIXED: Renamed this bean to 'teacherUserDetailsService' to avoid conflict
     * with the bean of the same name in Userconfig.java.
     * This bean tells Spring Security how to load a user by their teacherId.
     */
    @Bean
    public UserDetailsService teacherUserDetailsService() {
        return username -> teacherService.findByTeacherId(username)
                .map(teacher -> User.builder()
                        .username(teacher.getTeacherId())
                        .password(teacher.getPassword())
                        .roles("TEACHER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Teacher not found with ID: " + username));
    }

    /**
     * This is the bean that was missing. It's the primary authentication provider.
     * We're using a DaoAuthenticationProvider, which is the standard implementation that
     * uses a UserDetailsService and a PasswordEncoder to validate credentials.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // ✅ FIXED: Updated to use the renamed 'teacherUserDetailsService' bean.
        authProvider.setUserDetailsService(teacherUserDetailsService());
        // Set the password encoder that hashes and compares passwords.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * This bean defines the password hashing algorithm to be used (BCrypt).
     * It's crucial for securely storing and verifying passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager from the security configuration as a bean.
     * This is often needed in other parts of the application, like manual login controllers.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

