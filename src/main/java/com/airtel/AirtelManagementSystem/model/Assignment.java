package com.airtel.AirtelManagementSystem.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "assigned_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    
    @Column(name = "return_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    
    @Column(length = 30)
    private String status = "ACTIVE";
    
    @Column(name = "condition_at_return", length = 30)
    private String conditionAtReturn;
    
    @Column(columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public Assignment() {
    }
    
    // All arguments constructor
    public Assignment(Long id, Asset asset, User user, LocalDate assignedDate, LocalDate returnDate, 
                      String status, String conditionAtReturn, String remarks, LocalDateTime createdAt) {
        this.id = id;
        this.asset = asset;
        this.user = user;
        this.assignedDate = assignedDate;
        this.returnDate = returnDate;
        this.status = status;
        this.conditionAtReturn = conditionAtReturn;
        this.remarks = remarks;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Asset getAsset() {
        return asset;
    }
    
    public void setAsset(Asset asset) {
        this.asset = asset;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDate getAssignedDate() {
        return assignedDate;
    }
    
    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getConditionAtReturn() {
        return conditionAtReturn;
    }
    
    public void setConditionAtReturn(String conditionAtReturn) {
        this.conditionAtReturn = conditionAtReturn;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = "ACTIVE";
        if (assignedDate == null) assignedDate = LocalDate.now();
    }
}