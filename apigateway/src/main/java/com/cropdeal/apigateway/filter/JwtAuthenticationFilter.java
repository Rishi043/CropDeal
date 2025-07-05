package com.cropdeal.apigateway.filter;

import com.cropdeal.apigateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;



    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        logger.info("Incoming request path: {}", path);

        if (path.contains("/users/auth/login") || path.contains("/users/auth/register")) {
            logger.info("Public endpoint accessed: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
 
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            logger.warn("Invalid JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String role = jwtUtil.getRoleFromToken(token);
        logger.info("Authenticated role: {}", role);

        switch (role) {
            case "ADMIN":
                logger.info("Access granted to ADMIN");
                return chain.filter(exchange);

            case "FARMER":
                if (path.startsWith("/crops")
                        ||path.startsWith("/crops/")
                        || path.startsWith("/payment/")
                        || path.startsWith("/payment")){
                    logger.info("Access granted to FARMER for path: {}", path);
                    return chain.filter(exchange);
                }
                break;

            case "DEALER":
                if (path.startsWith("/orders") || path.startsWith("/orders/")
                        || path.startsWith("/payment/")
                        || path.startsWith("/payment")) {
                    logger.info("Access granted to DEALER for path: {}", path);
                    return chain.filter(exchange);
                }
                break;

            default:
                logger.warn("Unknown role or access denied: {}", role);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
        }

        logger.warn("Access denied for role: {} on path: {}", role, path);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}





