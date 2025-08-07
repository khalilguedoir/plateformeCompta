package com.example.demo.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class TenantHeaderRequestWrapper extends HttpServletRequestWrapper {

    private final String tenantId;

    public TenantHeaderRequestWrapper(HttpServletRequest request, String tenantId) {
        super(request);
        this.tenantId = tenantId;
    }

    @Override
    public String getHeader(String name) {
        if ("X-Tenant-ID".equalsIgnoreCase(name)) {
            return tenantId;
        }
        return super.getHeader(name);
    }
}
