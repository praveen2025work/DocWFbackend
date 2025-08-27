package com.docwf.service.impl;

import com.docwf.dto.WorkflowUserDto;
import com.docwf.entity.WorkflowUser;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.WorkflowUserRepository;
import com.docwf.service.WorkflowUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkflowUserServiceImpl implements WorkflowUserService {
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    @Override
    public WorkflowUserDto createUser(WorkflowUserDto userDto) {
        // Validate unique constraints
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new WorkflowException("Username already exists: " + userDto.getUsername());
        }
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new WorkflowException("Email already exists: " + userDto.getEmail());
        }
        
        // Create entity
        WorkflowUser user = new WorkflowUser();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setIsActive(userDto.getIsActive() != null ? userDto.getIsActive() : "Y");
        user.setCreatedBy(userDto.getCreatedBy() != null ? userDto.getCreatedBy() : "system");
        
        // Set escalation if provided
        if (userDto.getEscalationTo() != null) {
            Optional<WorkflowUser> escalationUser = userRepository.findById(userDto.getEscalationTo());
            if (escalationUser.isPresent()) {
                user.setEscalationTo(escalationUser.get());
            }
        }
        
        WorkflowUser savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowUserDto> getUserById(Long userId) {
        return userRepository.findById(userId).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowUserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowUserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowUserDto> getAllActiveUsers() {
        return userRepository.findByIsActive("Y")
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowUserDto> getAllUsers(String isActive, Pageable pageable) {
        Page<WorkflowUser> users;
        if (isActive != null && !isActive.isEmpty()) {
            users = userRepository.findByIsActive(isActive, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowUserDto> searchUsers(String username, String firstName, String lastName, String email, 
                                           String isActive, String createdBy, Long escalationTo, 
                                           String createdAfter, String createdBefore, Pageable pageable) {
        // Implement proper search logic with dynamic query building
        if (username == null && firstName == null && lastName == null && email == null && 
            isActive == null && createdBy == null && escalationTo == null && 
            createdAfter == null && createdBefore == null) {
            // No search criteria, return all users with pagination
            return getAllUsers(isActive, pageable);
        }
        
        // Build dynamic search criteria
        List<WorkflowUser> filteredUsers = userRepository.findAll().stream()
                .filter(user -> username == null || (user.getUsername() != null && 
                        user.getUsername().toLowerCase().contains(username.toLowerCase())))
                .filter(user -> firstName == null || (user.getFirstName() != null && 
                        user.getFirstName().toLowerCase().contains(firstName.toLowerCase())))
                .filter(user -> lastName == null || (user.getLastName() != null && 
                        user.getLastName().toLowerCase().contains(lastName.toLowerCase())))
                .filter(user -> email == null || (user.getEmail() != null && 
                        user.getEmail().toLowerCase().contains(email.toLowerCase())))
                .filter(user -> isActive == null || (user.getIsActive() != null && 
                        user.getIsActive().equals(isActive)))
                .filter(user -> createdBy == null || (user.getCreatedBy() != null && 
                        user.getCreatedBy().toLowerCase().contains(createdBy.toLowerCase())))
                .filter(user -> escalationTo == null || (user.getEscalationTo() != null && 
                        user.getEscalationTo().getUserId().equals(escalationTo)))
                .filter(user -> createdAfter == null || (user.getCreatedOn() != null && 
                        user.getCreatedOn().isAfter(LocalDateTime.parse(createdAfter))))
                .filter(user -> createdBefore == null || (user.getCreatedOn() != null && 
                        user.getCreatedOn().isBefore(LocalDateTime.parse(createdBefore))))
                .collect(Collectors.toList());
        
        // Apply pagination manually since we're filtering in memory
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredUsers.size());
        
        if (start > filteredUsers.size()) {
            return Page.empty(pageable);
        }
        
        List<WorkflowUserDto> pageContent = filteredUsers.subList(start, end)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filteredUsers.size());
    }
    
    @Override
    public WorkflowUserDto updateUser(Long userId, WorkflowUserDto userDto) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        // Update fields
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            // Check if email is unique (excluding current user)
            Optional<WorkflowUser> existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
                throw new WorkflowException("Email already exists: " + userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getIsActive() != null) {
            user.setIsActive(userDto.getIsActive());
        }
        
        user.setUpdatedBy(userDto.getUpdatedBy() != null ? userDto.getUpdatedBy() : "system");
        user.setUpdatedOn(LocalDateTime.now());
        
        // Update escalation if provided
        if (userDto.getEscalationTo() != null) {
            Optional<WorkflowUser> escalationUser = userRepository.findById(userDto.getEscalationTo());
            if (escalationUser.isPresent()) {
                user.setEscalationTo(escalationUser.get());
            }
        }
        
        WorkflowUser savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    @Override
    public void deleteUser(Long userId) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        // Check if user is assigned to any workflows
        List<WorkflowUser> usersInWorkflows = userRepository.findUsersByWorkflowId(userId);
        if (!usersInWorkflows.isEmpty()) {
            throw new WorkflowException("Cannot delete user. User is assigned to workflows.");
        }
        
        userRepository.delete(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowUserDto> getUsersByRoleName(String roleName) {
        return userRepository.findUsersByRoleName(roleName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowUserDto> getUsersByWorkflowId(Long workflowId) {
        return userRepository.findUsersByWorkflowId(workflowId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowUserDto> getEscalationHierarchy(Long userId) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        return userRepository.findUsersWhoEscalateTo(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public WorkflowUserDto toggleUserStatus(Long userId, String isActive) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        user.setIsActive(isActive);
        user.setUpdatedBy("system");
        user.setUpdatedOn(LocalDateTime.now());
        
        WorkflowUser savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    @Override
    public WorkflowUserDto setEscalation(Long userId, Long escalationToUserId) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        if (escalationToUserId != null) {
            WorkflowUser escalationUser = userRepository.findById(escalationToUserId)
                    .orElseThrow(() -> new WorkflowException("Escalation user not found with ID: " + escalationToUserId));
            user.setEscalationTo(escalationUser);
        } else {
            user.setEscalationTo(null);
        }
        
        user.setUpdatedBy("system");
        user.setUpdatedOn(LocalDateTime.now());
        
        WorkflowUser savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    /**
     * Convert entity to DTO
     */
    private WorkflowUserDto convertToDto(WorkflowUser user) {
        WorkflowUserDto dto = new WorkflowUserDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedOn(user.getCreatedOn());
        dto.setUpdatedBy(user.getUpdatedBy());
        dto.setUpdatedOn(user.getUpdatedOn());
        
        if (user.getEscalationTo() != null) {
            dto.setEscalationTo(user.getEscalationTo().getUserId());
        }
        
        return dto;
    }
}
