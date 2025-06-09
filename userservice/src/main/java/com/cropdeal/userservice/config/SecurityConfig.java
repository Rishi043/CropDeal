package com.cropdeal.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ✅ Ensures password encryption
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // ✅ Properly configured AuthenticationManager bean
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // ✅ Correct way to disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/auth/**").permitAll() // ✅ Ensure Register & Login bypass authentication
                        .requestMatchers("/users/profile").authenticated() // ✅ Require authentication for profile
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dealer/**").hasRole("DEALER")
                        .requestMatchers("/farmer/**").hasRole("FARMER")
                        .anyRequest().authenticated()
                )
                .build();
    }
}
