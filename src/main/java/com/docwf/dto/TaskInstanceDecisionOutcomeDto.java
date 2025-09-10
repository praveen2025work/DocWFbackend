package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TaskInstanceDecisionOutcomeDto {
    
    private Long outcomeId;
    
    private Long instanceTaskId;
    
    @NotBlank(message = "Outcome name is required")
    private String outcomeName;
    
    private String outcomeDescription;
    
    private String nextAction;
    
    private Long nextInstanceTaskId;
    
    private Long priorInstanceTaskId;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    // Related data
    private String nextTaskName;
    private String priorTaskName;
    
    // Constructors
    public TaskInstanceDecisionOutcomeDto() {}
    
    public TaskInstanceDecisionOutcomeDto(String outcomeName, String createdBy) {
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
    
    public Long getInstanceTaskId() {
        return instanceTaskId;
    }
    
    public void setInstanceTaskId(Long instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }
    
    public String getOutcomeName() {
        return outcomeName;
    }
    
    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
    
    public String getOutcomeDescription() {
        return outcomeDescription;
    }
    
    public void setOutcomeDescription(String outcomeDescription) {
        this.outcomeDescription = outcomeDescription;
    }
    
    public String getNextAction() {
        return nextAction;
    }
    
    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }
    
    public Long getNextInstanceTaskId() {
        return nextInstanceTaskId;
    }
    
    public void setNextInstanceTaskId(Long nextInstanceTaskId) {
        this.nextInstanceTaskId = nextInstanceTaskId;
    }
    
    public Long getPriorInstanceTaskId() {
        return priorInstanceTaskId;
    }
    
    public void setPriorInstanceTaskId(Long priorInstanceTaskId) {
        this.priorInstanceTaskId = priorInstanceTaskId;
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
        return "TaskInstanceDecisionOutcomeDto{" +
                "outcomeId=" + outcomeId +
                ", outcomeName='" + outcomeName + '\'' +
                ", instanceTaskId=" + instanceTaskId +
                ", nextInstanceTaskId=" + nextInstanceTaskId +
                '}';
    }
}
