package com.docwf.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateWorkflowInstanceDto {
    
    @NotNull(message = "Workflow ID is required")
    private Long workflowId;
    
    @NotNull(message = "Started by user ID is required")
    private Long startedBy;
    
    private Long calendarId; // Optional calendar mapping
    
    private String triggeredBy; // System scheduler, manual, etc.
    
    private LocalDateTime scheduledStartTime; // For future execution
    
    // Constructors
    public CreateWorkflowInstanceDto() {}
    
    public CreateWorkflowInstanceDto(Long workflowId, Long startedBy) {
        this.workflowId = workflowId;
        this.startedBy = startedBy;
    }
    
    public CreateWorkflowInstanceDto(Long workflowId, Long startedBy, Long calendarId) {
        this.workflowId = workflowId;
        this.startedBy = startedBy;
        this.calendarId = calendarId;
    }
    
    // Getters and Setters
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public Long getStartedBy() {
        return startedBy;
    }
    
    public void setStartedBy(Long startedBy) {
        this.startedBy = startedBy;
    }
    
    public Long getCalendarId() {
        return calendarId;
    }
    
    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }
    
    public String getTriggeredBy() {
        return triggeredBy;
    }
    
    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
    
    public LocalDateTime getScheduledStartTime() {
        return scheduledStartTime;
    }
    
    public void setScheduledStartTime(LocalDateTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }
}
