package com.cropdeal.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable) // ✅ Correct WebFlux CSRF disable syntax
                .authorizeExchange(ex -> ex.anyExchange().permitAll()) // ✅ Allow all requests
                .build();
    }
}
