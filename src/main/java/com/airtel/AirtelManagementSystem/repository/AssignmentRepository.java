package com.airtel.AirtelManagementSystem.repository;

import com.airtel.AirtelManagementSystem.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByStatus(String status);
    
    List<Assignment> findByAssetId(Long assetId);
    
    List<Assignment> findByUserId(Long userId);
    
    @Query("SELECT a FROM Assignment a WHERE a.assignedDate BETWEEN :startDate AND :endDate")
    List<Assignment> findByAssignedDateBetween(@Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Assignment a WHERE a.status = 'ACTIVE'")
    List<Assignment> findActiveAssignments();
    
    boolean existsByAssetIdAndStatus(Long assetId, String status);
}