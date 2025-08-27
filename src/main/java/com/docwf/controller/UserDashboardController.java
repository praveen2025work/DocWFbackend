package com.docwf.controller;

import com.docwf.dto.*;
import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowUserService;
import com.docwf.entity.WorkflowInstance;
import com.docwf.repository.WorkflowInstanceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for User Dashboard operations providing role-based views
 * and access control for different user types.
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "User Dashboard", description = "APIs for role-based user dashboards and views")
public class UserDashboardController {

    @Autowired
    private WorkflowExecutionService executionService;

    @Autowired
    private WorkflowUserService userService;

    @Autowired
    private WorkflowInstanceRepository instanceRepository;

    /**
     * Get user dashboard based on user's role
     */
    @GetMapping("/user")
    @Operation(summary = "Get User Dashboard", description = "Retrieves role-based dashboard for the authenticated user")
    public ResponseEntity<UserDashboardDto> getUserDashboard(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        UserDashboardDto dashboard = executionService.getUserDashboard(userId);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get process owner specific dashboard
     */
    @GetMapping("/process-owner")
    @Operation(summary = "Get Process Owner Dashboard", description = "Retrieves process owner specific dashboard with escalation and management features")
    public ResponseEntity<ProcessOwnerDashboardDto> getProcessOwnerDashboard(
            @Parameter(description = "Process owner user ID") @RequestParam Long processOwnerId) {
        ProcessOwnerDashboardDto dashboard = executionService.getProcessOwnerDashboard(processOwnerId);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get manager dashboard for team oversight
     */
    @GetMapping("/manager")
    @Operation(summary = "Get Manager Dashboard", description = "Retrieves manager dashboard with team overview and workflow monitoring")
    public ResponseEntity<ManagerDashboardDto> getManagerDashboard(
            @Parameter(description = "Manager user ID") @RequestParam Long managerId) {
        
        ManagerDashboardDto dashboard = executionService.getManagerDashboard(managerId);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get admin dashboard for system administration
     */
    @GetMapping("/admin")
    @Operation(summary = "Get Admin Dashboard", description = "Retrieves admin dashboard with system overview and administrative functions")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard(
            @Parameter(description = "Admin user ID") @RequestParam Long adminId) {
        
        AdminDashboardDto dashboard = executionService.getAdminDashboard(adminId);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get user's personal task list
     */
    @GetMapping("/tasks")
    @Operation(summary = "Get User Tasks", description = "Retrieves all tasks assigned to the authenticated user")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> getUserTasks(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Filter by task status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) String priority) {
        
        List<WorkflowInstanceTaskDto> tasks = executionService.getTasksAssignedToUser(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get user's workflow participation
     */
    @GetMapping("/workflows")
    @Operation(summary = "Get User Workflows", description = "Retrieves all workflows the user is participating in")
    public ResponseEntity<List<WorkflowInstanceDto>> getUserWorkflows(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Filter by workflow status") @RequestParam(required = false) String status) {
        
        List<WorkflowInstanceDto> workflows = executionService.getWorkflowInstancesByProcessOwner(userId, status, null);
        return ResponseEntity.ok(workflows);
    }
    
    // ===== SEARCH ENDPOINTS =====
    
    @GetMapping("/search/tasks")
    @Operation(summary = "Search User Tasks", description = "Search user tasks with multiple criteria")
    public ResponseEntity<List<WorkflowInstanceTaskDto>> searchUserTasks(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Task status") @RequestParam(required = false) String status,
            @Parameter(description = "Priority") @RequestParam(required = false) String priority,
            @Parameter(description = "Started after date (ISO format)") @RequestParam(required = false) String startedAfter,
            @Parameter(description = "Started before date (ISO format)") @RequestParam(required = false) String startedBefore,
            @Parameter(description = "Completed after date (ISO format)") @RequestParam(required = false) String completedAfter,
            @Parameter(description = "Completed before date (ISO format)") @RequestParam(required = false) String completedBefore) {
        
        List<WorkflowInstanceTaskDto> tasks = executionService.searchUserTasks(
                userId, status, priority, startedAfter, startedBefore, completedAfter, completedBefore);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/search/workflows")
    @Operation(summary = "Search User Workflows", description = "Search user workflows with multiple criteria")
    public ResponseEntity<List<WorkflowInstanceDto>> searchUserWorkflows(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Workflow status") @RequestParam(required = false) String status,
            @Parameter(description = "Started after date (ISO format)") @RequestParam(required = false) String startedAfter,
            @Parameter(description = "Started before date (ISO format)") @RequestParam(required = false) String startedBefore,
            @Parameter(description = "Completed after date (ISO format)") @RequestParam(required = false) String completedAfter,
            @Parameter(description = "Completed before date (ISO format)") @RequestParam(required = false) String completedBefore) {
        
        List<WorkflowInstanceDto> workflows = executionService.searchUserWorkflows(
                userId, status, startedAfter, startedBefore, completedAfter, completedBefore);
        return ResponseEntity.ok(workflows);
    }
    
    @GetMapping("/search/activities")
    @Operation(summary = "Search User Activities", description = "Search user activities with multiple criteria")
    public ResponseEntity<List<UserActivityDto>> searchUserActivities(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Activity type") @RequestParam(required = false) String activityType,
            @Parameter(description = "Started after date (ISO format)") @RequestParam(required = false) String startedAfter,
            @Parameter(description = "Started before date (ISO format)") @RequestParam(required = false) String startedBefore,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "50") Integer limit) {
        
        List<UserActivityDto> activities = executionService.searchUserActivities(
                userId, activityType, startedAfter, startedBefore, limit);
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/search/notifications")
    @Operation(summary = "Search User Notifications", description = "Search user notifications with multiple criteria")
    public ResponseEntity<List<UserNotificationDto>> searchUserNotifications(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Notification status") @RequestParam(required = false) String status,
            @Parameter(description = "Notification type") @RequestParam(required = false) String type,
            @Parameter(description = "Created after date (ISO format)") @RequestParam(required = false) String createdAfter,
            @Parameter(description = "Created before date (ISO format)") @RequestParam(required = false) String createdBefore) {
        
        List<UserNotificationDto> notifications = executionService.searchUserNotifications(
                userId, status, type, createdAfter, createdBefore);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get user's workload summary
     */
    @GetMapping("/workload")
    @Operation(summary = "Get User Workload", description = "Retrieves workload summary for the authenticated user")
    public ResponseEntity<UserWorkloadDto> getUserWorkload(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        UserWorkloadDto workload = executionService.getUserWorkload(userId);
        return ResponseEntity.ok(workload);
    }

    /**
     * Get user's recent activities
     */
    @GetMapping("/activities")
    @Operation(summary = "Get User Activities", description = "Retrieves recent activities and actions for the authenticated user")
    public ResponseEntity<List<UserActivityDto>> getUserActivities(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Number of activities to retrieve") @RequestParam(defaultValue = "10") Integer limit) {
        
        List<UserActivityDto> activities = executionService.getUserActivities(userId, limit);
        return ResponseEntity.ok(activities);
    }

    /**
     * Get user's notifications
     */
    @GetMapping("/notifications")
    @Operation(summary = "Get User Notifications", description = "Retrieves all notifications for the authenticated user")
    public ResponseEntity<List<UserNotificationDto>> getUserNotifications(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Filter by notification status") @RequestParam(required = false) String status) {
        
        List<UserNotificationDto> notifications = executionService.getUserNotifications(userId, status);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark notification as read
     */
    @PatchMapping("/notifications/{notificationId}/read")
    @Operation(summary = "Mark Notification as Read", description = "Marks a notification as read for the authenticated user")
    public ResponseEntity<UserNotificationDto> markNotificationAsRead(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId) {
        
        UserNotificationDto notification = executionService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    /**
     * Get user's calendar view
     */
    @GetMapping("/calendar")
    @Operation(summary = "Get User Calendar", description = "Retrieves calendar view for the authenticated user")
    public ResponseEntity<UserCalendarDto> getUserCalendar(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Start date (ISO 8601)") @RequestParam String startDate,
            @Parameter(description = "End date (ISO 8601)") @RequestParam String endDate) {
        
        UserCalendarDto calendar = executionService.getUserCalendar(userId, startDate, endDate);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Get user's performance metrics
     */
    @GetMapping("/performance")
    @Operation(summary = "Get User Performance", description = "Retrieves performance metrics for the authenticated user")
    public ResponseEntity<UserPerformanceDto> getUserPerformance(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Performance period (WEEKLY, MONTHLY, QUARTERLY)") @RequestParam(defaultValue = "MONTHLY") String period) {
        
        UserPerformanceDto performance = executionService.getUserPerformance(userId, period);
        return ResponseEntity.ok(performance);
    }

    /**
     * Get user's role information
     */
    @GetMapping("/roles")
    @Operation(summary = "Get User Roles", description = "Retrieves all roles assigned to the authenticated user")
    public ResponseEntity<List<WorkflowRoleDto>> getUserRoles(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        List<WorkflowRoleDto> roles = executionService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }

    /**
     * Get user's permissions
     */
    @GetMapping("/permissions")
    @Operation(summary = "Get User Permissions", description = "Retrieves all permissions for the authenticated user based on their roles")
    public ResponseEntity<List<UserPermissionDto>> getUserPermissions(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        List<UserPermissionDto> permissions = executionService.getUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * Get user's escalation hierarchy
     */
    @GetMapping("/escalation-hierarchy")
    @Operation(summary = "Get User Escalation Hierarchy", description = "Retrieves escalation hierarchy for the authenticated user")
    public ResponseEntity<List<WorkflowUserDto>> getUserEscalationHierarchy(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        List<WorkflowUserDto> escalationHierarchy = userService.getEscalationHierarchy(userId);
        return ResponseEntity.ok(escalationHierarchy);
    }

    /**
     * Get user's team information
     */
    @GetMapping("/team")
    @Operation(summary = "Get User Team", description = "Retrieves team information for the authenticated user")
    public ResponseEntity<UserTeamDto> getUserTeam(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        UserTeamDto team = executionService.getUserTeam(userId);
        return ResponseEntity.ok(team);
    }

    /**
     * Get user's preferences
     */
    @GetMapping("/preferences")
    @Operation(summary = "Get User Preferences", description = "Retrieves user preferences and settings")
    public ResponseEntity<UserPreferencesDto> getUserPreferences(
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        UserPreferencesDto preferences = executionService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    /**
     * Update user's preferences
     */
    @PutMapping("/preferences")
    @Operation(summary = "Update User Preferences", description = "Updates user preferences and settings")
    public ResponseEntity<UserPreferencesDto> updateUserPreferences(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @RequestBody UserPreferencesDto preferences) {
        
        UserPreferencesDto updatedPreferences = executionService.updateUserPreferences(userId, preferences);
        return ResponseEntity.ok(updatedPreferences);
    }
}
