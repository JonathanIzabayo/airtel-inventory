package com.airtel.AirtelManagementSystem.controller;

import com.airtel.AirtelManagementSystem.model.Assignment;
import com.airtel.AirtelManagementSystem.model.SystemUser;
import com.airtel.AirtelManagementSystem.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    
    private final AssetService assetService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final AuditService auditService;
    private final CustomUserDetailsService customUserDetailsService;
    
    public DashboardController(AssetService assetService, UserService userService, 
                               AssignmentService assignmentService, AuditService auditService,
                               CustomUserDetailsService customUserDetailsService) {
        this.assetService = assetService;
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.auditService = auditService;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @GetMapping("/dashboard")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SystemUser currentUser = customUserDetailsService.getSystemUserByUsername(username);
        
        model.addAttribute("totalAssets", assetService.getAllAssets().size());
        model.addAttribute("availableAssets", assetService.getAvailableAssetsCount());
        model.addAttribute("assignedAssets", assetService.getAssignedAssetsCount());
        model.addAttribute("maintenanceAssets", assetService.getMaintenanceAssetsCount());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userRole", currentUser != null ? currentUser.getRole() : "USER");
        
        if ("ADMIN".equals(currentUser.getRole())) {
            model.addAttribute("totalUsers", userService.getAllUsers().size());
            model.addAttribute("activeAssignments", assignmentService.getActiveAssignments().size());
            model.addAttribute("recentAssets", assetService.getAllAssets().stream().limit(10).collect(Collectors.toList()));
            model.addAttribute("recentAssignments", assignmentService.getActiveAssignments().stream().limit(10).collect(Collectors.toList()));
            model.addAttribute("recentLogs", auditService.getAllAuditLogs().stream().limit(20).collect(Collectors.toList()));
        } else if ("MANAGER".equals(currentUser.getRole())) {
            List<Assignment> departmentAssignments = assignmentService.getAllAssignments().stream()
                .filter(a -> a.getUser() != null && currentUser.getDepartment() != null && 
                       currentUser.getDepartment().equals(a.getUser().getDepartment()))
                .collect(Collectors.toList());
            model.addAttribute("departmentAssignments", departmentAssignments);
            model.addAttribute("departmentAssets", assetService.getAllAssets().stream().limit(10).collect(Collectors.toList()));
            model.addAttribute("activeAssignments", departmentAssignments.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count());
        } else {
            List<Assignment> myAssignments = assignmentService.getAssignmentsByUser(currentUser.getId());
            model.addAttribute("myAssignments", myAssignments);
            model.addAttribute("myActiveAssignments", myAssignments.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count());
            model.addAttribute("myReturnedAssignments", myAssignments.stream().filter(a -> "RETURNED".equals(a.getStatus())).count());
        }
        
        return "index";
    }
    
    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }
}