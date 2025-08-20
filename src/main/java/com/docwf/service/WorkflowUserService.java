package com.docwf.service;

import com.docwf.dto.WorkflowUserDto;
import com.docwf.entity.WorkflowUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface WorkflowUserService {
    
    /**
     * Create a new workflow user
     */
    WorkflowUserDto createUser(WorkflowUserDto userDto);
    
    /**
     * Get user by ID
     */
    Optional<WorkflowUserDto> getUserById(Long userId);
    
    /**
     * Get user by username
     */
    Optional<WorkflowUserDto> getUserByUsername(String username);
    
    /**
     * Get user by email
     */
    Optional<WorkflowUserDto> getUserByEmail(String email);
    
    /**
     * Get all active users
     */
    List<WorkflowUserDto> getAllActiveUsers();
    
    /**
     * Get all users
     */
    List<WorkflowUserDto> getAllUsers();
    
    /**
     * Get all users with pagination and optional filtering
     */
    Page<WorkflowUserDto> getAllUsers(String isActive, Pageable pageable);
    
    /**
     * Search users using multiple criteria with pagination
     */
    Page<WorkflowUserDto> searchUsers(String username, String firstName, String lastName, String email, 
                                    String isActive, String createdBy, Long escalationTo, 
                                    String createdAfter, String createdBefore, Pageable pageable);
    
    /**
     * Update user
     */
    WorkflowUserDto updateUser(Long userId, WorkflowUserDto userDto);
    
    /**
     * Delete user
     */
    void deleteUser(Long userId);
    
    /**
     * Get users by role name
     */
    List<WorkflowUserDto> getUsersByRoleName(String roleName);
    
    /**
     * Get users assigned to a specific workflow
     */
    List<WorkflowUserDto> getUsersByWorkflowId(Long workflowId);
    
    /**
     * Get escalation hierarchy for a user
     */
    List<WorkflowUserDto> getEscalationHierarchy(Long userId);
    
    /**
     * Check if username exists
     */
    boolean usernameExists(String username);
    
    /**
     * Check if email exists
     */
    boolean emailExists(String email);
    
    /**
     * Activate/Deactivate user
     */
    WorkflowUserDto toggleUserStatus(Long userId, String isActive);
    
    /**
     * Set escalation for user
     */
    WorkflowUserDto setEscalation(Long userId, Long escalationToUserId);
}
