package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log", schema = "public")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;   // ex: DATABASE_SCHEMA, USER, FACTEUR
    private String entityId;     // id de l’entité modifiée (optionnel)
    private String action;       // INSERT / UPDATE / DELETE / DB_EVENT
    @Column(columnDefinition = "TEXT")
    private String oldValue;     // ancienne valeur (JSON ou texte)
    @Column(columnDefinition = "TEXT")
    private String newValue;     // nouvelle valeur (JSON ou texte)
    private String username;     // qui a fait l’action
    private String tenantId;     // société / schéma concerné
    private LocalDateTime createdAt = LocalDateTime.now();

    public AuditLog() {}

    // constructeur pour logs applicatifs
    public AuditLog(String username, String tenantId, String action,
                    String entityName, String details) {
        this.username = username;
        this.tenantId = tenantId;
        this.action = action;
        this.entityName = entityName;
        this.newValue = details;
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
