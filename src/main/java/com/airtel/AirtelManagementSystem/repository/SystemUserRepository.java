package com.airtel.AirtelManagementSystem.repository;

import com.airtel.AirtelManagementSystem.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByUsername(String username);
    boolean existsByUsername(String username);
    List<SystemUser> findByRole(String role);
    List<SystemUser> findByDepartment(String department);
}