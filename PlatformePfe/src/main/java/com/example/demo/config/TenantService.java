package com.example.demo.config;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class TenantService {

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private DataSourceConfig dsConfig;

    /**
     * Crée un schéma vide pour le tenant si celui-ci n'existe pas
     */
    public void createSchemaIfNotExists(String schemaName) {
        String sql = "CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"";
        try (Connection conn = dsConfig.adminDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Schéma créé avec succès : " + schemaName);

        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la création du schéma : " + schemaName, e);
        }
    }

    /**
     * Crée le tenant pour la société : 
     * - Crée le schéma vide
     * - Met à jour le nom de la base dans l'entité Company
     */
    @Transactional
    public Company createTenantForCompany(Company company) {
        String schema = company.getName().toLowerCase().replace(" ", "_");

        // 1️⃣ Création du schéma vide
        createSchemaIfNotExists(schema);

        // 2️⃣ Enregistrement du nom du schéma dans la société
        company.setDatabaseName(schema);
        Company savedCompany = companyRepo.save(company);

        System.out.println("✅ Tenant créé pour la société : " + company.getName() + " -> " + schema);
        return savedCompany;
    }
}
