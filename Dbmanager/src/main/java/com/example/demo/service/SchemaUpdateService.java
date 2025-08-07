package com.example.demo.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.DatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Service
public class SchemaUpdateService {

    @Autowired
    private DataSource dataSource;

    private static final String CHANGELOG_PATH = "db/changelog/master.yml";

    public void applyChangelogToAllSchemas() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<String> schemaNames = jdbcTemplate.queryForList(
            "SELECT schema_name FROM information_schema.schemata WHERE schema_name NOT IN ('information_schema', 'pg_catalog', 'public', 'pg_toast')",
            String.class
        );

        for (String schema : schemaNames) {
            try (Connection connection = dataSource.getConnection()) {
                connection.setSchema(schema);

                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);
                liquibase.update((String) null);

                System.out.println("✅ Mise à jour appliquée au schéma : " + schema);
            } catch (Exception e) {
                System.err.println("❌ Erreur sur le schéma " + schema + ": " + e.getMessage());
            }
        }
    }

}
