package com.airtel.AirtelManagementSystem.config;

import com.airtel.AirtelManagementSystem.model.SystemUser;
import com.airtel.AirtelManagementSystem.repository.SystemUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final SystemUserRepository systemUserRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(SystemUserRepository systemUserRepository, PasswordEncoder passwordEncoder) {
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        // ========================================
        // CREATE MAIN ADMIN WITH REGISTRATION NUMBER
        // ========================================
        if (!systemUserRepository.existsByUsername("24rp11881")) {
            SystemUser mainAdmin = new SystemUser();
            mainAdmin.setUsername("24rp11881");
            mainAdmin.setPassword(passwordEncoder.encode("24rp11334"));
            mainAdmin.setFullName("Main Administrator");
            mainAdmin.setEmail("mainadmin@airtel.com");
            mainAdmin.setRole("ADMIN");
            mainAdmin.setDepartment("IT");
            mainAdmin.setEmployeeId("ADM001");
            systemUserRepository.save(mainAdmin);
            System.out.println("✅ MAIN ADMIN CREATED: 24rp11881 / 24rp11334");
        } else {
            // Update existing user if needed
            SystemUser existing = systemUserRepository.findByUsername("24rp11881").orElse(null);
            if (existing != null && !passwordEncoder.matches("24rp11334", existing.getPassword())) {
                existing.setPassword(passwordEncoder.encode("24rp11334"));
                systemUserRepository.save(existing);
                System.out.println("✅ MAIN ADMIN PASSWORD UPDATED: 24rp11881 / 24rp11334");
            }
        }
        
        // ========================================
        // BACKUP ADMIN (kept for compatibility)
        // ========================================
        if (!systemUserRepository.existsByUsername("admin")) {
            SystemUser admin = new SystemUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Backup Administrator");
            admin.setEmail("admin@airtel.com");
            admin.setRole("ADMIN");
            admin.setDepartment("IT");
            admin.setEmployeeId("ADM002");
            systemUserRepository.save(admin);
            System.out.println("✅ Backup Admin: admin / admin123");
        }
        
        // ========================================
        // MANAGER USER
        // ========================================
        if (!systemUserRepository.existsByUsername("manager")) {
            SystemUser manager = new SystemUser();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFullName("Department Manager");
            manager.setEmail("manager@airtel.com");
            manager.setRole("MANAGER");
            manager.setDepartment("IT");
            manager.setEmployeeId("MGR001");
            systemUserRepository.save(manager);
            System.out.println("✅ Manager: manager / manager123");
        }
        
        // ========================================
        // REGULAR USER
        // ========================================
        if (!systemUserRepository.existsByUsername("user")) {
            SystemUser user = new SystemUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Regular Employee");
            user.setEmail("user@airtel.com");
            user.setRole("USER");
            user.setDepartment("Sales");
            user.setEmployeeId("EMP001");
            systemUserRepository.save(user);
            System.out.println("✅ Regular User: user / user123");
        }
        
        // ========================================
        // JONATHAN USER
        // ========================================
        if (!systemUserRepository.existsByUsername("jonathan")) {
            SystemUser jonathan = new SystemUser();
            jonathan.setUsername("jonathan");
            jonathan.setPassword(passwordEncoder.encode("airtel123"));
            jonathan.setFullName("Jonathan Kimathi");
            jonathan.setEmail("jonathan@airtel.com");
            jonathan.setRole("ADMIN");
            jonathan.setDepartment("IT");
            jonathan.setEmployeeId("JON001");
            systemUserRepository.save(jonathan);
            System.out.println("✅ Jonathan: jonathan / airtel123");
        }
        
        // ========================================
        // DISPLAY SUMMARY
        // ========================================
        System.out.println("\n========================================");
        System.out.println("   AIRTEL RWANDA INVENTORY SYSTEM");
        System.out.println("========================================");
        System.out.println("\n🔐 MAIN LOGIN CREDENTIALS:");
        System.out.println("   Username: 24rp11881");
        System.out.println("   Password: 24rp11334");
        System.out.println("\n📋 BACKUP CREDENTIALS:");
        System.out.println("   Admin: admin / admin123");
        System.out.println("   Manager: manager / manager123");
        System.out.println("   User: user / user123");
        System.out.println("   Jonathan: jonathan / airtel123");
        System.out.println("========================================\n");
    }
}
