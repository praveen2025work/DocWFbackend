package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TaskDecisionOutcomeDto {
    
    private Long outcomeId;
    
    private Long taskId;
    
    @NotBlank(message = "Outcome name is required")
    private String outcomeName;
    
    private Long nextTaskId;
    
    private Long priorTaskId;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    // Related data
    private String nextTaskName;
    private String priorTaskName;
    
    // Constructors
    public TaskDecisionOutcomeDto() {}
    
    public TaskDecisionOutcomeDto(String outcomeName, String createdBy) {
        this.outcomeName = outcomeName;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getOutcomeId() {
        return outcomeId;
    }
    
    public void setOutcomeId(Long outcomeId) {
        this.outcomeId = outcomeId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getOutcomeName() {
        return outcomeName;
    }
    
    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
    
    public Long getNextTaskId() {
        return nextTaskId;
    }
    
    public void setNextTaskId(Long nextTaskId) {
        this.nextTaskId = nextTaskId;
    }
    
    public Long getPriorTaskId() {
        return priorTaskId;
    }
    
    public void setPriorTaskId(Long priorTaskId) {
        this.priorTaskId = priorTaskId;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getNextTaskName() {
        return nextTaskName;
    }
    
    public void setNextTaskName(String nextTaskName) {
        this.nextTaskName = nextTaskName;
    }
    
    public String getPriorTaskName() {
        return priorTaskName;
    }
    
    public void setPriorTaskName(String priorTaskName) {
        this.priorTaskName = priorTaskName;
    }
    
    @Override
    public String toString() {
        return "TaskDecisionOutcomeDto{" +
                "outcomeId=" + outcomeId +
                ", outcomeName='" + outcomeName + '\'' +
                ", taskId=" + taskId +
                ", nextTaskId=" + nextTaskId +
                '}';
    }
}
