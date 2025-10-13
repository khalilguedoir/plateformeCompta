package com.example.demo.service;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void logAction(String username, String tenantId,
                          String action, String entityName, String details) {
        AuditLog log = new AuditLog(username, tenantId, action, entityName, details);
        repository.save(log);
    }
}
