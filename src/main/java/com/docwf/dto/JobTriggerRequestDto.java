package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for job trigger requests
 */
public class JobTriggerRequestDto {
    
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private String cronExpression;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer priority;
    private String description;
    private Map<String, Object> jobDataMap;
    private Integer misfireInstruction;
    private Boolean requestRecovery;
    private String calendarName;
    private Long calendarId;
    private String workflowName;
    private Long workflowId;
    private String reason;
    
    // Constructors
    public JobTriggerRequestDto() {}
    
    public JobTriggerRequestDto(String jobName, String jobGroup, String cronExpression) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.cronExpression = cronExpression;
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
    
    public String getTriggerName() {
        return triggerName;
    }
    
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
    
    public String getTriggerGroup() {
        return triggerGroup;
    }
    
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }
    
    public void setJobDataMap(Map<String, Object> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
    
    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }
    
    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }
    
    public Boolean getRequestRecovery() {
        return requestRecovery;
    }
    
    public void setRequestRecovery(Boolean requestRecovery) {
        this.requestRecovery = requestRecovery;
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
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}
