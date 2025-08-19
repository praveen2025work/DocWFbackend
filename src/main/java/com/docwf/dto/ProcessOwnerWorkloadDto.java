package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Process Owner Workload
 */
public class ProcessOwnerWorkloadDto {
    
    private Long processOwnerId;
    private String processOwnerName;
    private int totalWorkflows;
    private int activeWorkflows;
    private int completedWorkflows;
    private int escalatedWorkflows;
    private int totalTasks;
    private int pendingTasks;
    private int inProgressTasks;
    private int completedTasks;
    private int overdueTasks;
    private double workloadPercentage;
    private LocalDateTime lastTaskAssigned;
    private LocalDateTime lastWorkflowStarted;
    private int escalationsHandled;
    private int reassignmentsMade;

    // Default constructor
    public ProcessOwnerWorkloadDto() {}

    // Constructor with required fields
    public ProcessOwnerWorkloadDto(Long processOwnerId, String processOwnerName) {
        this.processOwnerId = processOwnerId;
        this.processOwnerName = processOwnerName;
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

    public int getCompletedWorkflows() {
        return completedWorkflows;
    }

    public void setCompletedWorkflows(int completedWorkflows) {
        this.completedWorkflows = completedWorkflows;
    }

    public int getEscalatedWorkflows() {
        return escalatedWorkflows;
    }

    public void setEscalatedWorkflows(int escalatedWorkflows) {
        this.escalatedWorkflows = escalatedWorkflows;
    }

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

    public int getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(int inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
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

    public double getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(double workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }

    public LocalDateTime getLastTaskAssigned() {
        return lastTaskAssigned;
    }

    public void setLastTaskAssigned(LocalDateTime lastTaskAssigned) {
        this.lastTaskAssigned = lastTaskAssigned;
    }

    public LocalDateTime getLastWorkflowStarted() {
        return lastWorkflowStarted;
    }

    public void setLastWorkflowStarted(LocalDateTime lastWorkflowStarted) {
        this.lastWorkflowStarted = lastWorkflowStarted;
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

    /**
     * Calculate workload percentage based on active tasks
     */
    public void calculateWorkloadPercentage() {
        if (totalTasks > 0) {
            this.workloadPercentage = (double) (inProgressTasks + pendingTasks) / totalTasks * 100;
        } else {
            this.workloadPercentage = 0.0;
        }
    }

    /**
     * Get completion rate for workflows
     */
    public double getWorkflowCompletionRate() {
        if (totalWorkflows > 0) {
            return (double) completedWorkflows / totalWorkflows * 100;
        }
        return 0.0;
    }

    /**
     * Get completion rate for tasks
     */
    public double getTaskCompletionRate() {
        if (totalTasks > 0) {
            return (double) completedTasks / totalTasks * 100;
        }
        return 0.0;
    }
}
