package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Process Owner Dashboard
 */
public class ProcessOwnerDashboardDto {
    
    private Long processOwnerId;
    private String processOwnerName;
    private DashboardSummaryDto summary;
    private List<WorkflowInstanceDto> activeWorkflows;
    private List<WorkflowInstanceTaskDto> pendingTasks;
    private List<EscalationItemDto> escalationQueue;
    private List<ProcessOwnerStatsDto> recentStats;
    private LocalDateTime lastUpdated;

    // Default constructor
    public ProcessOwnerDashboardDto() {}

    // Constructor with required fields
    public ProcessOwnerDashboardDto(Long processOwnerId, String processOwnerName) {
        this.processOwnerId = processOwnerId;
        this.processOwnerName = processOwnerName;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getProcessOwnerId() {
        return processOwnerId;
    }

    public void setProcessOwnerId(Long processOwnerId) {
        this.processOwnerId = processOwnerId;
    }

    public String getProcessOwnerName() {
        return processOwnerName;
    }

    public void setProcessOwnerName(String processOwnerName) {
        this.processOwnerName = processOwnerName;
    }

    public DashboardSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(DashboardSummaryDto summary) {
        this.summary = summary;
    }

    public List<WorkflowInstanceDto> getActiveWorkflows() {
        return activeWorkflows;
    }

    public void setActiveWorkflows(List<WorkflowInstanceDto> activeWorkflows) {
        this.activeWorkflows = activeWorkflows;
    }

    public List<WorkflowInstanceTaskDto> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<WorkflowInstanceTaskDto> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public List<EscalationItemDto> getEscalationQueue() {
        return escalationQueue;
    }

    public void setEscalationQueue(List<EscalationItemDto> escalationQueue) {
        this.escalationQueue = escalationQueue;
    }

    public List<ProcessOwnerStatsDto> getRecentStats() {
        return recentStats;
    }

    public void setRecentStats(List<ProcessOwnerStatsDto> recentStats) {
        this.recentStats = recentStats;
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
        private int totalWorkflows;
        private int activeWorkflows;
        private int pendingTasks;
        private int escalatedItems;
        private int completedToday;
        private double completionRate;

        // Default constructor
        public DashboardSummaryDto() {}

        // Getters and Setters
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

        public int getPendingTasks() {
            return pendingTasks;
        }

        public void setPendingTasks(int pendingTasks) {
            this.pendingTasks = pendingTasks;
        }

        public int getEscalatedItems() {
            return escalatedItems;
        }

        public void setEscalatedItems(int escalatedItems) {
            this.escalatedItems = escalatedItems;
        }

        public int getCompletedToday() {
            return completedToday;
        }

        public void setCompletedToday(int completedToday) {
            this.completedToday = completedToday;
        }

        public double getCompletionRate() {
            return completionRate;
        }

        public void setCompletionRate(double completionRate) {
            this.completionRate = completionRate;
        }
    }
}
