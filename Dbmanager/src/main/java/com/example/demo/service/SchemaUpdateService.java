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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SchemaUpdateService {

    @Autowired
    private DataSource dataSource;

    private static final String CHANGELOG_PATH = "db/changelog/master.yml";

    public void updateAllSchemas() {
        for (String schema : getAllSchemas()) {
            updateSchema(schema);
        }
    }

    public void rollbackCountAllSchemas(int count) {
        for (String schema : getAllSchemas()) {
            rollbackCount(schema, count);
        }
    }

    public void rollbackToDateAllSchemas(String date) {
        for (String schema : getAllSchemas()) {
            rollbackToDate(schema, date);
        }
    }

    public void updateSchema(String schemaName) {
        executeLiquibase(schemaName, liquibase -> liquibase.update((String) null),
                "✅ Mise à jour appliquée", "❌ Erreur lors de la mise à jour");
    }

    public void rollbackCount(String schemaName, int count) {
        executeLiquibase(schemaName, liquibase -> liquibase.rollback(count, null),
                "✅ Rollback de " + count + " changeSet(s) effectué",
                "❌ Erreur lors du rollbackCount");
    }

    public void rollbackToDate(String schema, String dateStr) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setSchema(schema);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);

            Date dateCible = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            liquibase.rollback(dateCible, null);

            System.out.println("✅ Rollback appliqué au schéma : " + schema + " jusqu'à la date " + dateStr);
        } catch (Exception e) {
            System.err.println("❌ Erreur rollbackToDate sur le schéma " + schema + ": " + e.getMessage());
        }
    }


    private void executeLiquibase(String schemaName, LiquibaseAction action, String successMsg, String errorMsg) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setSchema(schemaName);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);

            action.run(liquibase);

            System.out.println(successMsg + " pour le schéma : " + schemaName);
        } catch (Exception e) {
            System.err.println(errorMsg + " sur " + schemaName + ": " + e.getMessage());
        }
    }

    private List<String> getAllSchemas() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata " +
                "WHERE schema_name NOT IN ('information_schema', 'pg_catalog', 'public', 'pg_toast')",
                String.class
        );
    }

    @FunctionalInterface
    interface LiquibaseAction {
        void run(Liquibase liquibase) throws Exception;
    }
}
