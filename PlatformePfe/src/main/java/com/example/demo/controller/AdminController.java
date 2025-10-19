package com.example.demo.controller;

import com.example.demo.config.DataSourceConfig;
import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.Statement;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @PostMapping("/create-company")
    public String createCompany(@RequestBody Company company) {
        if (companyRepository.existsByName(company.getName())) {
            return "Company already exists";
        }

        String schema = company.getName().toLowerCase().replace(" ", "_");

        try (Connection conn = dataSourceConfig.dataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");

            String url = "jdbc:postgresql://localhost:5432/admin_db?currentSchema=" + schema;
            HikariDataSource tenantDataSource = new com.zaxxer.hikari.HikariDataSource();
            tenantDataSource.setJdbcUrl(url);
            tenantDataSource.setUsername("postgres");
            tenantDataSource.setPassword("admin");

            ResourceDatabasePopulator pop = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
            pop.execute(tenantDataSource);

        } catch (Exception e) {
            return "Failed: " + e.getMessage();
        }

        company.setDatabaseName(schema);
        companyRepository.save(company);

        return "Company created with schema: " + schema;
    }
}
