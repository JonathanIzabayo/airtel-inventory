package com.airtel.AirtelManagementSystem.controller;

import com.airtel.AirtelManagementSystem.model.Asset;
import com.airtel.AirtelManagementSystem.service.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assets")
public class AssetController {
    
    private final AssetService assetService;
    
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }
    
    // READ - List all assets
    @GetMapping
    public String listAssets(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("assets", assetService.searchAssets(search));
            model.addAttribute("searchKeyword", search);
        } else {
            model.addAttribute("assets", assetService.getAllAssets());
        }
        model.addAttribute("assetTypes", assetService.getAllAssetTypes());
        return "assets";
    }
    
    // CREATE - Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("asset", new Asset());
        model.addAttribute("assetTypes", assetService.getAllAssetTypes());
        return "add-asset";
    }
    
    // CREATE - Save new asset
    @PostMapping("/add")
    public String addAsset(@ModelAttribute Asset asset, RedirectAttributes redirectAttributes) {
        try {
            assetService.addAsset(asset);
            redirectAttributes.addFlashAttribute("success", "Asset added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assets";
    }
    
    // UPDATE - Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("asset", assetService.getAssetById(id));
        model.addAttribute("assetTypes", assetService.getAllAssetTypes());
        return "add-asset";
    }
    
    // UPDATE - Update existing asset
    @PostMapping("/edit/{id}")
    public String updateAsset(@PathVariable Long id, @ModelAttribute Asset asset, RedirectAttributes redirectAttributes) {
        try {
            assetService.updateAsset(id, asset);
            redirectAttributes.addFlashAttribute("success", "Asset updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assets";
    }
    
    // DELETE - Delete asset
    @GetMapping("/delete/{id}")
    public String deleteAsset(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            assetService.deleteAsset(id);
            redirectAttributes.addFlashAttribute("success", "Asset deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/assets";
    }
}