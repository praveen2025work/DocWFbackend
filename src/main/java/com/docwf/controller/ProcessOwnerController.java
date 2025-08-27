package com.docwf.controller;

import com.docwf.dto.*;
import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Process Owner operations including escalation management,
 * task reassignment, and process owner dashboard functionality.
 */
@RestController
@RequestMapping("/api/process-owners")
@Tag(name = "Process Owner Management", description = "APIs for process owners to manage workflows, escalations, and task reassignments")
public class ProcessOwnerController {

    @Autowired
    private WorkflowExecutionService executionService;

    @Autowired
    private WorkflowUserService userService;

    /**
     * Get Process Owner Dashboard with overview of all managed workflows
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get Process Owner Dashboard", description = "Retrieves comprehensive dashboard for process owners with workflow overview and statistics")
    public ResponseEntity<ProcessOwnerDashboardDto> getDashboard() {
        Long processOwnerId = getCurrentUserId();
        ProcessOwnerDashboardDto dashboard = executionService.getProcessOwnerDashboard(processOwnerId);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get all workflows managed by the logged-in process owner
     */
    @GetMapping("/workflows")
    @Operation(summary = "Get Process Owner Workflows", description = "Retrieves all workflows managed by the logged-in process owner")
    public ResponseEntity<List<WorkflowInstanceDto>> getProcessOwnerWorkflows(
            @Parameter(description = "Filter by workflow status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by workflow priority") @RequestParam(required = false) String priority) {
        Long processOwnerId = getCurrentUserId();
        List<WorkflowInstanceDto> workflows = executionService.getWorkflowInstancesByProcessOwner(processOwnerId, status, priority);
        return ResponseEntity.ok(workflows);
    }

    /**
     * Get all tasks managed by the logged-in process owner
     */
    @GetMapping("/tasks")
    @Operation(summary = "Get Process Owner Tasks", description = "Retrieves all tasks managed by the logged-in process owner")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getProcessOwnerTasks(
            @Parameter(description = "Filter by task status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by task priority") @RequestParam(required = false) String priority) {
        Long processOwnerId = getCurrentUserId();
        List<WorkflowInstanceTaskDto> tasks = executionService.getTasksByProcessOwner(processOwnerId, status, priority);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get workflows that need process owner attention
     */
    @GetMapping("/workflows/attention-needed")
    @Operation(summary = "Get Workflows Needing Attention", description = "Retrieves workflows that need process owner attention (escalated, overdue, or stuck)")
    public ResponseEntity<List<WorkflowInstanceDto>> getWorkflowsNeedingAttention() {
        Long processOwnerId = getCurrentUserId();
        List<WorkflowInstanceDto> workflows = executionService.getWorkflowInstancesNeedingProcessOwnerAttention(processOwnerId);
        return ResponseEntity.ok(workflows);
    }

    /**
     * Get overdue workflows for the logged-in process owner
     */
    @GetMapping("/workflows/overdue")
    @Operation(summary = "Get Overdue Workflows", description = "Retrieves overdue workflows for the logged-in process owner")
    public ResponseEntity<List<WorkflowInstanceDto>> getOverdueWorkflows() {
        Long processOwnerId = getCurrentUserId();
        List<WorkflowInstanceDto> workflows = executionService.getOverdueWorkflowsForProcessOwner(processOwnerId);
        return ResponseEntity.ok(workflows);
    }

    /**
     * Escalate a workflow instance to a higher authority
     */
    @PostMapping("/escalate/{instanceId}")
    @Operation(summary = "Escalate Workflow", description = "Process owner escalates a workflow instance to a higher authority")
    public ResponseEntity<WorkflowInstanceDto> escalateWorkflow(
            @Parameter(description = "Workflow instance ID") @PathVariable Long instanceId,
            @Parameter(description = "User ID to escalate to") @RequestParam Long escalatedToUserId,
            @Parameter(description = "Escalation reason") @RequestParam(required = false) String reason) {
        
        WorkflowInstanceDto escalatedInstance = executionService.escalateWorkflowInstance(instanceId, escalatedToUserId);
        return ResponseEntity.ok(escalatedInstance);
    }

    /**
     * Reassign a task to a different user
     */
    @PostMapping("/tasks/{taskId}/reassign")
    @Operation(summary = "Reassign Task", description = "Process owner reassigns a task to another user")
    public ResponseEntity<WorkflowInstanceTaskDto> reassignTask(
            @Parameter(description = "Task instance ID") @PathVariable Long taskId,
            @Parameter(description = "User ID to reassign task to") @RequestParam Long newUserId,
            @Parameter(description = "Reassignment reason") @RequestParam(required = false) String reason) {
        // Implement task reassignment service method
        WorkflowInstanceTaskDto reassignedTask = executionService.reassignTask(taskId, newUserId, reason);
        return ResponseEntity.ok(reassignedTask);
    }

    /**
     * Override task decision (Process Owner authority)
     */
    @PostMapping("/tasks/{taskId}/override")
    @Operation(summary = "Override Task Decision", description = "Process owner overrides a task decision with final authority")
    public ResponseEntity<WorkflowInstanceTaskDto> overrideTaskDecision(
            @Parameter(description = "Task instance ID") @PathVariable Long taskId,
            @Parameter(description = "Override decision") @RequestParam String decision,
            @Parameter(description = "Override reason") @RequestParam String reason) {
        // Implement task decision override service method
        WorkflowInstanceTaskDto overriddenTask = executionService.overrideTaskDecision(taskId, decision, reason);
        return ResponseEntity.ok(overriddenTask);
    }

    /**
     * Get escalation queue for the logged-in process owner
     */
    @GetMapping("/escalation-queue")
    @Operation(summary = "Get Escalation Queue", description = "Retrieves all items in the escalation queue for the logged-in process owner")
    public ResponseEntity<List<EscalationItemDto>> getEscalationQueue() {
        Long processOwnerId = getCurrentUserId();
        List<EscalationItemDto> escalationQueue = executionService.getEscalationQueueForProcessOwner(processOwnerId);
        return ResponseEntity.ok(escalationQueue);
    }

    /**
     * Process owner statistics and metrics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get Process Owner Statistics", description = "Retrieves comprehensive statistics for the logged-in process owner")
    public ResponseEntity<ProcessOwnerStatsDto> getProcessOwnerStats(
            @Parameter(description = "Start date for statistics (ISO 8601)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date for statistics (ISO 8601)") @RequestParam(required = false) String endDate) {
        Long processOwnerId = getCurrentUserId();
        ProcessOwnerStatsDto stats = executionService.getProcessOwnerStatistics(processOwnerId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get process owner team members
     */
    @GetMapping("/team")
    @Operation(summary = "Get Process Owner Team", description = "Retrieves all team members under the logged-in process owner")
    public ResponseEntity<List<WorkflowUserDto>> getProcessOwnerTeam() {
        Long processOwnerId = getCurrentUserId();
        // Implement team members service method
        List<WorkflowUserDto> teamMembers = executionService.getProcessOwnerTeam(processOwnerId);
        return ResponseEntity.ok(teamMembers);
    }

    /**
     * Assign workflow to the logged-in process owner
     */
    @PostMapping("/workflows/{workflowId}/assign")
    @Operation(summary = "Assign Workflow to Process Owner", description = "Assigns a workflow to the logged-in process owner")
    public ResponseEntity<WorkflowInstanceDto> assignWorkflowToProcessOwner(
            @Parameter(description = "Workflow instance ID") @PathVariable Long workflowId) {
        Long processOwnerId = getCurrentUserId();
        // Implement workflow assignment to process owner service method
        WorkflowInstanceDto assignedWorkflow = executionService.assignWorkflowToProcessOwner(workflowId, processOwnerId);
        return ResponseEntity.ok(assignedWorkflow);
    }

    /**
     * Remove workflow from the logged-in process owner
     */
    @DeleteMapping("/workflows/{workflowId}/unassign")
    @Operation(summary = "Unassign Workflow from Process Owner", description = "Removes workflow assignment from the logged-in process owner")
    public ResponseEntity<Void> unassignWorkflowFromProcessOwner(
            @Parameter(description = "Workflow instance ID") @PathVariable Long workflowId) {
        Long processOwnerId = getCurrentUserId();
        // Implement workflow unassignment service method
        executionService.unassignWorkflowFromProcessOwner(workflowId, processOwnerId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get process owner workload summary
     */
    @GetMapping("/workload")
    @Operation(summary = "Get Process Owner Workload", description = "Retrieves workload summary for the logged-in process owner")
    public ResponseEntity<ProcessOwnerWorkloadDto> getProcessOwnerWorkload() {
        Long processOwnerId = getCurrentUserId();
        // Implement process owner workload service method
        ProcessOwnerWorkloadDto workload = executionService.getProcessOwnerWorkload(processOwnerId);
        return ResponseEntity.ok(workload);
    }

    /**
     * Get process owner performance metrics
     */
    @GetMapping("/performance")
    @Operation(summary = "Get Process Owner Performance", description = "Retrieves performance metrics for the logged-in process owner")
    public ResponseEntity<ProcessOwnerPerformanceDto> getProcessOwnerPerformance(
            @Parameter(description = "Performance period (WEEKLY, MONTHLY, QUARTERLY)") @RequestParam(defaultValue = "MONTHLY") String period) {
        Long processOwnerId = getCurrentUserId();
        // Implement process owner performance service method
        ProcessOwnerPerformanceDto performance = executionService.getProcessOwnerPerformance(processOwnerId, period);
        return ResponseEntity.ok(performance);
    }

    /**
     * Helper method to get the current logged-in user's ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            // Extract user ID from UserDetails or implement proper user ID extraction
            // For now, return a default value - this should be implemented based on your security setup
            // TODO: Implement proper user ID extraction from UserDetails
            return 1L; // Placeholder - implement proper user ID extraction
        }
        throw new RuntimeException("User not authenticated");
    }
}
