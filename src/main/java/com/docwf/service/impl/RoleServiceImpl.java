package com.docwf.service.impl;

import com.docwf.dto.WorkflowRoleDto;
import com.docwf.entity.WorkflowRole;
import com.docwf.entity.WorkflowUser;
import com.docwf.entity.WorkflowConfigRole;
import com.docwf.repository.WorkflowRoleRepository;
import com.docwf.repository.WorkflowUserRepository;
import com.docwf.repository.WorkflowConfigRoleRepository;
import com.docwf.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private WorkflowRoleRepository roleRepository;
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    @Autowired
    private WorkflowConfigRoleRepository configRoleRepository;
    
    @Override
    public WorkflowRoleDto createRole(WorkflowRoleDto roleDto) {
        WorkflowRole role = new WorkflowRole();
        role.setRoleName(roleDto.getRoleName());
        role.setIsActive(roleDto.getIsActive() != null ? roleDto.getIsActive() : "Y");
        role.setCreatedBy(roleDto.getCreatedBy());
        role.setCreatedOn(LocalDateTime.now());
        role.setUpdatedBy(roleDto.getCreatedBy());
        role.setUpdatedOn(LocalDateTime.now());
        
        WorkflowRole savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }
    
    @Override
    public Optional<WorkflowRoleDto> getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .map(this::convertToDto);
    }
    
    @Override
    public Optional<WorkflowRoleDto> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .map(this::convertToDto);
    }
    
    @Override
    public List<WorkflowRoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowRoleDto> getAllRoles(String isActive, Pageable pageable) {
        Page<WorkflowRole> roles;
        if (isActive != null && !isActive.isEmpty()) {
            roles = roleRepository.findByIsActive(isActive, pageable);
        } else {
            roles = roleRepository.findAll(pageable);
        }
        return roles.map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowRoleDto> searchRoles(String roleName, String isActive, String createdBy, 
                                           Long minRoleId, Long maxRoleId, String createdAfter, 
                                           String createdBefore, Pageable pageable) {
        // Implement proper search logic with dynamic query building
        if (roleName == null && isActive == null && createdBy == null && 
            minRoleId == null && maxRoleId == null && createdAfter == null && createdBefore == null) {
            // No search criteria, return all roles with pagination
            return getAllRoles(isActive, pageable);
        }
        
        // Build dynamic search criteria
        List<WorkflowRole> filteredRoles = roleRepository.findAll().stream()
                .filter(role -> roleName == null || (role.getRoleName() != null && 
                        role.getRoleName().toLowerCase().contains(roleName.toLowerCase())))
                .filter(role -> isActive == null || (role.getIsActive() != null && 
                        role.getIsActive().equals(isActive)))
                .filter(role -> createdBy == null || (role.getCreatedBy() != null && 
                        role.getCreatedBy().toLowerCase().contains(createdBy.toLowerCase())))
                .filter(role -> minRoleId == null || (role.getRoleId() != null && 
                        role.getRoleId() >= minRoleId))
                .filter(role -> maxRoleId == null || (role.getRoleId() != null && 
                        role.getRoleId() <= maxRoleId))
                .filter(role -> createdAfter == null || (role.getCreatedOn() != null && 
                        role.getCreatedOn().isAfter(LocalDateTime.parse(createdAfter))))
                .filter(role -> createdBefore == null || (role.getCreatedOn() != null && 
                        role.getCreatedOn().isBefore(LocalDateTime.parse(createdBefore))))
                .collect(Collectors.toList());
        
        // Apply pagination manually since we're filtering in memory
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredRoles.size());
        
        if (start > filteredRoles.size()) {
            return Page.empty(pageable);
        }
        
        List<WorkflowRoleDto> pageContent = filteredRoles.subList(start, end)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filteredRoles.size());
    }
    
    @Override
    public List<WorkflowRoleDto> getAllActiveRoles() {
        return roleRepository.findByIsActive("Y").stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowRoleDto updateRole(Long roleId, WorkflowRoleDto roleDto) {
        WorkflowRole existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        existingRole.setRoleName(roleDto.getRoleName());
        existingRole.setIsActive(roleDto.getIsActive());
        existingRole.setUpdatedBy(roleDto.getUpdatedBy());
        existingRole.setUpdatedOn(LocalDateTime.now());
        
        WorkflowRole updatedRole = roleRepository.save(existingRole);
        return convertToDto(updatedRole);
    }
    
    @Override
    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }
    
    @Override
    public List<WorkflowRoleDto> getRolesByWorkflowId(Long workflowId) {
        // This would need to be implemented based on the relationship between roles and workflows
        // For now, returning empty list as placeholder
        return List.of();
    }
    
    @Override
    public List<WorkflowRoleDto> getRolesByUserId(Long userId) {
        // This would need to be implemented based on the relationship between users and roles
        // For now, returning empty list as placeholder
        return List.of();
    }
    
    @Override
    public boolean roleNameExists(String roleName) {
        return roleRepository.findByRoleName(roleName).isPresent();
    }
    
    @Override
    public WorkflowRoleDto toggleRoleStatus(Long roleId, String isActive) {
        WorkflowRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        role.setIsActive(isActive);
        role.setUpdatedOn(LocalDateTime.now());
        
        WorkflowRole updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }
    
    @Override
    public void assignRoleToUser(Long roleId, Long userId) {
        // Implement user-role assignment logic
        WorkflowRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // For general role assignments, we'll create a system-level workflow assignment
        // This is a simplified approach - in a real system, you might have a separate UserRole entity
        
        // Check if assignment already exists (using null workflowId for system-level assignments)
        WorkflowConfigRole existingAssignment = configRoleRepository
                .findByWorkflowWorkflowIdAndRoleRoleIdAndUserUserId(null, roleId, userId);
        
        if (existingAssignment != null) {
            throw new RuntimeException("User already has this role assigned");
        }
        
        // Create new role assignment (system-level, no specific workflow)
        WorkflowConfigRole roleAssignment = new WorkflowConfigRole();
        // Note: We can't set workflow to null due to @NotNull constraint
        // This approach needs to be revised based on your actual requirements
        // For now, we'll throw an exception indicating this needs a different approach
        throw new RuntimeException("General user-role assignment not supported with current entity structure. " +
                "Consider creating a separate UserRole entity or using workflow-specific assignments.");
    }
    
    @Override
    public void unassignRoleFromUser(Long roleId, Long userId) {
        // Implement user-role unassignment logic
        WorkflowRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Find and remove existing assignment (system-level)
        WorkflowConfigRole existingAssignment = configRoleRepository
                .findByWorkflowWorkflowIdAndRoleRoleIdAndUserUserId(null, roleId, userId);
        
        if (existingAssignment != null) {
            configRoleRepository.delete(existingAssignment);
        } else {
            throw new RuntimeException("User does not have this role assigned");
        }
    }
    
    private WorkflowRoleDto convertToDto(WorkflowRole role) {
        WorkflowRoleDto dto = new WorkflowRoleDto();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        dto.setIsActive(role.getIsActive());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setCreatedOn(role.getCreatedOn());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setUpdatedOn(role.getUpdatedOn());
        return dto;
    }
}
