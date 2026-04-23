package com.airtel.AirtelManagementSystem.repository;

import com.airtel.AirtelManagementSystem.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    
    Optional<Asset> findBySerialNumber(String serialNumber);
    
    List<Asset> findByStatus(String status);
    
    List<Asset> findByType(String type);
    
    List<Asset> findByConditionStatus(String conditionStatus);
    
    @Query("SELECT a FROM Asset a WHERE " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.model) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Asset> searchAssets(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT a.type FROM Asset a")
    List<String> findAllTypes();
    
    long countByStatus(String status);
}