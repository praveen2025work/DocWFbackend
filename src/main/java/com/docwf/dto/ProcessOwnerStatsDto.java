package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Process Owner Statistics
 */
public class ProcessOwnerStatsDto {
    
    private Long processOwnerId;
    private String processOwnerName;
    private String period; // DAILY, WEEKLY, MONTHLY, QUARTERLY
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    
    // Workflow Statistics
    private int totalWorkflows;
    private int completedWorkflows;
    private int activeWorkflows;
    private int escalatedWorkflows;
    private double workflowCompletionRate;
    private double averageWorkflowDuration;
    
    // Task Statistics
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private int overdueTasks;
    private double taskCompletionRate;
    private double averageTaskDuration;
    
    // Performance Metrics
    private int escalationsHandled;
    private int reassignmentsMade;
    private double customerSatisfactionScore;
    private int slaBreaches;
    private double slaComplianceRate;
    
    // Team Performance
    private int teamMembers;
    private double teamProductivity;
    private int teamEscalations;
    private double teamSatisfactionScore;

    // Default constructor
    public ProcessOwnerStatsDto() {}

    // Constructor with required fields
    public ProcessOwnerStatsDto(Long processOwnerId, String processOwnerName, String period) {
        this.processOwnerId = processOwnerId;
        this.processOwnerName = processOwnerName;
        this.period = period;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }

    public int getTotalWorkflows() {
        return totalWorkflows;
    }

    public void setTotalWorkflows(int totalWorkflows) {
        this.totalWorkflows = totalWorkflows;
    }

    public int getCompletedWorkflows() {
        return completedWorkflows;
    }

    public void setCompletedWorkflows(int completedWorkflows) {
        this.completedWorkflows = completedWorkflows;
    }

    public int getActiveWorkflows() {
        return activeWorkflows;
    }

    public void setActiveWorkflows(int activeWorkflows) {
        this.activeWorkflows = activeWorkflows;
    }

    public int getEscalatedWorkflows() {
        return escalatedWorkflows;
    }

    public void setEscalatedWorkflows(int escalatedWorkflows) {
        this.escalatedWorkflows = escalatedWorkflows;
    }

    public double getWorkflowCompletionRate() {
        return workflowCompletionRate;
    }

    public void setWorkflowCompletionRate(double workflowCompletionRate) {
        this.workflowCompletionRate = workflowCompletionRate;
    }

    public double getAverageWorkflowDuration() {
        return averageWorkflowDuration;
    }

    public void setAverageWorkflowDuration(double averageWorkflowDuration) {
        this.averageWorkflowDuration = averageWorkflowDuration;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public int getOverdueTasks() {
        return overdueTasks;
    }

    public void setOverdueTasks(int overdueTasks) {
        this.overdueTasks = overdueTasks;
    }

    public double getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(double taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public double getAverageTaskDuration() {
        return averageTaskDuration;
    }

    public void setAverageTaskDuration(double averageTaskDuration) {
        this.averageTaskDuration = averageTaskDuration;
    }

    public int getEscalationsHandled() {
        return escalationsHandled;
    }

    public void setEscalationsHandled(int escalationsHandled) {
        this.escalationsHandled = escalationsHandled;
    }

    public int getReassignmentsMade() {
        return reassignmentsMade;
    }

    public void setReassignmentsMade(int reassignmentsMade) {
        this.reassignmentsMade = reassignmentsMade;
    }

    public double getCustomerSatisfactionScore() {
        return customerSatisfactionScore;
    }

    public void setCustomerSatisfactionScore(double customerSatisfactionScore) {
        this.customerSatisfactionScore = customerSatisfactionScore;
    }

    public int getSlaBreaches() {
        return slaBreaches;
    }

    public void setSlaBreaches(int slaBreaches) {
        this.slaBreaches = slaBreaches;
    }

    public double getSlaComplianceRate() {
        return slaComplianceRate;
    }

    public void setSlaComplianceRate(double slaComplianceRate) {
        this.slaComplianceRate = slaComplianceRate;
    }

    public int getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(int teamMembers) {
        this.teamMembers = teamMembers;
    }

    public double getTeamProductivity() {
        return teamProductivity;
    }

    public void setTeamProductivity(double teamProductivity) {
        this.teamProductivity = teamProductivity;
    }

    public int getTeamEscalations() {
        return teamEscalations;
    }

    public void setTeamEscalations(int teamEscalations) {
        this.teamEscalations = teamEscalations;
    }

    public double getTeamSatisfactionScore() {
        return teamSatisfactionScore;
    }

    public void setTeamSatisfactionScore(double teamSatisfactionScore) {
        this.teamSatisfactionScore = teamSatisfactionScore;
    }
}
