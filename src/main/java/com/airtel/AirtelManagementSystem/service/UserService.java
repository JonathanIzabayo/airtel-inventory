package com.airtel.AirtelManagementSystem.service;

import com.airtel.AirtelManagementSystem.model.User;
import com.airtel.AirtelManagementSystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final AuditService auditService;
    
    public UserService(UserRepository userRepository, AuditService auditService) {
        this.userRepository = userRepository;
        this.auditService = auditService;
    }
    
    public User addUser(User user) {
        User savedUser = userRepository.save(user);
        auditService.logAction("USER_CREATED", null, savedUser.getId(),
            "New user added: " + savedUser.getFullName());
        return savedUser;
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        auditService.logAction("USER_UPDATED", null, updatedUser.getId(),
            "User updated: " + updatedUser.getFullName());
        return updatedUser;
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            auditService.logAction("USER_DELETED", null, id, "User deleted: " + user.getFullName());
            userRepository.deleteById(id);
        }
    }
    
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.searchUsers(keyword);
    }
    
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }
    
    public List<String> getAllDepartments() {
        return userRepository.findAllDepartments();
    }
}