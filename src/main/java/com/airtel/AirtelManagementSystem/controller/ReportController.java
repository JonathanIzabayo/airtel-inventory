package com.airtel.AirtelManagementSystem.controller;

import com.airtel.AirtelManagementSystem.service.AssetService;
import com.airtel.AirtelManagementSystem.service.AssignmentService;
import com.airtel.AirtelManagementSystem.service.AuditService;
import com.airtel.AirtelManagementSystem.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {
    
    private final AssetService assetService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final AuditService auditService;
    
    public ReportController(AssetService assetService, UserService userService, 
                           AssignmentService assignmentService, AuditService auditService) {
        this.assetService = assetService;
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.auditService = auditService;
    }
    
    @GetMapping
    public String showReports(Model model) {
        model.addAttribute("totalAssets", assetService.getAllAssets().size());
        model.addAttribute("availableAssets", assetService.getAvailableAssetsCount());
        model.addAttribute("assignedAssets", assetService.getAssignedAssetsCount());
        model.addAttribute("maintenanceAssets", assetService.getMaintenanceAssetsCount());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("activeAssignments", assignmentService.getActiveAssignments().size());
        model.addAttribute("assetTypes", assetService.getAllAssetTypes());
        model.addAttribute("departments", userService.getAllDepartments());
        model.addAttribute("recentLogs", auditService.getAllAuditLogs());
        return "reports";
    }
    
    @GetMapping("/filter")
    public String filterReports(
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {
        
        // Always add these to the model
        model.addAttribute("totalAssets", assetService.getAllAssets().size());
        model.addAttribute("availableAssets", assetService.getAvailableAssetsCount());
        model.addAttribute("assignedAssets", assetService.getAssignedAssetsCount());
        model.addAttribute("maintenanceAssets", assetService.getMaintenanceAssetsCount());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("activeAssignments", assignmentService.getActiveAssignments().size());
        model.addAttribute("assetTypes", assetService.getAllAssetTypes());
        model.addAttribute("departments", userService.getAllDepartments());
        model.addAttribute("recentLogs", auditService.getAllAuditLogs());
        
        // Handle filter logic
        if (reportType != null && startDate != null && endDate != null) {
            if ("assignments".equals(reportType)) {
                List<com.airtel.AirtelManagementSystem.model.Assignment> filteredAssignments = 
                    assignmentService.getAssignmentsByDateRange(startDate, endDate);
                model.addAttribute("filteredAssignments", filteredAssignments);
                model.addAttribute("filterMessage", "Showing assignments from " + startDate + " to " + endDate);
            } else if ("audit".equals(reportType)) {
                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.atTime(23, 59, 59);
                List<com.airtel.AirtelManagementSystem.model.AuditLog> filteredLogs = 
                    auditService.getAuditLogsByDateRange(start, end);
                model.addAttribute("filteredLogs", filteredLogs);
                model.addAttribute("filterMessage", "Showing audit logs from " + startDate + " to " + endDate);
            }
        }
        
        model.addAttribute("reportType", reportType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "reports";
    }
}