package com.docwf.dto;

import java.time.LocalDateTime;

public class UserWorkloadDto {
    
    private Long userId;
    private String username;
    private String fullName;
    private int totalAssignedTasks;
    private int pendingTasks;
    private int inProgressTasks;
    private int completedTasks;
    private int overdueTasks;
    private double workloadPercentage;
    private LocalDateTime lastTaskAssigned;
    private LocalDateTime lastTaskCompleted;
    private int averageTaskCompletionTime;
    
    // Constructors
    public UserWorkloadDto() {}
    
    public UserWorkloadDto(Long userId, String username, String fullName) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public int getTotalAssignedTasks() {
        return totalAssignedTasks;
    }
    
    public void setTotalAssignedTasks(int totalAssignedTasks) {
        this.totalAssignedTasks = totalAssignedTasks;
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
    
    public LocalDateTime getLastTaskCompleted() {
        return lastTaskCompleted;
    }
    
    public void setLastTaskCompleted(LocalDateTime lastTaskCompleted) {
        this.lastTaskCompleted = lastTaskCompleted;
    }
    
    public int getAverageTaskCompletionTime() {
        return averageTaskCompletionTime;
    }
    
    public void setAverageTaskCompletionTime(int averageTaskCompletionTime) {
        this.averageTaskCompletionTime = averageTaskCompletionTime;
    }
    
    // Utility methods
    public void calculateWorkloadPercentage() {
        if (totalAssignedTasks > 0) {
            this.workloadPercentage = (double) (inProgressTasks + pendingTasks) / totalAssignedTasks * 100;
        }
    }
    
    public boolean isOverloaded() {
        return workloadPercentage > 80;
    }
    
    public boolean hasOverdueTasks() {
        return overdueTasks > 0;
    }
    
    public int getActiveTasks() {
        return inProgressTasks + pendingTasks;
    }
    
    public double getCompletionRate() {
        if (totalAssignedTasks > 0) {
            return (double) completedTasks / totalAssignedTasks * 100;
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "UserWorkloadDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", activeTasks=" + getActiveTasks() +
                ", workload=" + workloadPercentage + "%" +
                '}';
    }
}
