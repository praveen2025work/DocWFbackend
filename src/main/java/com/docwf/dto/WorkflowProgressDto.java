package com.docwf.dto;

import java.time.LocalDateTime;

public class WorkflowProgressDto {
    
    private Long instanceId;
    private String workflowName;
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private int inProgressTasks;
    private int failedTasks;
    private double progressPercentage;
    private LocalDateTime estimatedCompletion;
    private String currentStatus;
    
    // Constructors
    public WorkflowProgressDto() {}
    
    public WorkflowProgressDto(Long instanceId, String workflowName) {
        this.instanceId = instanceId;
        this.workflowName = workflowName;
    }
    
    // Getters and Setters
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
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
    
    public int getInProgressTasks() {
        return inProgressTasks;
    }
    
    public void setInProgressTasks(int inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }
    
    public int getFailedTasks() {
        return failedTasks;
    }
    
    public void setFailedTasks(int failedTasks) {
        this.failedTasks = failedTasks;
    }
    
    public double getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    public LocalDateTime getEstimatedCompletion() {
        return estimatedCompletion;
    }
    
    public void setEstimatedCompletion(LocalDateTime estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }
    
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    // Utility methods
    public void calculateProgress() {
        if (totalTasks > 0) {
            this.progressPercentage = (double) completedTasks / totalTasks * 100;
        }
    }
    
    public boolean isCompleted() {
        return completedTasks == totalTasks;
    }
    
    public boolean hasFailures() {
        return failedTasks > 0;
    }
    
    @Override
    public String toString() {
        return "WorkflowProgressDto{" +
                "instanceId=" + instanceId +
                ", workflowName='" + workflowName + '\'' +
                ", progress=" + progressPercentage + "%" +
                ", completed=" + completedTasks + "/" + totalTasks +
                '}';
    }
}
