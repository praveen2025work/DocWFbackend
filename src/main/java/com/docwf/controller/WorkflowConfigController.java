package com.docwf.controller;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigParamDto;
import com.docwf.service.WorkflowConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workflows")
@Tag(name = "Workflow Configuration Management", description = "APIs for managing workflow configurations")
public class WorkflowConfigController {
    
    @Autowired
    private WorkflowConfigService workflowService;
    
    @PostMapping
    @Operation(summary = "Create a new workflow", description = "Creates a new workflow configuration")
    public ResponseEntity<WorkflowConfigDto> createWorkflow(
            @Valid @RequestBody WorkflowConfigDto workflowDto) {
        WorkflowConfigDto createdWorkflow = workflowService.createWorkflow(workflowDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkflow);
    }
    
    @GetMapping("/{workflowId}")
    @Operation(summary = "Get workflow by ID", description = "Retrieves a workflow configuration by ID")
    public ResponseEntity<WorkflowConfigDto> getWorkflowById(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        Optional<WorkflowConfigDto> workflow = workflowService.getWorkflowById(workflowId);
        return workflow.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get workflow by name", description = "Retrieves a workflow configuration by name")
    public ResponseEntity<WorkflowConfigDto> getWorkflowByName(
            @Parameter(description = "Workflow name") @PathVariable String name) {
        Optional<WorkflowConfigDto> workflow = workflowService.getWorkflowByName(name);
        return workflow.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all workflows", description = "Retrieves all workflow configurations with optional filtering")
    public ResponseEntity<List<WorkflowConfigDto>> getAllWorkflows(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) String isActive) {
        List<WorkflowConfigDto> workflows;
        if (isActive != null) {
            workflows = workflowService.getAllActiveWorkflows();
        } else {
            workflows = workflowService.getAllWorkflows();
        }
        return ResponseEntity.ok(workflows);
    }
    
    @PutMapping("/{workflowId}")
    @Operation(summary = "Update workflow", description = "Updates an existing workflow configuration")
    public ResponseEntity<WorkflowConfigDto> updateWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigDto workflowDto) {
        WorkflowConfigDto updatedWorkflow = workflowService.updateWorkflow(workflowId, workflowDto);
        return ResponseEntity.ok(updatedWorkflow);
    }
    
    @DeleteMapping("/{workflowId}")
    @Operation(summary = "Delete workflow", description = "Deletes a workflow configuration")
    public ResponseEntity<Void> deleteWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        workflowService.deleteWorkflow(workflowId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{workflowId}/status")
    @Operation(summary = "Toggle workflow status", description = "Activates or deactivates a workflow")
    public ResponseEntity<WorkflowConfigDto> toggleWorkflowStatus(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Active status (Y/N)") @RequestParam String isActive) {
        WorkflowConfigDto workflow = workflowService.toggleWorkflowStatus(workflowId, isActive);
        return ResponseEntity.ok(workflow);
    }
    
    // Workflow Role Management
    @PostMapping("/{workflowId}/roles")
    @Operation(summary = "Assign role to workflow", description = "Assigns a role to a workflow")
    public ResponseEntity<WorkflowConfigRoleDto> assignRoleToWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigRoleDto roleDto) {
        WorkflowConfigRoleDto assignedRole = workflowService.assignRoleToWorkflow(workflowId, roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignedRole);
    }
    
    @GetMapping("/{workflowId}/roles")
    @Operation(summary = "Get workflow roles", description = "Retrieves all roles assigned to a workflow")
    public ResponseEntity<List<WorkflowConfigRoleDto>> getWorkflowRoles(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        List<WorkflowConfigRoleDto> roles = workflowService.getWorkflowRoles(workflowId);
        return ResponseEntity.ok(roles);
    }
    
    @PutMapping("/{workflowId}/roles/{configRoleId}")
    @Operation(summary = "Update workflow role", description = "Updates a role assignment in a workflow")
    public ResponseEntity<WorkflowConfigRoleDto> updateWorkflowRole(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Config Role ID") @PathVariable Long configRoleId,
            @Valid @RequestBody WorkflowConfigRoleDto roleDto) {
        WorkflowConfigRoleDto updatedRole = workflowService.updateWorkflowRole(workflowId, configRoleId, roleDto);
        return ResponseEntity.ok(updatedRole);
    }
    
    @DeleteMapping("/{workflowId}/roles/{configRoleId}")
    @Operation(summary = "Remove role from workflow", description = "Removes a role assignment from a workflow")
    public ResponseEntity<Void> removeRoleFromWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Config Role ID") @PathVariable Long configRoleId) {
        workflowService.removeRoleFromWorkflow(workflowId, configRoleId);
        return ResponseEntity.noContent().build();
    }
    
    // Workflow Task Management
    @PostMapping("/{workflowId}/tasks")
    @Operation(summary = "Create task in workflow", description = "Creates a new task in a workflow")
    public ResponseEntity<WorkflowConfigTaskDto> createTask(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigTaskDto taskDto) {
        WorkflowConfigTaskDto createdTask = workflowService.createTask(workflowId, taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    @GetMapping("/{workflowId}/tasks")
    @Operation(summary = "Get workflow tasks", description = "Retrieves all tasks in a workflow")
    public ResponseEntity<List<WorkflowConfigTaskDto>> getWorkflowTasks(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        List<WorkflowConfigTaskDto> tasks = workflowService.getWorkflowTasks(workflowId);
        return ResponseEntity.ok(tasks);
    }
    
    @PutMapping("/{workflowId}/tasks/{taskId}")
    @Operation(summary = "Update workflow task", description = "Updates a task in a workflow")
    public ResponseEntity<WorkflowConfigTaskDto> updateTask(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId,
            @Valid @RequestBody WorkflowConfigTaskDto taskDto) {
        WorkflowConfigTaskDto updatedTask = workflowService.updateTask(workflowId, taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }
    
    @DeleteMapping("/{workflowId}/tasks/{taskId}")
    @Operation(summary = "Delete workflow task", description = "Deletes a task from a workflow")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId) {
        workflowService.deleteTask(workflowId, taskId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{workflowId}/tasks/reorder")
    @Operation(summary = "Reorder tasks", description = "Reorders tasks in a workflow")
    public ResponseEntity<List<WorkflowConfigTaskDto>> reorderTasks(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @RequestBody List<Long> taskIds) {
        List<WorkflowConfigTaskDto> reorderedTasks = workflowService.reorderTasks(workflowId, taskIds);
        return ResponseEntity.ok(reorderedTasks);
    }
    
    // Workflow Parameter Management
    @PostMapping("/{workflowId}/parameters")
    @Operation(summary = "Add parameter to workflow", description = "Adds a parameter to a workflow")
    public ResponseEntity<WorkflowConfigParamDto> addParameter(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigParamDto paramDto) {
        WorkflowConfigParamDto addedParam = workflowService.addParameter(workflowId, paramDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedParam);
    }
    
    @GetMapping("/{workflowId}/parameters")
    @Operation(summary = "Get workflow parameters", description = "Retrieves all parameters of a workflow")
    public ResponseEntity<List<WorkflowConfigParamDto>> getWorkflowParameters(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        List<WorkflowConfigParamDto> parameters = workflowService.getWorkflowParameters(workflowId);
        return ResponseEntity.ok(parameters);
    }
    
    @PutMapping("/{workflowId}/parameters/{paramId}")
    @Operation(summary = "Update workflow parameter", description = "Updates a parameter in a workflow")
    public ResponseEntity<WorkflowConfigParamDto> updateParameter(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Parameter ID") @PathVariable Long paramId,
            @Valid @RequestBody WorkflowConfigParamDto paramDto) {
        WorkflowConfigParamDto updatedParam = workflowService.updateParameter(workflowId, paramId, paramDto);
        return ResponseEntity.ok(updatedParam);
    }
    
    @DeleteMapping("/{workflowId}/parameters/{paramId}")
    @Operation(summary = "Delete workflow parameter", description = "Deletes a parameter from a workflow")
    public ResponseEntity<Void> deleteParameter(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Parameter ID") @PathVariable Long paramId) {
        workflowService.deleteParameter(workflowId, paramId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{workflowId}/parameters/{paramKey}/value")
    @Operation(summary = "Get parameter value", description = "Retrieves the value of a specific parameter")
    public ResponseEntity<String> getParameterValue(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Parameter key") @PathVariable String paramKey) {
        String value = workflowService.getParameterValue(workflowId, paramKey);
        return ResponseEntity.ok(value);
    }
    
    // Utility endpoints
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get workflows by user", description = "Retrieves all workflows assigned to a specific user")
    public ResponseEntity<List<WorkflowConfigDto>> getWorkflowsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowConfigDto> workflows = workflowService.getWorkflowsByUserId(userId);
        return ResponseEntity.ok(workflows);
    }
    
    @GetMapping("/role/{roleId}")
    @Operation(summary = "Get workflows by role", description = "Retrieves all workflows assigned to a specific role")
    public ResponseEntity<List<WorkflowConfigDto>> getWorkflowsByRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        List<WorkflowConfigDto> workflows = workflowService.getWorkflowsByRoleId(roleId);
        return ResponseEntity.ok(workflows);
    }
    
    @GetMapping("/reminders")
    @Operation(summary = "Get workflows needing reminders", description = "Retrieves workflows that need reminder notifications")
    public ResponseEntity<List<WorkflowConfigDto>> getWorkflowsNeedingReminders() {
        List<WorkflowConfigDto> workflows = workflowService.getWorkflowsNeedingReminders();
        return ResponseEntity.ok(workflows);
    }
    
    @GetMapping("/escalations")
    @Operation(summary = "Get workflows needing escalations", description = "Retrieves workflows that need escalation notifications")
    public ResponseEntity<List<WorkflowConfigDto>> getWorkflowsNeedingEscalations() {
        List<WorkflowConfigDto> workflows = workflowService.getWorkflowsNeedingEscalation();
        return ResponseEntity.ok(workflows);
    }
}
