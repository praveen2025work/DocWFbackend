package com.docwf.service;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.dto.WorkflowProgressDto;
import com.docwf.dto.WorkflowInstanceStatsDto;
import com.docwf.dto.UserWorkloadDto;
import com.docwf.dto.CreateWorkflowInstanceDto;
import com.docwf.dto.ProcessOwnerDashboardDto;
import com.docwf.dto.EscalationItemDto;
import com.docwf.dto.ProcessOwnerStatsDto;
import com.docwf.dto.UserDashboardDto;
import com.docwf.dto.UserActivityDto;
import com.docwf.dto.UserNotificationDto;
import com.docwf.dto.UserCalendarDto;
import com.docwf.dto.UserPerformanceDto;
import com.docwf.dto.WorkflowRoleDto;
import com.docwf.dto.UserPermissionDto;
import com.docwf.dto.UserTeamDto;
import com.docwf.dto.UserPreferencesDto;
import com.docwf.dto.ManagerDashboardDto;
import com.docwf.dto.AdminDashboardDto;
import com.docwf.dto.WorkflowUserDto;
import com.docwf.dto.ProcessOwnerWorkloadDto;
import com.docwf.dto.ProcessOwnerPerformanceDto;
import java.util.List;
import java.util.Optional;

public interface WorkflowExecutionService {
    
    /**
     * Start a new workflow instance
     */
    WorkflowInstanceDto startWorkflow(Long workflowId, Long startedByUserId);
    
    /**
     * Start a new workflow instance with calendar validation
     */
    WorkflowInstanceDto startWorkflowWithCalendar(Long workflowId, Long startedByUserId, Long calendarId);
    
    /**
     * Start a new workflow instance with calendar using DTO
     */
    WorkflowInstanceDto startWorkflowWithCalendar(CreateWorkflowInstanceDto createInstanceDto);
    
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
     * Check if workflow can execute on a specific date based on calendar
     */
    boolean canExecuteWorkflowOnDate(Long workflowId, Long calendarId, java.time.LocalDate date);
    
    /**
     * Get next valid execution date for a workflow based on calendar
     */
    java.time.LocalDate getNextValidExecutionDate(Long workflowId, Long calendarId, java.time.LocalDate fromDate);
    
    /**
     * Get workflow instance statistics
     */
    WorkflowInstanceStatsDto getWorkflowInstanceStats(Long workflowId);
    
    /**
     * Get user workload
     */
    UserWorkloadDto getUserWorkload(Long userId);
    
    // Process Owner specific methods
    /**
     * Get workflow instances managed by a process owner
     */
    List<WorkflowInstanceDto> getWorkflowInstancesByProcessOwner(Long processOwnerId, String status, String priority);
    
    /**
     * Get tasks managed by a process owner
     */
    List<WorkflowInstanceTaskDto> getTasksByProcessOwner(Long processOwnerId, String status, String priority);
    
    /**
     * Get workflow instances that need process owner attention
     */
    List<WorkflowInstanceDto> getWorkflowInstancesNeedingProcessOwnerAttention(Long processOwnerId);
    
    /**
     * Get overdue workflows for a process owner
     */
    List<WorkflowInstanceDto> getOverdueWorkflowsForProcessOwner(Long processOwnerId);
    
    // Dashboard specific methods
    /**
     * Get process owner dashboard data
     */
    ProcessOwnerDashboardDto getProcessOwnerDashboard(Long processOwnerId);
    
    /**
     * Get escalation queue for a process owner
     */
    List<EscalationItemDto> getEscalationQueueForProcessOwner(Long processOwnerId);
    
    /**
     * Get process owner statistics
     */
    ProcessOwnerStatsDto getProcessOwnerStatistics(Long processOwnerId);
    
    /**
     * Reassign a task to another user
     */
    WorkflowInstanceTaskDto reassignTask(Long taskId, Long newUserId, String reason);
    
    /**
     * Override task decision with process owner authority
     */
    WorkflowInstanceTaskDto overrideTaskDecision(Long taskId, String decision, String reason);
    
    /**
     * Get process owner team members
     */
    List<WorkflowUserDto> getProcessOwnerTeam(Long processOwnerId);
    
    /**
     * Assign workflow to process owner
     */
    WorkflowInstanceDto assignWorkflowToProcessOwner(Long workflowId, Long processOwnerId);
    
    /**
     * Unassign workflow from process owner
     */
    void unassignWorkflowFromProcessOwner(Long workflowId, Long processOwnerId);
    
    /**
     * Get process owner workload
     */
    ProcessOwnerWorkloadDto getProcessOwnerWorkload(Long processOwnerId);
    
    /**
     * Get process owner performance metrics
     */
    ProcessOwnerPerformanceDto getProcessOwnerPerformance(Long processOwnerId, String period);
    
    // User Dashboard specific methods
    /**
     * Get user dashboard data
     */
    UserDashboardDto getUserDashboard(Long userId);
    
    // Additional dashboard methods
    /**
     * Get user activities
     */
    List<UserActivityDto> getUserActivities(Long userId, Integer limit);
    
    /**
     * Get user notifications
     */
    List<UserNotificationDto> getUserNotifications(Long userId, String status);
    
    /**
     * Mark notification as read
     */
    UserNotificationDto markNotificationAsRead(Long notificationId);
    
    /**
     * Get user calendar
     */
    UserCalendarDto getUserCalendar(Long userId, String startDate, String endDate);
    
    /**
     * Get user performance metrics
     */
    UserPerformanceDto getUserPerformance(Long userId, String period);
    
    /**
     * Get user roles
     */
    List<WorkflowRoleDto> getUserRoles(Long userId);
    
    /**
     * Get user permissions
     */
    List<UserPermissionDto> getUserPermissions(Long userId);
    
    /**
     * Get user team information
     */
    UserTeamDto getUserTeam(Long userId);
    
    /**
     * Get user preferences
     */
    UserPreferencesDto getUserPreferences(Long userId);
    
    /**
     * Update user preferences
     */
    UserPreferencesDto updateUserPreferences(Long userId, UserPreferencesDto preferences);
    
    // Manager and Admin Dashboard methods
    /**
     * Get manager dashboard data
     */
    ManagerDashboardDto getManagerDashboard(Long managerId);
    
    /**
     * Get admin dashboard data
     */
    AdminDashboardDto getAdminDashboard(Long adminId);
}
