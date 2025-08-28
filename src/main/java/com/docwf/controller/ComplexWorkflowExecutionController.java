package com.docwf.controller;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.service.ComplexWorkflowExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complex-workflow")
@Tag(name = "Complex Workflow Execution", description = "APIs for complex workflow execution with file dependencies, consolidation, and decision routing")
public class ComplexWorkflowExecutionController {
    
    @Autowired
    private ComplexWorkflowExecutionService complexWorkflowExecutionService;
    
    // ===== TASK EXECUTION WITH FILE DEPENDENCIES =====
    
    @PostMapping("/tasks/{instanceTaskId}/execute")
    @Operation(summary = "Execute task step", description = "Execute a workflow step with file dependencies")
    public ResponseEntity<WorkflowInstanceTaskDto> executeTaskStep(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Action to perform") @RequestParam String action,
            @Parameter(description = "Comments") @RequestParam(required = false) String comments) {
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.executeTaskStep(instanceTaskId, action, comments);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/tasks/{instanceTaskId}/available-files")
    @Operation(summary = "Get available files", description = "Get available files for a task based on source task dependencies")
    public ResponseEntity<List<Object>> getAvailableFilesForTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        List<Object> files = complexWorkflowExecutionService.getAvailableFilesForTask(instanceTaskId);
        return ResponseEntity.ok(files);
    }
    
    // ===== FILE CONSOLIDATION =====
    
    @PostMapping("/tasks/{instanceTaskId}/consolidate")
    @Operation(summary = "Consolidate files", description = "Consolidate files based on consolidation rules")
    public ResponseEntity<WorkflowInstanceTaskDto> consolidateFiles(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Selected file IDs") @RequestBody List<Long> selectedFileIds,
            @Parameter(description = "Consolidation notes") @RequestParam(required = false) String consolidationNotes) {
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.consolidateFiles(instanceTaskId, selectedFileIds, consolidationNotes);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/tasks/{instanceTaskId}/consolidation-preview")
    @Operation(summary = "Get consolidation preview", description = "Get consolidation preview for a task")
    public ResponseEntity<Object> getConsolidationPreview(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        Object preview = complexWorkflowExecutionService.getConsolidationPreview(instanceTaskId);
        return ResponseEntity.ok(preview);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/auto-consolidate")
    @Operation(summary = "Auto consolidate", description = "Execute auto-consolidation based on rules")
    public ResponseEntity<WorkflowInstanceTaskDto> executeAutoConsolidation(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.executeAutoConsolidation(instanceTaskId);
        return ResponseEntity.ok(result);
    }
    
    // ===== DECISION AND ROUTING =====
    
    @PostMapping("/tasks/{instanceTaskId}/decide")
    @Operation(summary = "Make decision", description = "Make a decision and route to appropriate tasks")
    public ResponseEntity<WorkflowInstanceTaskDto> makeDecision(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Decision") @RequestParam String decision,
            @Parameter(description = "Comments") @RequestParam(required = false) String comments,
            @Parameter(description = "Next action") @RequestParam String nextAction) {
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.makeDecision(instanceTaskId, decision, comments, nextAction);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/instances/{instanceId}/route-decision")
    @Operation(summary = "Route workflow", description = "Route workflow based on decision outcome")
    public ResponseEntity<Void> routeWorkflowBasedOnDecision(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId,
            @Parameter(description = "Decision Task ID") @RequestParam Long decisionTaskId,
            @Parameter(description = "Decision") @RequestParam String decision,
            @Parameter(description = "Next action") @RequestParam String nextAction) {
        complexWorkflowExecutionService.routeWorkflowBasedOnDecision(instanceId, decisionTaskId, decision, nextAction);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/tasks/{instanceTaskId}/decision-routing-options")
    @Operation(summary = "Get decision routing options", description = "Get decision routing options for a task")
    public ResponseEntity<List<TaskInstanceDecisionOutcomeDto>> getDecisionRoutingOptions(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        List<TaskInstanceDecisionOutcomeDto> options = complexWorkflowExecutionService.getDecisionRoutingOptions(instanceTaskId);
        return ResponseEntity.ok(options);
    }
    
    // ===== REVISION AND REVISITATION =====
    
    @PostMapping("/instances/{instanceId}/open-revision-tasks")
    @Operation(summary = "Open revision tasks", description = "Open revision tasks based on decision outcome")
    public ResponseEntity<Void> openRevisionTasks(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId,
            @Parameter(description = "Task IDs to open") @RequestBody List<Long> taskIds,
            @Parameter(description = "Revision strategy") @RequestParam String revisionStrategy) {
        complexWorkflowExecutionService.openRevisionTasks(instanceId, taskIds, revisionStrategy);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/tasks/{instanceTaskId}/can-revise")
    @Operation(summary = "Check revision capability", description = "Check if a task can be revised")
    public ResponseEntity<Boolean> canTaskBeRevised(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        boolean canRevise = complexWorkflowExecutionService.canTaskBeRevised(instanceTaskId);
        return ResponseEntity.ok(canRevise);
    }
    
    @GetMapping("/instances/{instanceId}/revision-history")
    @Operation(summary = "Get revision history", description = "Get revision history for a workflow instance")
    public ResponseEntity<List<Object>> getRevisionHistory(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        List<Object> history = complexWorkflowExecutionService.getRevisionHistory(instanceId);
        return ResponseEntity.ok(history);
    }
    
    // ===== VALIDATION AND UTILITIES =====
    
    @GetMapping("/tasks/{instanceTaskId}/validate-dependencies")
    @Operation(summary = "Validate file dependencies", description = "Validate file dependencies before task execution")
    public ResponseEntity<Boolean> validateFileDependencies(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        boolean isValid = complexWorkflowExecutionService.validateFileDependencies(instanceTaskId);
        return ResponseEntity.ok(isValid);
    }
    
    @GetMapping("/instances/{instanceId}/file-dependency-tree")
    @Operation(summary = "Get file dependency tree", description = "Get file dependency tree for a workflow instance")
    public ResponseEntity<Object> getFileDependencyTree(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        Object dependencyTree = complexWorkflowExecutionService.getFileDependencyTree(instanceId);
        return ResponseEntity.ok(dependencyTree);
    }
}
