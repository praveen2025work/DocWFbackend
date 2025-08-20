package com.docwf.service.impl;

import com.docwf.dto.WorkflowRoleDto;
import com.docwf.entity.WorkflowRole;
import com.docwf.repository.WorkflowRoleRepository;
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
        // For now, return all roles with pagination
        // TODO: Implement proper search logic
        return getAllRoles(isActive, pageable);
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
        // This would need to be implemented based on the user-role relationship
        // For now, just checking if role exists
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        // TODO: Implement user-role assignment logic
    }
    
    @Override
    public void unassignRoleFromUser(Long roleId, Long userId) {
        // This would need to be implemented based on the user-role relationship
        // For now, just checking if role exists
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        // TODO: Implement user-role unassignment logic
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
