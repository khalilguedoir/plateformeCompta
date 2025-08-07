package com.example.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("tenant_route", r -> r
                .path("/api/{tenant}/**")
                .filters(f -> f
                    .rewritePath("/api/(?<tenant>[^/]+)(?<segment>/.*)", "${segment}")
                )
                .uri("http://localhost:8080")
            )
            .build();
    }
}
