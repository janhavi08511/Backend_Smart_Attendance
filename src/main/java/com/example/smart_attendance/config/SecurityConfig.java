package com.example.smart_attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ STEP 1: Disable CSRF protection, as it's not needed for stateless JWT-based APIs.
                .csrf(AbstractHttpConfigurer::disable)

                // ✅ STEP 2: Apply the global CORS configuration defined in the corsConfigurationSource bean below.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ STEP 3: Define authorization rules for your endpoints.
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints that do not require authentication
                        .requestMatchers(
                                "/api/teacher/login",
                                "/api/teacher/signup",
                                "/api/attendence/active-by-beacon", // Student-facing endpoint
                                "/api/attendence/mark"              // Student-facing endpoint
                        ).permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )

                // ✅ STEP 4: Configure session management to be stateless. The server will not create sessions.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ STEP 5: Set the custom authentication provider.
                .authenticationProvider(authenticationProvider)

                // ✅ STEP 6: Add your custom JWT filter to run before the standard username/password filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * ✅ This is the global CORS configuration bean. It tells Spring Security how to handle
     * cross-origin requests for the entire application, including the OPTIONS preflight requests.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from any origin. For production, you should restrict this to your Flutter app's domain.
        configuration.setAllowedOrigins(List.of("*"));

        // Allow common HTTP methods, including the crucial "OPTIONS" for preflight requests.
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow specific headers that your Flutter app will be sending.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths in your application.
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
