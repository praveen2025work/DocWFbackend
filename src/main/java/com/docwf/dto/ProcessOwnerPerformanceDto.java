package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Process Owner Performance
 */
public class ProcessOwnerPerformanceDto {
    
    private Long processOwnerId;
    private String processOwnerName;
    private String period; // WEEKLY, MONTHLY, QUARTERLY
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    
    // Performance Metrics
    private double workflowCompletionRate;
    private double taskCompletionRate;
    private double averageWorkflowDuration;
    private double averageTaskDuration;
    private int escalationsHandled;
    private int reassignmentsMade;
    private double slaComplianceRate;
    private double customerSatisfactionScore;
    
    // Efficiency Metrics
    private double productivityScore;
    private double qualityScore;
    private double timelinessScore;
    private double collaborationScore;
    
    // Comparative Metrics
    private double performanceRanking;
    private double teamAverage;
    private double improvementRate;

    // Default constructor
    public ProcessOwnerPerformanceDto() {}

    // Constructor with required fields
    public ProcessOwnerPerformanceDto(Long processOwnerId, String processOwnerName, String period) {
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

    public double getWorkflowCompletionRate() {
        return workflowCompletionRate;
    }

    public void setWorkflowCompletionRate(double workflowCompletionRate) {
        this.workflowCompletionRate = workflowCompletionRate;
    }

    public double getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(double taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public double getAverageWorkflowDuration() {
        return averageWorkflowDuration;
    }

    public void setAverageWorkflowDuration(double averageWorkflowDuration) {
        this.averageWorkflowDuration = averageWorkflowDuration;
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

    public double getSlaComplianceRate() {
        return slaComplianceRate;
    }

    public void setSlaComplianceRate(double slaComplianceRate) {
        this.slaComplianceRate = slaComplianceRate;
    }

    public double getCustomerSatisfactionScore() {
        return customerSatisfactionScore;
    }

    public void setCustomerSatisfactionScore(double customerSatisfactionScore) {
        this.customerSatisfactionScore = customerSatisfactionScore;
    }

    public double getProductivityScore() {
        return productivityScore;
    }

    public void setProductivityScore(double productivityScore) {
        this.productivityScore = productivityScore;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
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
        return (workflowCompletionRate + taskCompletionRate + slaComplianceRate + customerSatisfactionScore) / 4.0;
    }

    /**
     * Calculate efficiency score
     */
    public double calculateEfficiencyScore() {
        return (productivityScore + qualityScore + timelinessScore + collaborationScore) / 4.0;
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
