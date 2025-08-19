package com.docwf.dto;

import java.time.LocalDateTime;

public class WorkflowInstanceStatsDto {
    
    private Long workflowId;
    private String workflowName;
    private int totalInstances;
    private int completedInstances;
    private int inProgressInstances;
    private int pendingInstances;
    private int failedInstances;
    private int cancelledInstances;
    private double successRate;
    private double averageCompletionTime;
    private LocalDateTime lastStarted;
    private LocalDateTime lastCompleted;
    
    // Constructors
    public WorkflowInstanceStatsDto() {}
    
    public WorkflowInstanceStatsDto(Long workflowId, String workflowName) {
        this.workflowId = workflowId;
        this.workflowName = workflowName;
    }
    
    // Getters and Setters
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    public int getTotalInstances() {
        return totalInstances;
    }
    
    public void setTotalInstances(int totalInstances) {
        this.totalInstances = totalInstances;
    }
    
    public int getCompletedInstances() {
        return completedInstances;
    }
    
    public void setCompletedInstances(int completedInstances) {
        this.completedInstances = completedInstances;
    }
    
    public int getInProgressInstances() {
        return inProgressInstances;
    }
    
    public void setInProgressInstances(int inProgressInstances) {
        this.inProgressInstances = inProgressInstances;
    }
    
    public int getPendingInstances() {
        return pendingInstances;
    }
    
    public void setPendingInstances(int pendingInstances) {
        this.pendingInstances = pendingInstances;
    }
    
    public int getFailedInstances() {
        return failedInstances;
    }
    
    public void setFailedInstances(int failedInstances) {
        this.failedInstances = failedInstances;
    }
    
    public int getCancelledInstances() {
        return cancelledInstances;
    }
    
    public void setCancelledInstances(int cancelledInstances) {
        this.cancelledInstances = cancelledInstances;
    }
    
    public double getSuccessRate() {
        return successRate;
    }
    
    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
    
    public double getAverageCompletionTime() {
        return averageCompletionTime;
    }
    
    public void setAverageCompletionTime(double averageCompletionTime) {
        this.averageCompletionTime = averageCompletionTime;
    }
    
    public LocalDateTime getLastStarted() {
        return lastStarted;
    }
    
    public void setLastStarted(LocalDateTime lastStarted) {
        this.lastStarted = lastStarted;
    }
    
    public LocalDateTime getLastCompleted() {
        return lastCompleted;
    }
    
    public void setLastCompleted(LocalDateTime lastCompleted) {
        this.lastCompleted = lastCompleted;
    }
    
    // Utility methods
    public void calculateSuccessRate() {
        if (totalInstances > 0) {
            this.successRate = (double) completedInstances / totalInstances * 100;
        }
    }
    
    public boolean hasActiveInstances() {
        return inProgressInstances > 0 || pendingInstances > 0;
    }
    
    public int getActiveInstances() {
        return inProgressInstances + pendingInstances;
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceStatsDto{" +
                "workflowId=" + workflowId +
                ", workflowName='" + workflowName + '\'' +
                ", totalInstances=" + totalInstances +
                ", completedInstances=" + completedInstances +
                ", successRate=" + successRate + "%" +
                '}';
    }
}
