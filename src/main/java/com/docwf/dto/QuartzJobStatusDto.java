package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * DTO for Quartz job status information
 */
public class QuartzJobStatusDto {
    
    private String jobName;
    private String jobGroup;
    private String description;
    private String status; // RUNNING, PAUSED, COMPLETED, FAILED, STUCK
    private LocalDateTime lastRunTime;
    private LocalDateTime nextRunTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long executionTime; // in milliseconds
    private String cronExpression;
    private Boolean isStuck;
    private String stuckReason;
    private Integer retryCount;
    private String lastError;
    private LocalDateTime lastErrorTime;
    private String triggerState;
    private String jobClass;
    private Boolean durable;
    private Boolean requestsRecovery;
    private Integer misfireInstruction;
    private String calendarName;
    private Long calendarId;
    private String workflowName;
    private Long workflowId;
    
    // Constructors
    public QuartzJobStatusDto() {}
    
    public QuartzJobStatusDto(String jobName, String jobGroup, String status) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.status = status;
    }
    
    // Getters and Setters
    public String getJobName() {
        return jobName;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    public String getJobGroup() {
        return jobGroup;
    }
    
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }
    
    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }
    
    public LocalDateTime getNextRunTime() {
        return nextRunTime;
    }
    
    public void setNextRunTime(LocalDateTime nextRunTime) {
        this.nextRunTime = nextRunTime;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public Boolean getIsStuck() {
        return isStuck;
    }
    
    public void setIsStuck(Boolean isStuck) {
        this.isStuck = isStuck;
    }
    
    public String getStuckReason() {
        return stuckReason;
    }
    
    public void setStuckReason(String stuckReason) {
        this.stuckReason = stuckReason;
    }
    
    public Integer getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    
    public LocalDateTime getLastErrorTime() {
        return lastErrorTime;
    }
    
    public void setLastErrorTime(LocalDateTime lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }
    
    public String getTriggerState() {
        return triggerState;
    }
    
    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }
    
    public String getJobClass() {
        return jobClass;
    }
    
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }
    
    public Boolean getDurable() {
        return durable;
    }
    
    public void setDurable(Boolean durable) {
        this.durable = durable;
    }
    
    public Boolean getRequestsRecovery() {
        return requestsRecovery;
    }
    
    public void setRequestsRecovery(Boolean requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }
    
    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }
    
    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }
    
    public String getCalendarName() {
        return calendarName;
    }
    
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }
    
    public Long getCalendarId() {
        return calendarId;
    }
    
    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    // Additional methods for scheduler management
    public Integer getTriggerCount() {
        return triggerCount;
    }
    
    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }
    
    public java.util.Date getNextFireTime() {
        return nextFireTime;
    }
    
    public void setNextFireTime(java.util.Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
    
    public java.util.Date getPreviousFireTime() {
        return previousFireTime;
    }
    
    public void setPreviousFireTime(java.util.Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }
    
    private Integer triggerCount;
    private java.util.Date nextFireTime;
    private java.util.Date previousFireTime;
}
