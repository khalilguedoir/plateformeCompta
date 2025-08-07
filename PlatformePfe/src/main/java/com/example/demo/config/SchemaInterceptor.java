package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class SchemaInterceptor implements HandlerInterceptor {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String schema = TenantContext.getCurrentTenant();
        if (schema != null) {
            try (Connection conn = dataSourceConfig.adminDataSource().getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("SET search_path TO " + schema);
            }
        }
        return true;
    }
}
