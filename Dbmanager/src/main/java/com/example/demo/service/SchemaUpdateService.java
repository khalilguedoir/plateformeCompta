package com.example.demo.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
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

    private static final String PUBLIC_CHANGELOG = "db/changelog/public-changelog.yml";
    private static final String SCHEMA_CHANGELOG = "db/changelog/master.yml";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SchemaUpdateService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void updatePublicSchema() {
        executeLiquibase("public", PUBLIC_CHANGELOG, liquibase -> liquibase.update((String) null),
                "✅ Migration public appliquée", "❌ Erreur migration public");
        logMigration("public", PUBLIC_CHANGELOG, "UPDATE");
    }

    public void rollbackPublicCount(int count) {
        executeLiquibase("public", PUBLIC_CHANGELOG, liquibase -> liquibase.rollback(count, null),
                "✅ Rollback public effectué", "❌ Erreur rollback public");
        logMigration("public", PUBLIC_CHANGELOG, "ROLLBACK_COUNT_" + count);
    }

    public void rollbackPublicToDate(String dateStr) {
        executeLiquibaseToDate("public", PUBLIC_CHANGELOG, dateStr,
                "✅ Rollback public appliqué", "❌ Erreur rollback public");
        logMigration("public", PUBLIC_CHANGELOG, "ROLLBACK_TO_DATE_" + dateStr);
    }

    public void updateSchema(String schemaName) {
        executeLiquibase(schemaName, SCHEMA_CHANGELOG, liquibase -> liquibase.update((String) null),
                "✅ Migration appliquée", "❌ Erreur migration");
        logMigration(schemaName, SCHEMA_CHANGELOG, "UPDATE");
    }

    public void updateAllSchemas() {
        for (String schema : getAllSchemas()) {
            updateSchema(schema);
        }
    }

    public void rollbackCount(String schemaName, int count) {
        executeLiquibase(schemaName, SCHEMA_CHANGELOG, liquibase -> liquibase.rollback(count, null),
                "✅ Rollback de " + count + " changeSets effectué", "❌ Erreur rollback");
        logMigration(schemaName, SCHEMA_CHANGELOG, "ROLLBACK_COUNT_" + count);
    }

    public void rollbackCountAllSchemas(int count) {
        for (String schema : getAllSchemas()) {
            rollbackCount(schema, count);
        }
    }

    public void rollbackToDate(String schemaName, String dateStr) {
        executeLiquibaseToDate(schemaName, SCHEMA_CHANGELOG, dateStr,
                "✅ Rollback appliqué", "❌ Erreur rollback");
        logMigration(schemaName, SCHEMA_CHANGELOG, "ROLLBACK_TO_DATE_" + dateStr);
    }

    public void rollbackToDateAllSchemas(String dateStr) {
        for (String schema : getAllSchemas()) {
            rollbackToDate(schema, dateStr);
        }
    }

    private void executeLiquibase(String schemaName, String changelog, LiquibaseAction action,
                                  String successMsg, String errorMsg) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setSchema(schemaName);
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            action.run(liquibase);
            System.out.println(successMsg + " pour le schéma : " + schemaName);
        } catch (Exception e) {
            System.err.println(errorMsg + " sur " + schemaName + ": " + e.getMessage());
        }
    }

    private void executeLiquibaseToDate(String schemaName, String changelog, String dateStr,
                                        String successMsg, String errorMsg) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setSchema(schemaName);
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            Date dateCible = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            liquibase.rollback(dateCible, null);
            System.out.println(successMsg + " pour le schéma : " + schemaName + " jusqu'à la date " + dateStr);
        } catch (Exception e) {
            System.err.println(errorMsg + " sur " + schemaName + ": " + e.getMessage());
        }
    }

    // -------------------- GET ALL SCHEMAS --------------------
    private List<String> getAllSchemas() {
        return jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata " +
                        "WHERE schema_name NOT IN ('information_schema','pg_catalog','public','pg_toast')",
                String.class
        );
    }

    private void logMigration(String schemaName, String changelogPath, String action) {
        try {
            String sql = "INSERT INTO public.audit_log(entity_name, action, new_value, tenant_id, username) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    "DATABASE_SCHEMA",
                    action,
                    "Migration appliquée depuis " + changelogPath,
                    schemaName,
                    "system"
            );
        } catch (Exception e) {
            System.err.println("❌ Erreur audit sur schema " + schemaName + ": " + e.getMessage());
        }
    }

    @FunctionalInterface
    interface LiquibaseAction {
        void run(Liquibase liquibase) throws Exception;
    }
}
