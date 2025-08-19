package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User Performance
 */
public class UserPerformanceDto {
    
    private Long userId;
    private String username;
    private String period; // WEEKLY, MONTHLY, QUARTERLY
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    
    // Task Performance
    private int totalTasks;
    private int completedTasks;
    private int overdueTasks;
    private double taskCompletionRate;
    private double averageTaskDuration;
    private double taskQualityScore;
    
    // Workflow Performance
    private int totalWorkflows;
    private int completedWorkflows;
    private double workflowCompletionRate;
    private double averageWorkflowDuration;
    
    // Efficiency Metrics
    private double productivityScore;
    private double timelinessScore;
    private double collaborationScore;
    private double slaComplianceRate;
    
    // Comparative Metrics
    private double performanceRanking;
    private double teamAverage;
    private double improvementRate;

    // Default constructor
    public UserPerformanceDto() {}

    // Constructor with required fields
    public UserPerformanceDto(Long userId, String username, String period) {
        this.userId = userId;
        this.username = username;
        this.period = period;
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

    public double getTaskQualityScore() {
        return taskQualityScore;
    }

    public void setTaskQualityScore(double taskQualityScore) {
        this.taskQualityScore = taskQualityScore;
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

    public double getProductivityScore() {
        return productivityScore;
    }

    public void setProductivityScore(double productivityScore) {
        this.productivityScore = productivityScore;
    }

    public double getTimelinessScore() {
        return timelinessScore;
    }

    public void setTimelinessScore(double timelinessScore) {
        this.timelinessScore = timelinessScore;
    }

    public double getCollaborationScore() {
        return collaborationScore;
    }

    public void setCollaborationScore(double collaborationScore) {
        this.collaborationScore = collaborationScore;
    }

    public double getSlaComplianceRate() {
        return slaComplianceRate;
    }

    public void setSlaComplianceRate(double slaComplianceRate) {
        this.slaComplianceRate = slaComplianceRate;
    }

    public double getPerformanceRanking() {
        return performanceRanking;
    }

    public void setPerformanceRanking(double performanceRanking) {
        this.performanceRanking = performanceRanking;
    }

    public double getTeamAverage() {
        return teamAverage;
    }

    public void setTeamAverage(double teamAverage) {
        this.teamAverage = teamAverage;
    }

    public double getImprovementRate() {
        return improvementRate;
    }

    public void setImprovementRate(double improvementRate) {
        this.improvementRate = improvementRate;
    }

    /**
     * Calculate overall performance score
     */
    public double calculateOverallScore() {
        return (taskCompletionRate + workflowCompletionRate + slaComplianceRate + taskQualityScore) / 4.0;
    }

    /**
     * Calculate efficiency score
     */
    public double calculateEfficiencyScore() {
        return (productivityScore + timelinessScore + collaborationScore) / 3.0;
    }

    /**
     * Check if performance is above team average
     */
    public boolean isAboveTeamAverage() {
        return calculateOverallScore() > teamAverage;
    }

    /**
     * Get performance trend
     */
    public String getPerformanceTrend() {
        if (improvementRate > 5.0) {
            return "IMPROVING";
        } else if (improvementRate < -5.0) {
            return "DECLINING";
        } else {
            return "STABLE";
        }
    }
}
