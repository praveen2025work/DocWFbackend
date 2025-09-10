package com.docwf.controller;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigTaskFileDto;
import com.docwf.dto.WorkflowConfigTaskFileDependencyDto;
import com.docwf.dto.WorkflowConfigParamDto;
import com.docwf.service.WorkflowConfigService;
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
@RequestMapping("/api/workflows")
@Tag(name = "Workflow Configuration Management", description = "APIs for managing workflow configurations with predicate-based search")
public class WorkflowConfigController {
    
    @Autowired
    private WorkflowConfigService workflowService;
    
    // ===== CORE CRUD OPERATIONS =====
    
    @PostMapping
    @Operation(summary = "Create workflow", description = "Creates a new workflow configuration")
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
    
    // ===== PREDICATE-BASED SEARCH =====
    
    @GetMapping("/search")
    @Operation(summary = "Search workflows with predicates", description = "Search workflows using multiple criteria with pagination")
    public ResponseEntity<Page<WorkflowConfigDto>> searchWorkflows(
            @Parameter(description = "Workflow name (partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Description (partial match)") @RequestParam(required = false) String description,
            @Parameter(description = "Active status (Y/N)") @RequestParam(required = false) String isActive,
            @Parameter(description = "Created by user") @RequestParam(required = false) String createdBy,
            @Parameter(description = "Minimum due time in minutes") @RequestParam(required = false) Integer minDueTime,
            @Parameter(description = "Maximum due time in minutes") @RequestParam(required = false) Integer maxDueTime,
            @Parameter(description = "Created after date (ISO format)") @RequestParam(required = false) String createdAfter,
            @Parameter(description = "Created before date (ISO format)") @RequestParam(required = false) String createdBefore,
            Pageable pageable) {
        
        Page<WorkflowConfigDto> workflows = workflowService.searchWorkflows(
                name, description, isActive, createdBy, minDueTime, maxDueTime, createdAfter, createdBefore, pageable);
        return ResponseEntity.ok(workflows);
    }
    
    @GetMapping
    @Operation(summary = "Get all workflows", description = "Retrieves all workflows with optional filtering and pagination")
    public ResponseEntity<Page<WorkflowConfigDto>> getAllWorkflows(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) String isActive,
            Pageable pageable) {
        Page<WorkflowConfigDto> workflows = workflowService.getAllWorkflows(isActive, pageable);
        return ResponseEntity.ok(workflows);
    }
    
    // ===== WORKFLOW TASK MANAGEMENT =====
    
    @PostMapping("/{workflowId}/tasks")
    @Operation(summary = "Add task to workflow", description = "Adds a new task to a workflow")
    public ResponseEntity<WorkflowConfigTaskDto> addTask(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigTaskDto taskDto) {
        WorkflowConfigTaskDto addedTask = workflowService.addTask(workflowId, taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedTask);
    }
    
    @GetMapping("/{workflowId}/tasks")
    @Operation(summary = "Get workflow tasks", description = "Retrieves all tasks of a workflow")
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
    
    // ===== WORKFLOW ROLE MANAGEMENT =====
    
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
    
    @DeleteMapping("/{workflowId}/roles/{roleId}")
    @Operation(summary = "Remove role from workflow", description = "Removes a role assignment from a workflow")
    public ResponseEntity<Void> removeRoleFromWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        workflowService.removeRoleFromWorkflow(workflowId, roleId);
        return ResponseEntity.noContent().build();
    }
    
    // ===== WORKFLOW PARAMETER MANAGEMENT =====
    
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
    
    // ===== UTILITY OPERATIONS =====
    
    @PatchMapping("/{workflowId}/status")
    @Operation(summary = "Toggle workflow status", description = "Activates or deactivates a workflow")
    public ResponseEntity<WorkflowConfigDto> toggleWorkflowStatus(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Active status (Y/N)") @RequestParam String isActive) {
        WorkflowConfigDto workflow = workflowService.toggleWorkflowStatus(workflowId, isActive);
        return ResponseEntity.ok(workflow);
    }
    
    @PostMapping("/{workflowId}/tasks/reorder")
    @Operation(summary = "Reorder tasks", description = "Reorders tasks in a workflow")
    public ResponseEntity<List<WorkflowConfigTaskDto>> reorderTasks(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @RequestBody List<Long> taskIds) {
        List<WorkflowConfigTaskDto> reorderedTasks = workflowService.reorderTasks(workflowId, taskIds);
        return ResponseEntity.ok(reorderedTasks);
    }
    
    // ===== ENHANCED WORKFLOW CREATION WITH SEQUENCE MAPPING =====
    
    @PostMapping("/create-with-sequences")
    @Operation(summary = "Create workflow with sequence mapping", description = "Creates a workflow with tasks and files using sequence-based mapping for new workflows")
    public ResponseEntity<WorkflowConfigDto> createWorkflowWithSequenceMapping(
            @Valid @RequestBody WorkflowConfigDto workflowDto) {
        WorkflowConfigDto createdWorkflow = workflowService.createWorkflowWithSequenceMapping(workflowDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkflow);
    }
    
    @PutMapping("/{workflowId}/update-with-sequences")
    @Operation(summary = "Update workflow with sequence mapping", description = "Updates a workflow with tasks and files using sequence-based mapping")
    public ResponseEntity<WorkflowConfigDto> updateWorkflowWithSequenceMapping(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowConfigDto workflowDto) {
        WorkflowConfigDto updatedWorkflow = workflowService.updateWorkflowWithSequenceMapping(workflowId, workflowDto);
        return ResponseEntity.ok(updatedWorkflow);
    }
    
    // ===== TASK FILE MANAGEMENT =====
    
    @PostMapping("/{workflowId}/tasks/{taskId}/files")
    @Operation(summary = "Add file to task", description = "Adds a file to a workflow task")
    public ResponseEntity<WorkflowConfigTaskFileDto> addFileToTask(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId,
            @Valid @RequestBody WorkflowConfigTaskFileDto fileDto) {
        WorkflowConfigTaskFileDto addedFile = workflowService.addFileToTask(taskId, fileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFile);
    }
    
    @GetMapping("/{workflowId}/tasks/{taskId}/files")
    @Operation(summary = "Get task files", description = "Retrieves all files for a workflow task")
    public ResponseEntity<List<WorkflowConfigTaskFileDto>> getTaskFiles(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId) {
        List<WorkflowConfigTaskFileDto> files = workflowService.getTaskFiles(taskId);
        return ResponseEntity.ok(files);
    }
    
    @PutMapping("/{workflowId}/tasks/{taskId}/files/{fileId}")
    @Operation(summary = "Update task file", description = "Updates a file in a workflow task")
    public ResponseEntity<WorkflowConfigTaskFileDto> updateTaskFile(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId,
            @Parameter(description = "File ID") @PathVariable Long fileId,
            @Valid @RequestBody WorkflowConfigTaskFileDto fileDto) {
        WorkflowConfigTaskFileDto updatedFile = workflowService.updateTaskFile(taskId, fileId, fileDto);
        return ResponseEntity.ok(updatedFile);
    }
    
    @DeleteMapping("/{workflowId}/tasks/{taskId}/files/{fileId}")
    @Operation(summary = "Delete task file", description = "Deletes a file from a workflow task")
    public ResponseEntity<Void> deleteTaskFile(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Task ID") @PathVariable Long taskId,
            @Parameter(description = "File ID") @PathVariable Long fileId) {
        workflowService.deleteTaskFile(taskId, fileId);
        return ResponseEntity.noContent().build();
    }
    
    // ===== FILE DEPENDENCY MANAGEMENT =====
    
    @PostMapping("/files/{fileId}/dependencies")
    @Operation(summary = "Add file dependency", description = "Adds a dependency relationship between files")
    public ResponseEntity<WorkflowConfigTaskFileDependencyDto> addFileDependency(
            @Parameter(description = "File ID") @PathVariable Long fileId,
            @Valid @RequestBody WorkflowConfigTaskFileDependencyDto dependencyDto) {
        WorkflowConfigTaskFileDependencyDto addedDependency = workflowService.addFileDependency(dependencyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDependency);
    }
    
    @GetMapping("/files/{fileId}/dependencies")
    @Operation(summary = "Get file dependencies", description = "Retrieves all dependencies for a file")
    public ResponseEntity<List<WorkflowConfigTaskFileDependencyDto>> getFileDependencies(
            @Parameter(description = "File ID") @PathVariable Long fileId) {
        List<WorkflowConfigTaskFileDependencyDto> dependencies = workflowService.getFileDependencies(fileId);
        return ResponseEntity.ok(dependencies);
    }
    
    @PutMapping("/dependencies/{dependencyId}")
    @Operation(summary = "Update file dependency", description = "Updates a file dependency relationship")
    public ResponseEntity<WorkflowConfigTaskFileDependencyDto> updateFileDependency(
            @Parameter(description = "Dependency ID") @PathVariable Long dependencyId,
            @Valid @RequestBody WorkflowConfigTaskFileDependencyDto dependencyDto) {
        WorkflowConfigTaskFileDependencyDto updatedDependency = workflowService.updateFileDependency(dependencyId, dependencyDto);
        return ResponseEntity.ok(updatedDependency);
    }
    
    @DeleteMapping("/dependencies/{dependencyId}")
    @Operation(summary = "Delete file dependency", description = "Deletes a file dependency relationship")
    public ResponseEntity<Void> deleteFileDependency(
            @Parameter(description = "Dependency ID") @PathVariable Long dependencyId) {
        workflowService.deleteFileDependency(dependencyId);
        return ResponseEntity.noContent().build();
    }
}
