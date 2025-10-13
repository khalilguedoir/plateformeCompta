package com.example.demo.service;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LiquibaseAuditService {

    private final AuditLogRepository auditLogRepository;

    public LiquibaseAuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logSchemaMigration(String schemaName, String changelogPath) {
        AuditLog log = new AuditLog();
        log.setEntityName("DATABASE_SCHEMA");
        log.setAction("DB_EVENT");
        log.setNewValue("Migration appliquée depuis " + changelogPath);
        log.setTenantId(schemaName);
        log.setUsername("system");

        auditLogRepository.save(log);
    }
}
