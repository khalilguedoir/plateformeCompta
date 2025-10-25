package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String DEFAULT_TENANT = "public"; // ou "admin_db" si tu veux travailler sur ce nom

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== FILTER START ===");

        String tenantId = request.getHeader("X-Tenant-ID");
        System.out.println("=== TENANT ID HEADER === " + tenantId);

        if (tenantId == null || tenantId.isBlank()) {
            // Set a default tenant instead of leaving it null
            tenantId = DEFAULT_TENANT;
            System.out.println("=== TENANT ID not provided. Using default: " + DEFAULT_TENANT);
        }

        TenantContext.setCurrentTenant(tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            System.out.println("=== FILTER END ===");
        }
    }
}
