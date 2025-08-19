package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for User Dashboard
 */
public class UserDashboardDto {
    
    private Long userId;
    private String username;
    private String userRole;
    private DashboardSummaryDto summary;
    private List<WorkflowInstanceTaskDto> myTasks;
    private List<WorkflowInstanceDto> myWorkflows;
    private List<UserActivityDto> recentActivities;
    private List<UserNotificationDto> notifications;
    private UserWorkloadDto workload;
    private LocalDateTime lastUpdated;

    // Default constructor
    public UserDashboardDto() {}

    // Constructor with required fields
    public UserDashboardDto(Long userId, String username, String userRole) {
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public DashboardSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(DashboardSummaryDto summary) {
        this.summary = summary;
    }

    public List<WorkflowInstanceTaskDto> getMyTasks() {
        return myTasks;
    }

    public void setMyTasks(List<WorkflowInstanceTaskDto> myTasks) {
        this.myTasks = myTasks;
    }

    public List<WorkflowInstanceDto> getMyWorkflows() {
        return myWorkflows;
    }

    public void setMyWorkflows(List<WorkflowInstanceDto> myWorkflows) {
        this.myWorkflows = myWorkflows;
    }

    public List<UserActivityDto> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<UserActivityDto> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public List<UserNotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<UserNotificationDto> notifications) {
        this.notifications = notifications;
    }

    public UserWorkloadDto getWorkload() {
        return workload;
    }

    public void setWorkload(UserWorkloadDto workload) {
        this.workload = workload;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Inner class for dashboard summary
     */
    public static class DashboardSummaryDto {
        private int totalTasks;
        private int pendingTasks;
        private int completedTasks;
        private int overdueTasks;
        private int totalWorkflows;
        private int activeWorkflows;
        private int notifications;
        private double taskCompletionRate;

        // Default constructor
        public DashboardSummaryDto() {}

        // Getters and Setters
        public int getTotalTasks() {
            return totalTasks;
        }

        public void setTotalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
        }

        public int getPendingTasks() {
            return pendingTasks;
        }

        public void setPendingTasks(int pendingTasks) {
            this.pendingTasks = pendingTasks;
        }

        public int getCompletedTasks() {
            return completedTasks;
        }

        public void setCompletedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
        }

        public int getOverdueTasks() {
            return overdueTasks;
        }

        public void setOverdueTasks(int overdueTasks) {
            this.overdueTasks = overdueTasks;
        }

        public int getTotalWorkflows() {
            return totalWorkflows;
        }

        public void setTotalWorkflows(int totalWorkflows) {
            this.totalWorkflows = totalWorkflows;
        }

        public int getActiveWorkflows() {
            return activeWorkflows;
        }

        public void setActiveWorkflows(int activeWorkflows) {
            this.activeWorkflows = activeWorkflows;
        }

        public int getNotifications() {
            return notifications;
        }

        public void setNotifications(int notifications) {
            this.notifications = notifications;
        }

        public double getTaskCompletionRate() {
            return taskCompletionRate;
        }

        public void setTaskCompletionRate(double taskCompletionRate) {
            this.taskCompletionRate = taskCompletionRate;
        }
    }
}
