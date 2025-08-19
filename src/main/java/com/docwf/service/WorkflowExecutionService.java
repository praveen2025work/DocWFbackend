package com.docwf.service;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.dto.WorkflowProgressDto;
import com.docwf.dto.WorkflowInstanceStatsDto;
import com.docwf.dto.UserWorkloadDto;
import java.util.List;
import java.util.Optional;

public interface WorkflowExecutionService {
    
    /**
     * Start a new workflow instance
     */
    WorkflowInstanceDto startWorkflow(Long workflowId, Long startedByUserId);
    
    /**
     * Get workflow instance by ID
     */
    Optional<WorkflowInstanceDto> getWorkflowInstance(Long instanceId);
    
    /**
     * Get all instances of a workflow
     */
    List<WorkflowInstanceDto> getWorkflowInstances(Long workflowId);
    
    /**
     * Get workflow instances by status
     */
    List<WorkflowInstanceDto> getWorkflowInstancesByStatus(String status);
    
    /**
     * Update workflow instance status
     */
    WorkflowInstanceDto updateInstanceStatus(Long instanceId, String status);
    
    /**
     * Complete workflow instance
     */
    WorkflowInstanceDto completeWorkflowInstance(Long instanceId);
    
    /**
     * Cancel workflow instance
     */
    WorkflowInstanceDto cancelWorkflowInstance(Long instanceId);
    
    /**
     * Escalate workflow instance
     */
    WorkflowInstanceDto escalateWorkflowInstance(Long instanceId, Long escalatedToUserId);
    
    // Task Instance Management
    /**
     * Get instance tasks
     */
    List<WorkflowInstanceTaskDto> getInstanceTasks(Long instanceId);
    
    /**
     * Get instance task by ID
     */
    Optional<WorkflowInstanceTaskDto> getInstanceTask(Long instanceTaskId);
    
    /**
     * Assign task to user
     */
    WorkflowInstanceTaskDto assignTask(Long instanceTaskId, Long userId);
    
    /**
     * Start task
     */
    WorkflowInstanceTaskDto startTask(Long instanceTaskId);
    
    /**
     * Complete task
     */
    WorkflowInstanceTaskDto completeTask(Long instanceTaskId, String decisionOutcome);
    
    /**
     * Fail task
     */
    WorkflowInstanceTaskDto failTask(Long instanceTaskId, String reason);
    
    /**
     * Escalate task
     */
    WorkflowInstanceTaskDto escalateTask(Long instanceTaskId, Long escalatedToUserId);
    
    /**
     * Get next pending task for instance
     */
    Optional<WorkflowInstanceTaskDto> getNextPendingTask(Long instanceId);
    
    /**
     * Get tasks assigned to user
     */
    List<WorkflowInstanceTaskDto> getTasksAssignedToUser(Long userId);
    
    /**
     * Get pending tasks for user
     */
    List<WorkflowInstanceTaskDto> getPendingTasksForUser(Long userId);
    
    // Task Decision Management
    /**
     * Record task decision outcome
     */
    TaskInstanceDecisionOutcomeDto recordDecisionOutcome(Long instanceTaskId, String outcomeName, String createdBy);
    
    /**
     * Get task decision outcomes
     */
    List<TaskInstanceDecisionOutcomeDto> getTaskDecisionOutcomes(Long instanceTaskId);
    
    /**
     * Process decision outcome and move to next task
     */
    WorkflowInstanceTaskDto processDecisionOutcome(Long instanceTaskId, String outcomeName);
    
    // Workflow Execution Logic
    /**
     * Execute workflow step (move to next task)
     */
    WorkflowInstanceTaskDto executeNextTask(Long instanceId);
    
    /**
     * Check workflow completion
     */
    boolean isWorkflowComplete(Long instanceId);
    
    /**
     * Get workflow progress
     */
    WorkflowProgressDto getWorkflowProgress(Long instanceId);
    
    /**
     * Trigger workflow reminders
     */
    void triggerWorkflowReminders();
    
    /**
     * Trigger workflow escalations
     */
    void triggerWorkflowEscalations();
    
    /**
     * Get overdue tasks
     */
    List<WorkflowInstanceTaskDto> getOverdueTasks();
    
    /**
     * Get tasks needing attention
     */
    List<WorkflowInstanceTaskDto> getTasksNeedingAttention();
    
    /**
     * Get workflow instance statistics
     */
    WorkflowInstanceStatsDto getWorkflowInstanceStats(Long workflowId);
    
    /**
     * Get user workload
     */
    UserWorkloadDto getUserWorkload(Long userId);
}
