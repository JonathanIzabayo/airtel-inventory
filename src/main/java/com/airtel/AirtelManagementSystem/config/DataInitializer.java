package com.airtel.AirtelManagementSystem.config;

import com.airtel.AirtelManagementSystem.model.SystemUser;
import com.airtel.AirtelManagementSystem.model.User;
import com.airtel.AirtelManagementSystem.repository.SystemUserRepository;
import com.airtel.AirtelManagementSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final SystemUserRepository systemUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(SystemUserRepository systemUserRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.systemUserRepository = systemUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create ADMIN user
        if (!systemUserRepository.existsByUsername("admin")) {
            SystemUser admin = new SystemUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@airtel.com");
            admin.setRole("ADMIN");
            admin.setDepartment("IT");
            admin.setEmployeeId("ADM001");
            systemUserRepository.save(admin);
            System.out.println("Admin user created: admin / admin123");
        }
        
        // Create MANAGER user
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
            System.out.println("Manager user created: manager / manager123");
        }
        
        // Create REGULAR USER
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
            System.out.println("Regular user created: user / user123");
        }
        
        // Create Jonathan user (default credentials)
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
            System.out.println("Jonathan user created: jonathan / airtel123");
        }
        
        // Create sample department users
        String[][] users = {
            {"hruser", "hr123", "HR Manager", "hr@airtel.com", "MANAGER", "HR", "HR001"},
            {"finance", "finance123", "Finance Officer", "finance@airtel.com", "USER", "Finance", "FIN001"},
            {"sales1", "sales123", "Sales Representative", "sales@airtel.com", "USER", "Sales", "SAL001"}
        };
        
        for (String[] u : users) {
            if (!systemUserRepository.existsByUsername(u[0])) {
                SystemUser newUser = new SystemUser();
                newUser.setUsername(u[0]);
                newUser.setPassword(passwordEncoder.encode(u[1]));
                newUser.setFullName(u[2]);
                newUser.setEmail(u[3]);
                newUser.setRole(u[4]);
                newUser.setDepartment(u[5]);
                newUser.setEmployeeId(u[6]);
                systemUserRepository.save(newUser);
                System.out.println("User created: " + u[0] + " / " + u[1]);
            }
        }
        
        // Create corresponding person entries in the User table (for assignments)
        if (userRepository.count() == 0) {
            User person1 = new User();
            person1.setFullName("Jonathan Kimathi");
            person1.setDepartment("IT");
            person1.setEmail("jonathan@airtel.com");
            person1.setPhone("+250788123456");
            userRepository.save(person1);
            
            User person2 = new User();
            person2.setFullName("HR Manager");
            person2.setDepartment("HR");
            person2.setEmail("hr@airtel.com");
            person2.setPhone("+250788123457");
            userRepository.save(person2);
            
            User person3 = new User();
            person3.setFullName("Finance Officer");
            person3.setDepartment("Finance");
            person3.setEmail("finance@airtel.com");
            person3.setPhone("+250788123458");
            userRepository.save(person3);
            
            User person4 = new User();
            person4.setFullName("Sales Representative");
            person4.setDepartment("Sales");
            person4.setEmail("sales@airtel.com");
            person4.setPhone("+250788123459");
            userRepository.save(person4);
        }
        
        System.out.println("========================================");
        System.out.println("AIRTEL RWANDA INVENTORY SYSTEM READY");
        System.out.println("========================================");
        System.out.println("LOGIN CREDENTIALS:");
        System.out.println("Admin: admin / admin123");
        System.out.println("Manager: manager / manager123");
        System.out.println("User: user / user123");
        System.out.println("Jonathan: jonathan / airtel123");
        System.out.println("HR User: hruser / hr123");
        System.out.println("Finance User: finance / finance123");
        System.out.println("Sales User: sales1 / sales123");
        System.out.println("========================================");
    }
}