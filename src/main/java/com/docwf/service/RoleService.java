package com.docwf.service;

import com.docwf.dto.WorkflowRoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    
    /**
     * Creates a new workflow role
     * @param roleDto the role data
     * @return the created role
     */
    WorkflowRoleDto createRole(WorkflowRoleDto roleDto);
    
    /**
     * Retrieves a role by its ID
     * @param roleId the role ID
     * @return the role if found
     */
    Optional<WorkflowRoleDto> getRoleById(Long roleId);
    
    /**
     * Retrieves a role by its name
     * @param roleName the role name
     * @return the role if found
     */
    Optional<WorkflowRoleDto> getRoleByName(String roleName);
    
    /**
     * Retrieves all roles
     * @return list of all roles
     */
    List<WorkflowRoleDto> getAllRoles();
    
    /**
     * Retrieves all roles with pagination and optional filtering
     * @return page of roles
     */
    Page<WorkflowRoleDto> getAllRoles(String isActive, Pageable pageable);
    
    /**
     * Search roles using multiple criteria with pagination
     * @return page of roles
     */
    Page<WorkflowRoleDto> searchRoles(String roleName, String isActive, String createdBy, 
                                    Long minRoleId, Long maxRoleId, String createdAfter, 
                                    String createdBefore, Pageable pageable);
    
    /**
     * Retrieves all active roles
     * @return list of active roles
     */
    List<WorkflowRoleDto> getAllActiveRoles();
    
    /**
     * Updates an existing role
     * @param roleId the role ID
     * @param roleDto the updated role data
     * @return the updated role
     */
    WorkflowRoleDto updateRole(Long roleId, WorkflowRoleDto roleDto);
    
    /**
     * Deletes a role
     * @param roleId the role ID
     */
    void deleteRole(Long roleId);
    
    /**
     * Retrieves all roles assigned to a specific workflow
     * @param workflowId the workflow ID
     * @return list of roles
     */
    List<WorkflowRoleDto> getRolesByWorkflowId(Long workflowId);
    
    /**
     * Retrieves all roles assigned to a specific user
     * @param userId the user ID
     * @return list of roles
     */
    List<WorkflowRoleDto> getRolesByUserId(Long userId);
    
    /**
     * Checks if a role name already exists
     * @param roleName the role name to check
     * @return true if exists, false otherwise
     */
    boolean roleNameExists(String roleName);
    
    /**
     * Toggles the active status of a role
     * @param roleId the role ID
     * @param isActive the new active status
     * @return the updated role
     */
    WorkflowRoleDto toggleRoleStatus(Long roleId, String isActive);
    
    /**
     * Assigns a role to a user
     * @param roleId the role ID
     * @param userId the user ID
     */
    void assignRoleToUser(Long roleId, Long userId);
    
    /**
     * Removes a role assignment from a user
     * @param roleId the role ID
     * @param userId the user ID
     */
    void unassignRoleFromUser(Long roleId, Long userId);
}
