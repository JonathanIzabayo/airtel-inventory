package com.airtel.AirtelManagementSystem.controller;

import com.airtel.AirtelManagementSystem.model.SystemUser;
import com.airtel.AirtelManagementSystem.service.AssetService;
import com.airtel.AirtelManagementSystem.service.AssignmentService;
import com.airtel.AirtelManagementSystem.service.CustomUserDetailsService;
import com.airtel.AirtelManagementSystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {
    
    private final AssignmentService assignmentService;
    private final AssetService assetService;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    
    public AssignmentController(AssignmentService assignmentService, AssetService assetService, 
                                UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.assignmentService = assignmentService;
        this.assetService = assetService;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    @GetMapping
    public String listAssignments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SystemUser currentUser = customUserDetailsService.getSystemUserByUsername(username);
        
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        model.addAttribute("activeAssignments", assignmentService.getActiveAssignments());
        model.addAttribute("totalAssignments", assignmentService.getAllAssignments().size());
        model.addAttribute("userRole", currentUser != null ? currentUser.getRole() : "USER");
        return "assignments";
    }
    
    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SystemUser currentUser = customUserDetailsService.getSystemUserByUsername(username);
        
        // Check if user has permission (ADMIN or MANAGER)
        if (!"ADMIN".equals(currentUser.getRole()) && !"MANAGER".equals(currentUser.getRole())) {
            return "redirect:/access-denied";
        }
        
        model.addAttribute("availableAssets", assetService.getAssetsByStatus("AVAILABLE"));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("totalAvailableAssets", assetService.getAssetsByStatus("AVAILABLE").size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("userRole", currentUser.getRole());
        return "assign";
    }
    
    @PostMapping("/assign")
    public String assignAsset(@RequestParam Long assetId, 
                              @RequestParam Long userId, 
                              @RequestParam(required = false) String remarks,
                              RedirectAttributes redirectAttributes) {
        try {
            assignmentService.assignAsset(assetId, userId, remarks);
            redirectAttributes.addFlashAttribute("success", "Asset assigned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assignments";
    }
    
    @GetMapping("/return/{id}")
    public String showReturnForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SystemUser currentUser = customUserDetailsService.getSystemUserByUsername(username);
        
        model.addAttribute("assignment", assignmentService.getAssignmentById(id));
        model.addAttribute("userRole", currentUser.getRole());
        return "return";
    }
    
    @PostMapping("/return/{id}")
    public String returnAsset(@PathVariable Long id,
                              @RequestParam String conditionAtReturn,
                              @RequestParam(required = false) String remarks,
                              RedirectAttributes redirectAttributes) {
        try {
            assignmentService.returnAsset(id, conditionAtReturn, remarks);
            redirectAttributes.addFlashAttribute("success", "Asset returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assignments";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAssignment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SystemUser currentUser = customUserDetailsService.getSystemUserByUsername(username);
        
        // Only ADMIN can delete assignments
        if (!"ADMIN".equals(currentUser.getRole())) {
            return "redirect:/access-denied";
        }
        
        try {
            assignmentService.deleteAssignment(id);
            redirectAttributes.addFlashAttribute("success", "Assignment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assignments";
    }
}