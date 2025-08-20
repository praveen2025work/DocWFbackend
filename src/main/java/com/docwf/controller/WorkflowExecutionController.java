package com.docwf.controller;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.service.WorkflowExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/execution")
@Tag(name = "Workflow Execution Management", description = "APIs for managing workflow execution and task management")
public class WorkflowExecutionController {
    
    @Autowired
    private WorkflowExecutionService executionService;
    
    // Workflow Instance Management
    @PostMapping("/workflows/{workflowId}/start")
    @Operation(summary = "Start workflow instance", description = "Starts a new instance of a workflow")
    public ResponseEntity<WorkflowInstanceDto> startWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "User ID who started the workflow") @RequestParam Long startedByUserId) {
        WorkflowInstanceDto instance = executionService.startWorkflow(workflowId, startedByUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(instance);
    }
    
    @PostMapping("/workflows/{workflowId}/start-with-calendar")
    @Operation(summary = "Start workflow instance with calendar validation", description = "Starts a new workflow instance with calendar date validation")
    public ResponseEntity<WorkflowInstanceDto> startWorkflowWithCalendar(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "User ID who started the workflow") @RequestParam Long startedByUserId,
            @Parameter(description = "Calendar ID for date validation") @RequestParam(required = false) Long calendarId) {
        WorkflowInstanceDto instance = executionService.startWorkflowWithCalendar(workflowId, startedByUserId, calendarId);
        return ResponseEntity.status(HttpStatus.CREATED).body(instance);
    }
    
    @GetMapping("/instances/{instanceId}")
    @Operation(summary = "Get workflow instance", description = "Retrieves a workflow instance by ID")
    public ResponseEntity<WorkflowInstanceDto> getWorkflowInstance(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        Optional<WorkflowInstanceDto> instance = executionService.getWorkflowInstance(instanceId);
        return instance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/workflows/{workflowId}/instances")
    @Operation(summary = "Get workflow instances", description = "Retrieves all instances of a specific workflow")
    public ResponseEntity<List<WorkflowInstanceDto>> getWorkflowInstances(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        List<WorkflowInstanceDto> instances = executionService.getWorkflowInstances(workflowId);
        return ResponseEntity.ok(instances);
    }
    
    @GetMapping("/instances/status/{status}")
    @Operation(summary = "Get instances by status", description = "Retrieves workflow instances by status")
    public ResponseEntity<List<WorkflowInstanceDto>> getInstancesByStatus(
            @Parameter(description = "Instance status") @PathVariable String status) {
        List<WorkflowInstanceDto> instances = executionService.getWorkflowInstancesByStatus(status);
        return ResponseEntity.ok(instances);
    }
    
    @PatchMapping("/instances/{instanceId}/status")
    @Operation(summary = "Update instance status", description = "Updates the status of a workflow instance")
    public ResponseEntity<WorkflowInstanceDto> updateInstanceStatus(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId,
            @Parameter(description = "New status") @RequestParam String status) {
        WorkflowInstanceDto instance = executionService.updateInstanceStatus(instanceId, status);
        return ResponseEntity.ok(instance);
    }
    
    @PostMapping("/instances/{instanceId}/complete")
    @Operation(summary = "Complete workflow instance", description = "Marks a workflow instance as completed")
    public ResponseEntity<WorkflowInstanceDto> completeWorkflowInstance(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        WorkflowInstanceDto instance = executionService.completeWorkflowInstance(instanceId);
        return ResponseEntity.ok(instance);
    }
    
    @PostMapping("/instances/{instanceId}/cancel")
    @Operation(summary = "Cancel workflow instance", description = "Cancels a workflow instance")
    public ResponseEntity<WorkflowInstanceDto> cancelWorkflowInstance(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        WorkflowInstanceDto instance = executionService.cancelWorkflowInstance(instanceId);
        return ResponseEntity.ok(instance);
    }
    
    @PostMapping("/instances/{instanceId}/escalate")
    @Operation(summary = "Escalate workflow instance", description = "Escalates a workflow instance to another user")
    public ResponseEntity<WorkflowInstanceDto> escalateWorkflowInstance(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId,
            @Parameter(description = "User ID to escalate to") @RequestParam Long escalatedToUserId) {
        WorkflowInstanceDto instance = executionService.escalateWorkflowInstance(instanceId, escalatedToUserId);
        return ResponseEntity.ok(instance);
    }
    
    // Task Instance Management
    @GetMapping("/instances/{instanceId}/tasks")
    @Operation(summary = "Get instance tasks", description = "Retrieves all tasks for a workflow instance")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getInstanceTasks(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        List<WorkflowInstanceTaskDto> tasks = executionService.getInstanceTasks(instanceId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/tasks/{instanceTaskId}")
    @Operation(summary = "Get instance task", description = "Retrieves a specific task instance by ID")
    public ResponseEntity<WorkflowInstanceTaskDto> getInstanceTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        Optional<WorkflowInstanceTaskDto> task = executionService.getInstanceTask(instanceTaskId);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/tasks/{instanceTaskId}/assign")
    @Operation(summary = "Assign task to user", description = "Assigns a task to a specific user")
    public ResponseEntity<WorkflowInstanceTaskDto> assignTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "User ID to assign task to") @RequestParam Long userId) {
        WorkflowInstanceTaskDto task = executionService.assignTask(instanceTaskId, userId);
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/start")
    @Operation(summary = "Start task", description = "Marks a task as started")
    public ResponseEntity<WorkflowInstanceTaskDto> startTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        WorkflowInstanceTaskDto task = executionService.startTask(instanceTaskId);
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/complete")
    @Operation(summary = "Complete task", description = "Marks a task as completed with optional decision outcome")
    public ResponseEntity<WorkflowInstanceTaskDto> completeTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Decision outcome") @RequestParam(required = false) String decisionOutcome) {
        WorkflowInstanceTaskDto task = executionService.completeTask(instanceTaskId, decisionOutcome);
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/fail")
    @Operation(summary = "Fail task", description = "Marks a task as failed with reason")
    public ResponseEntity<WorkflowInstanceTaskDto> failTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Failure reason") @RequestParam String reason) {
        WorkflowInstanceTaskDto task = executionService.failTask(instanceTaskId, reason);
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/escalate")
    @Operation(summary = "Escalate task", description = "Escalates a task to another user")
    public ResponseEntity<WorkflowInstanceTaskDto> escalateTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "User ID to escalate to") @RequestParam Long escalatedToUserId) {
        WorkflowInstanceTaskDto task = executionService.escalateTask(instanceTaskId, escalatedToUserId);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/instances/{instanceId}/next-task")
    @Operation(summary = "Get next pending task", description = "Retrieves the next pending task for an instance")
    public ResponseEntity<WorkflowInstanceTaskDto> getNextPendingTask(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        Optional<WorkflowInstanceTaskDto> task = executionService.getNextPendingTask(instanceId);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/users/{userId}/tasks")
    @Operation(summary = "Get tasks assigned to user", description = "Retrieves all tasks assigned to a specific user")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getTasksAssignedToUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowInstanceTaskDto> tasks = executionService.getTasksAssignedToUser(userId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/users/{userId}/pending-tasks")
    @Operation(summary = "Get pending tasks for user", description = "Retrieves all pending tasks for a specific user")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getPendingTasksForUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowInstanceTaskDto> tasks = executionService.getPendingTasksForUser(userId);
        return ResponseEntity.ok(tasks);
    }
    
    // Task Decision Management
    @PostMapping("/tasks/{instanceTaskId}/decisions")
    @Operation(summary = "Record decision outcome", description = "Records a decision outcome for a task")
    public ResponseEntity<TaskInstanceDecisionOutcomeDto> recordDecisionOutcome(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Outcome name") @RequestParam String outcomeName,
            @Parameter(description = "User who made the decision") @RequestParam String createdBy) {
        TaskInstanceDecisionOutcomeDto outcome = executionService.recordDecisionOutcome(instanceTaskId, outcomeName, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(outcome);
    }
    
    @GetMapping("/tasks/{instanceTaskId}/decisions")
    @Operation(summary = "Get task decision outcomes", description = "Retrieves all decision outcomes for a task")
    public ResponseEntity<List<TaskInstanceDecisionOutcomeDto>> getTaskDecisionOutcomes(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId) {
        List<TaskInstanceDecisionOutcomeDto> outcomes = executionService.getTaskDecisionOutcomes(instanceTaskId);
        return ResponseEntity.ok(outcomes);
    }
    
    @PostMapping("/tasks/{instanceTaskId}/process-decision")
    @Operation(summary = "Process decision outcome", description = "Processes a decision outcome and moves to next task")
    public ResponseEntity<WorkflowInstanceTaskDto> processDecisionOutcome(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Outcome name to process") @RequestParam String outcomeName) {
        WorkflowInstanceTaskDto task = executionService.processDecisionOutcome(instanceTaskId, outcomeName);
        return ResponseEntity.ok(task);
    }
    
    // Workflow Execution Logic
    @PostMapping("/instances/{instanceId}/execute-next")
    @Operation(summary = "Execute next task", description = "Executes the next task in a workflow instance")
    public ResponseEntity<WorkflowInstanceTaskDto> executeNextTask(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        WorkflowInstanceTaskDto task = executionService.executeNextTask(instanceId);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/instances/{instanceId}/complete")
    @Operation(summary = "Check workflow completion", description = "Checks if a workflow instance is complete")
    public ResponseEntity<Boolean> isWorkflowComplete(
            @Parameter(description = "Instance ID") @PathVariable Long instanceId) {
        boolean isComplete = executionService.isWorkflowComplete(instanceId);
        return ResponseEntity.ok(isComplete);
    }
    
    // Utility endpoints
    @GetMapping("/overdue-tasks")
    @Operation(summary = "Get overdue tasks", description = "Retrieves all tasks that are overdue")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getOverdueTasks() {
        List<WorkflowInstanceTaskDto> tasks = executionService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/tasks-needing-attention")
    @Operation(summary = "Get tasks needing attention", description = "Retrieves tasks that need attention")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getTasksNeedingAttention() {
        List<WorkflowInstanceTaskDto> tasks = executionService.getTasksNeedingAttention();
        return ResponseEntity.ok(tasks);
    }
    
    @PostMapping("/reminders/trigger")
    @Operation(summary = "Trigger workflow reminders", description = "Triggers reminder notifications for workflows")
    public ResponseEntity<Void> triggerWorkflowReminders() {
        executionService.triggerWorkflowReminders();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/escalations/trigger")
    @Operation(summary = "Trigger workflow escalations", description = "Triggers escalation notifications for workflows")
    public ResponseEntity<Void> triggerWorkflowEscalations() {
        executionService.triggerWorkflowEscalations();
        return ResponseEntity.ok().build();
    }
    
    // Calendar Integration Endpoints
    @GetMapping("/workflows/{workflowId}/calendar/{calendarId}/can-execute")
    @Operation(summary = "Check workflow execution on date", description = "Checks if a workflow can execute on a specific date based on calendar")
    public ResponseEntity<Boolean> canExecuteWorkflowOnDate(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to check") @RequestParam java.time.LocalDate date) {
        boolean canExecute = executionService.canExecuteWorkflowOnDate(workflowId, calendarId, date);
        return ResponseEntity.ok(canExecute);
    }
    
    @GetMapping("/workflows/{workflowId}/calendar/{calendarId}/next-valid-date")
    @Operation(summary = "Get next valid execution date", description = "Finds the next valid date for workflow execution based on calendar")
    public ResponseEntity<java.time.LocalDate> getNextValidExecutionDate(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId,
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to start from") @RequestParam java.time.LocalDate fromDate) {
        java.time.LocalDate nextValidDate = executionService.getNextValidExecutionDate(workflowId, calendarId, fromDate);
        return ResponseEntity.ok(nextValidDate);
    }
}
