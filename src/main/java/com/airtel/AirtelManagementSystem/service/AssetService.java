package com.airtel.AirtelManagementSystem.service;

import com.airtel.AirtelManagementSystem.model.Asset;
import com.airtel.AirtelManagementSystem.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    
    private final AssetRepository assetRepository;
    private final AuditService auditService;
    
    public AssetService(AssetRepository assetRepository, AuditService auditService) {
        this.assetRepository = assetRepository;
        this.auditService = auditService;
    }
    
    public Asset addAsset(Asset asset) {
        Asset savedAsset = assetRepository.save(asset);
        auditService.logAction("ASSET_CREATED", savedAsset.getId(), null, 
            "New asset added: " + savedAsset.getName());
        return savedAsset;
    }
    
    public Asset updateAsset(Long id, Asset assetDetails) {
        Asset asset = getAssetById(id);
        asset.setName(assetDetails.getName());
        asset.setType(assetDetails.getType());
        asset.setSerialNumber(assetDetails.getSerialNumber());
        asset.setBrand(assetDetails.getBrand());
        asset.setModel(assetDetails.getModel());
        asset.setPurchaseDate(assetDetails.getPurchaseDate());
        asset.setConditionStatus(assetDetails.getConditionStatus());
        
        Asset updatedAsset = assetRepository.save(asset);
        auditService.logAction("ASSET_UPDATED", updatedAsset.getId(), null,
            "Asset updated: " + updatedAsset.getName());
        return updatedAsset;
    }
    
    public void deleteAsset(Long id) {
        Asset asset = getAssetById(id);
        auditService.logAction("ASSET_DELETED", id, null,
            "Asset deleted: " + asset.getName());
        assetRepository.deleteById(id);
    }
    
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
    
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }
    
    public List<Asset> searchAssets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAssets();
        }
        return assetRepository.searchAssets(keyword);
    }
    
    public List<Asset> getAssetsByStatus(String status) {
        return assetRepository.findByStatus(status);
    }
    
    public List<Asset> getAssetsByType(String type) {
        return assetRepository.findByType(type);
    }
    
    public long getAvailableAssetsCount() {
        return assetRepository.countByStatus("AVAILABLE");
    }
    
    public long getAssignedAssetsCount() {
        return assetRepository.countByStatus("ASSIGNED");
    }
    
    public long getMaintenanceAssetsCount() {
        return assetRepository.countByStatus("MAINTENANCE");
    }
    
    public List<String> getAllAssetTypes() {
        return assetRepository.findAllTypes();
    }
    
    public void updateAssetStatus(Long assetId, String status) {
        Asset asset = getAssetById(assetId);
        if (asset != null) {
            asset.setStatus(status);
            assetRepository.save(asset);
            auditService.logAction("STATUS_UPDATED", assetId, null,
                "Asset status changed to: " + status);
        }
    }
}