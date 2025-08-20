package com.docwf.controller;

import com.docwf.dto.WorkflowRoleDto;
import com.docwf.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "APIs for managing workflow roles with predicate-based search")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    // ===== CORE CRUD OPERATIONS =====
    
    @PostMapping
    @Operation(summary = "Create role", description = "Creates a new workflow role")
    public ResponseEntity<WorkflowRoleDto> createRole(
            @Valid @RequestBody WorkflowRoleDto roleDto) {
        WorkflowRoleDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }
    
    @GetMapping("/{roleId}")
    @Operation(summary = "Get role by ID", description = "Retrieves a role by its unique identifier")
    public ResponseEntity<WorkflowRoleDto> getRoleById(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        Optional<WorkflowRoleDto> role = roleService.getRoleById(roleId);
        return role.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{roleId}")
    @Operation(summary = "Update role", description = "Updates an existing role's information")
    public ResponseEntity<WorkflowRoleDto> updateRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId,
            @Valid @RequestBody WorkflowRoleDto roleDto) {
        WorkflowRoleDto updatedRole = roleService.updateRole(roleId, roleDto);
        return ResponseEntity.ok(updatedRole);
    }
    
    @DeleteMapping("/{roleId}")
    @Operation(summary = "Delete role", description = "Deletes a role from the system")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
    
    // ===== PREDICATE-BASED SEARCH =====
    
    @GetMapping("/search")
    @Operation(summary = "Search roles with predicates", description = "Search roles using multiple criteria with pagination")
    public ResponseEntity<Page<WorkflowRoleDto>> searchRoles(
            @Parameter(description = "Role name (partial match)") @RequestParam(required = false) String roleName,
            @Parameter(description = "Active status (Y/N)") @RequestParam(required = false) String isActive,
            @Parameter(description = "Created by user") @RequestParam(required = false) String createdBy,
            @Parameter(description = "Workflow ID") @RequestParam(required = false) Long workflowId,
            @Parameter(description = "User ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "Created after date (ISO format)") @RequestParam(required = false) String createdAfter,
            @Parameter(description = "Created before date (ISO format)") @RequestParam(required = false) String createdBefore,
            Pageable pageable) {
        
        Page<WorkflowRoleDto> roles = roleService.searchRoles(
                roleName, isActive, createdBy, workflowId, userId, createdAfter, createdBefore, pageable);
        return ResponseEntity.ok(roles);
    }
    
    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieves all roles with optional filtering and pagination")
    public ResponseEntity<Page<WorkflowRoleDto>> getAllRoles(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) String isActive,
            Pageable pageable) {
        Page<WorkflowRoleDto> roles = roleService.getAllRoles(isActive, pageable);
        return ResponseEntity.ok(roles);
    }
    
    // ===== UTILITY OPERATIONS =====
    
    @PatchMapping("/{roleId}/status")
    @Operation(summary = "Toggle role status", description = "Activates or deactivates a role")
    public ResponseEntity<WorkflowRoleDto> toggleRoleStatus(
            @Parameter(description = "Role ID") @PathVariable Long roleId,
            @Parameter(description = "Active status (Y/N)") @RequestParam String isActive) {
        WorkflowRoleDto role = roleService.toggleRoleStatus(roleId, isActive);
        return ResponseEntity.ok(role);
    }
    
    @PostMapping("/{roleId}/assign/user/{userId}")
    @Operation(summary = "Assign role to user", description = "Assigns a role to a specific user")
    public ResponseEntity<Void> assignRoleToUser(
            @Parameter(description = "Role ID") @PathVariable Long roleId,
            @Parameter(description = "User ID") @PathVariable Long userId) {
        roleService.assignRoleToUser(roleId, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{roleId}/unassign/user/{userId}")
    @Operation(summary = "Unassign role from user", description = "Removes a role assignment from a specific user")
    public ResponseEntity<Void> unassignRoleFromUser(
            @Parameter(description = "Role ID") @PathVariable Long roleId,
            @Parameter(description = "User ID") @PathVariable Long userId) {
        roleService.unassignRoleFromUser(roleId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/check/name/{roleName}")
    @Operation(summary = "Check role name availability", description = "Checks if a role name is already taken")
    public ResponseEntity<Boolean> checkRoleNameExists(
            @Parameter(description = "Role name to check") @PathVariable String roleName) {
        boolean exists = roleService.roleNameExists(roleName);
        return ResponseEntity.ok(exists);
    }
}
