package com.airtel.AirtelManagementSystem.service;

import com.airtel.AirtelManagementSystem.model.Asset;
import com.airtel.AirtelManagementSystem.model.Assignment;
import com.airtel.AirtelManagementSystem.model.User;
import com.airtel.AirtelManagementSystem.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    private final AssetService assetService;
    private final UserService userService;
    private final AuditService auditService;
    
    public AssignmentService(AssignmentRepository assignmentRepository, AssetService assetService, 
                            UserService userService, AuditService auditService) {
        this.assignmentRepository = assignmentRepository;
        this.assetService = assetService;
        this.userService = userService;
        this.auditService = auditService;
    }
    
    public Assignment assignAsset(Long assetId, Long userId, String remarks) {
        Asset asset = assetService.getAssetById(assetId);
        User user = userService.getUserById(userId);
        
        Assignment assignment = new Assignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setAssignedDate(LocalDate.now());
        assignment.setStatus("ACTIVE");
        assignment.setRemarks(remarks);
        
        if (asset != null) {
            asset.setStatus("ASSIGNED");
            assetService.updateAssetStatus(assetId, "ASSIGNED");
        }
        
        Assignment savedAssignment = assignmentRepository.save(assignment);
        
        auditService.logAction("ASSET_ASSIGNED", assetId, userId,
            "Asset assigned to " + (user != null ? user.getFullName() : "Unknown"));
        
        return savedAssignment;
    }
    
    public Assignment returnAsset(Long assignmentId, String conditionAtReturn, String remarks) {
        Assignment assignment = getAssignmentById(assignmentId);
        
        if (assignment != null) {
            assignment.setReturnDate(LocalDate.now());
            assignment.setStatus("RETURNED");
            assignment.setConditionAtReturn(conditionAtReturn);
            assignment.setRemarks(remarks);
            
            Asset asset = assignment.getAsset();
            if (asset != null) {
                asset.setStatus("AVAILABLE");
                assetService.updateAssetStatus(asset.getId(), "AVAILABLE");
            }
            
            Assignment savedAssignment = assignmentRepository.save(assignment);
            
            auditService.logAction("ASSET_RETURNED", assignment.getAsset() != null ? assignment.getAsset().getId() : null, 
                assignment.getUser() != null ? assignment.getUser().getId() : null,
                "Asset returned");
            
            return savedAssignment;
        }
        return null;
    }
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }
    
    public List<Assignment> getActiveAssignments() {
        return assignmentRepository.findActiveAssignments();
    }
    
    public List<Assignment> getAssignmentsByAsset(Long assetId) {
        return assignmentRepository.findByAssetId(assetId);
    }
    
    public List<Assignment> getAssignmentsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }
    
    public List<Assignment> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return assignmentRepository.findByAssignedDateBetween(startDate, endDate);
    }
    
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        if (assignment != null) {
            if ("ACTIVE".equals(assignment.getStatus()) && assignment.getAsset() != null) {
                Asset asset = assignment.getAsset();
                asset.setStatus("AVAILABLE");
                assetService.updateAssetStatus(asset.getId(), "AVAILABLE");
            }
            auditService.logAction("ASSIGNMENT_DELETED", 
                assignment.getAsset() != null ? assignment.getAsset().getId() : null, 
                assignment.getUser() != null ? assignment.getUser().getId() : null,
                "Assignment deleted");
            assignmentRepository.deleteById(id);
        }
    }
}