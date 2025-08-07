package com.example.demo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> tenantDataSources = new HashMap<>();
    private final String DEFAULT_TENANT = "admin_db";

    @Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.getCurrentTenant();
        return tenant != null ? tenant : DEFAULT_TENANT;
    }

    public void addDataSource(String tenantId, DataSource dataSource) {
        tenantDataSources.put(tenantId, dataSource);
        super.setTargetDataSources(new HashMap<>(tenantDataSources));
        super.setDefaultTargetDataSource(tenantDataSources.get(DEFAULT_TENANT));
        super.afterPropertiesSet();
    }
}
