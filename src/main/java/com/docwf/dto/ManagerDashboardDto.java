package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Manager Dashboard
 */
public class ManagerDashboardDto {
    
    private Long managerId;
    private String managerName;
    private DashboardSummaryDto summary;
    private List<WorkflowInstanceDto> teamWorkflows;
    private List<WorkflowInstanceTaskDto> teamTasks;
    private List<WorkflowUserDto> teamMembers;
    private List<EscalationItemDto> teamEscalations;
    private TeamPerformanceDto teamPerformance;
    private LocalDateTime lastUpdated;

    // Default constructor
    public ManagerDashboardDto() {}

    // Constructor with required fields
    public ManagerDashboardDto(Long managerId, String managerName) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public DashboardSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(DashboardSummaryDto summary) {
        this.summary = summary;
    }

    public List<WorkflowInstanceDto> getTeamWorkflows() {
        return teamWorkflows;
    }

    public void setTeamWorkflows(List<WorkflowInstanceDto> teamWorkflows) {
        this.teamWorkflows = teamWorkflows;
    }

    public List<WorkflowInstanceTaskDto> getTeamTasks() {
        return teamTasks;
    }

    public void setTeamTasks(List<WorkflowInstanceTaskDto> teamTasks) {
        this.teamTasks = teamTasks;
    }

    public List<WorkflowUserDto> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<WorkflowUserDto> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<EscalationItemDto> getTeamEscalations() {
        return teamEscalations;
    }

    public void setTeamEscalations(List<EscalationItemDto> teamEscalations) {
        this.teamEscalations = teamEscalations;
    }

    public TeamPerformanceDto getTeamPerformance() {
        return teamPerformance;
    }

    public void setTeamPerformance(TeamPerformanceDto teamPerformance) {
        this.teamPerformance = teamPerformance;
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
        private int totalTeamMembers;
        private int activeWorkflows;
        private int pendingTasks;
        private int escalatedItems;
        private double teamProductivity;
        private double slaCompliance;

        // Default constructor
        public DashboardSummaryDto() {}

        // Getters and Setters
        public int getTotalTeamMembers() {
            return totalTeamMembers;
        }

        public void setTotalTeamMembers(int totalTeamMembers) {
            this.totalTeamMembers = totalTeamMembers;
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

        public double getTeamProductivity() {
            return teamProductivity;
        }

        public void setTeamProductivity(double teamProductivity) {
            this.teamProductivity = teamProductivity;
        }

        public double getSlaCompliance() {
            return slaCompliance;
        }

        public void setSlaCompliance(double slaCompliance) {
            this.slaCompliance = slaCompliance;
        }
    }

    /**
     * Inner class for team performance
     */
    public static class TeamPerformanceDto {
        private double averageTaskCompletionTime;
        private double averageWorkflowCompletionTime;
        private int totalEscalations;
        private int totalReassignments;
        private double customerSatisfactionScore;

        // Default constructor
        public TeamPerformanceDto() {}

        // Getters and Setters
        public double getAverageTaskCompletionTime() {
            return averageTaskCompletionTime;
        }

        public void setAverageTaskCompletionTime(double averageTaskCompletionTime) {
            this.averageTaskCompletionTime = averageTaskCompletionTime;
        }

        public double getAverageWorkflowCompletionTime() {
            return averageWorkflowCompletionTime;
        }

        public void setAverageWorkflowCompletionTime(double averageWorkflowCompletionTime) {
            this.averageWorkflowCompletionTime = averageWorkflowCompletionTime;
        }

        public int getTotalEscalations() {
            return totalEscalations;
        }

        public void setTotalEscalations(int totalEscalations) {
            this.totalEscalations = totalEscalations;
        }

        public int getTotalReassignments() {
            return totalReassignments;
        }

        public void setTotalReassignments(int totalReassignments) {
            this.totalReassignments = totalReassignments;
        }

        public double getCustomerSatisfactionScore() {
            return customerSatisfactionScore;
        }

        public void setCustomerSatisfactionScore(double customerSatisfactionScore) {
            this.customerSatisfactionScore = customerSatisfactionScore;
        }
    }
}
