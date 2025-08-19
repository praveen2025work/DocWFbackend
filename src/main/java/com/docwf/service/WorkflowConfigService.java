package com.docwf.service;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigParamDto;
import java.util.List;
import java.util.Optional;

public interface WorkflowConfigService {
    
    /**
     * Create a new workflow configuration
     */
    WorkflowConfigDto createWorkflow(WorkflowConfigDto workflowDto);
    
    /**
     * Get workflow by ID
     */
    Optional<WorkflowConfigDto> getWorkflowById(Long workflowId);
    
    /**
     * Get workflow by name
     */
    Optional<WorkflowConfigDto> getWorkflowByName(String name);
    
    /**
     * Get all active workflows
     */
    List<WorkflowConfigDto> getAllActiveWorkflows();
    
    /**
     * Get all workflows
     */
    List<WorkflowConfigDto> getAllWorkflows();
    
    /**
     * Update workflow
     */
    WorkflowConfigDto updateWorkflow(Long workflowId, WorkflowConfigDto workflowDto);
    
    /**
     * Delete workflow
     */
    void deleteWorkflow(Long workflowId);
    
    /**
     * Get workflows by user assignment
     */
    List<WorkflowConfigDto> getWorkflowsByUserId(Long userId);
    
    /**
     * Get workflows by role assignment
     */
    List<WorkflowConfigDto> getWorkflowsByRoleId(Long roleId);
    
    /**
     * Get workflows that need reminders
     */
    List<WorkflowConfigDto> getWorkflowsNeedingReminders();
    
    /**
     * Get workflows that need escalation
     */
    List<WorkflowConfigDto> getWorkflowsNeedingEscalation();
    
    /**
     * Check if workflow name exists
     */
    boolean workflowNameExists(String name);
    
    /**
     * Activate/Deactivate workflow
     */
    WorkflowConfigDto toggleWorkflowStatus(Long workflowId, String isActive);
    
    // Workflow Role Management
    /**
     * Assign role to workflow
     */
    WorkflowConfigRoleDto assignRoleToWorkflow(Long workflowId, WorkflowConfigRoleDto roleDto);
    
    /**
     * Get workflow roles
     */
    List<WorkflowConfigRoleDto> getWorkflowRoles(Long workflowId);
    
    /**
     * Update workflow role
     */
    WorkflowConfigRoleDto updateWorkflowRole(Long workflowId, Long configRoleId, WorkflowConfigRoleDto roleDto);
    
    /**
     * Remove role from workflow
     */
    void removeRoleFromWorkflow(Long workflowId, Long configRoleId);
    
    // Workflow Task Management
    /**
     * Create task in workflow
     */
    WorkflowConfigTaskDto createTask(Long workflowId, WorkflowConfigTaskDto taskDto);
    
    /**
     * Get workflow tasks
     */
    List<WorkflowConfigTaskDto> getWorkflowTasks(Long workflowId);
    
    /**
     * Update workflow task
     */
    WorkflowConfigTaskDto updateTask(Long workflowId, Long taskId, WorkflowConfigTaskDto taskDto);
    
    /**
     * Delete workflow task
     */
    void deleteTask(Long workflowId, Long taskId);
    
    /**
     * Reorder tasks in workflow
     */
    List<WorkflowConfigTaskDto> reorderTasks(Long workflowId, List<Long> taskIds);
    
    // Workflow Parameter Management
    /**
     * Add parameter to workflow
     */
    WorkflowConfigParamDto addParameter(Long workflowId, WorkflowConfigParamDto paramDto);
    
    /**
     * Get workflow parameters
     */
    List<WorkflowConfigParamDto> getWorkflowParameters(Long workflowId);
    
    /**
     * Update workflow parameter
     */
    WorkflowConfigParamDto updateParameter(Long workflowId, Long paramId, WorkflowConfigParamDto paramDto);
    
    /**
     * Delete workflow parameter
     */
    void deleteParameter(Long workflowId, Long paramId);
    
    /**
     * Get parameter value by key
     */
    String getParameterValue(Long workflowId, String paramKey);
}
