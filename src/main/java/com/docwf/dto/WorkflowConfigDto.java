package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowConfigDto {
    
    private Long workflowId;
    
    @NotBlank(message = "Workflow name is required")
    private String name;
    
    private String description;
    
    private Integer reminderBeforeDueMins;
    
    private Integer minutesAfterDue;
    
    private Integer escalationAfterMins;
    
    private Integer dueInMins;
    
    @NotNull(message = "Active status is required")
    private String isActive;  // Y/N
    
    private Long calendarId;  // Reference to the assigned calendar for this workflow
    
    private String createdBy;
    
    private LocalDateTime createdOn;
    
    private String updatedBy;
    
    private LocalDateTime updatedOn;
    
    // Workflow trigger configuration
    private String triggerType; // MANUAL, SCHEDULED, EVENT
    
    // Related data
    private List<WorkflowConfigRoleDto> workflowRoles;
    private List<WorkflowConfigTaskDto> tasks;
    private List<WorkflowConfigParamDto> parameters;
    
    // For UI-based workflow creation with sequence mapping
    private List<WorkflowRoleDto> roles;
    
    // Constructors
    public WorkflowConfigDto() {}
    
    public WorkflowConfigDto(String name, String description, String isActive) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getReminderBeforeDueMins() {
        return reminderBeforeDueMins;
    }
    
    public void setReminderBeforeDueMins(Integer reminderBeforeDueMins) {
        this.reminderBeforeDueMins = reminderBeforeDueMins;
    }
    
    public Integer getMinutesAfterDue() {
        return minutesAfterDue;
    }
    
    public void setMinutesAfterDue(Integer minutesAfterDue) {
        this.minutesAfterDue = minutesAfterDue;
    }
    
    public Integer getEscalationAfterMins() {
        return escalationAfterMins;
    }
    
    public void setEscalationAfterMins(Integer escalationAfterMins) {
        this.escalationAfterMins = escalationAfterMins;
    }
    
    public Integer getDueInMins() {
        return dueInMins;
    }
    
    public void setDueInMins(Integer dueInMins) {
        this.dueInMins = dueInMins;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    public Long getCalendarId() {
        return calendarId;
    }
    
    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    public List<WorkflowConfigRoleDto> getWorkflowRoles() {
        return workflowRoles;
    }
    
    public void setWorkflowRoles(List<WorkflowConfigRoleDto> workflowRoles) {
        this.workflowRoles = workflowRoles;
    }
    
    public List<WorkflowConfigTaskDto> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<WorkflowConfigTaskDto> tasks) {
        this.tasks = tasks;
    }
    
    public List<WorkflowConfigParamDto> getParameters() {
        return parameters;
    }
    
    public void setParameters(List<WorkflowConfigParamDto> parameters) {
        this.parameters = parameters;
    }
    
    public String getTriggerType() {
        return triggerType;
    }
    
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }
    
    public List<WorkflowRoleDto> getRoles() {
        return roles;
    }
    
    public void setRoles(List<WorkflowRoleDto> roles) {
        this.roles = roles;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigDto{" +
                "workflowId=" + workflowId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive='" + isActive + '\'' +
                ", triggerType='" + triggerType + '\'' +
                '}';
    }
}
