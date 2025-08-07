package com.example.demo.config;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class TenantService {

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private DataSourceConfig dsConfig;


    public void createSchemaIfNotExists(String schemaName) {
        String sqlScript = """
            CREATE SCHEMA IF NOT EXISTS %s;
            SET search_path TO %s;
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(100) NOT NULL,
                password TEXT NOT NULL,
                role VARCHAR(50) NOT NULL
            );
        """.formatted(schemaName, schemaName);

        DataSource dataSource = dsConfig.createDataSource("admin_db");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(sqlScript);
    }
    
    
    @Transactional
    public Company createTenantForCompany(Company company) {
        String schema = company.getName().toLowerCase().replace(" ", "_");

        JdbcTemplate jdbc = new JdbcTemplate(dsConfig.adminDataSource());
        jdbc.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");

        DataSource companyDataSource = dsConfig.createDataSource(schema);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(companyDataSource);

        company.setDatabaseName(schema);
        return companyRepo.save(company);
    }
}
