package com.airtel.AirtelManagementSystem.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String action;
    
    @Column(name = "asset_id")
    private Long assetId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private LocalDateTime timestamp;
    
    // Default constructor
    public AuditLog() {
    }
    
    // All arguments constructor
    public AuditLog(Long id, String action, Long assetId, Long userId, String description, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.assetId = assetId;
        this.userId = userId;
        this.description = description;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Long getAssetId() {
        return assetId;
    }
    
    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}