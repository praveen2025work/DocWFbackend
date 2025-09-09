package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class TaskDecisionOutcomeDto {
    
    private Long outcomeId;
    
    private Long taskId;
    
    @NotBlank(message = "Outcome name is required")
    private String outcomeName;
    
    private Long nextTaskId;
    
    private Long priorTaskId;
    
    private Long targetTaskId;  // Task to go to after this decision
    
    private String revisionType = "SINGLE";  // SINGLE, CASCADE, SELECTIVE
    
    private String revisionTaskIds;  // Comma-separated list of tasks to open for revision
    
    private String revisionStrategy = "REPLACE";  // REPLACE, ADD, MERGE
    
    private Integer revisionPriority = 1;  // Priority of this revision path
    
    private String revisionConditions;  // JSON string for additional conditions
    
    private String autoEscalate = "N";  // Y/N - whether to auto-escalate on this path
    
    private Long escalationRoleId;  // Role to escalate to on this path
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    // For sequence-based mapping during creation
    private Integer taskSequence;
    private Integer targetTaskSequence;
    private List<Integer> revisionTaskSequences;
    
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
    
    public Long getTargetTaskId() {
        return targetTaskId;
    }
    
    public void setTargetTaskId(Long targetTaskId) {
        this.targetTaskId = targetTaskId;
    }
    
    public String getRevisionType() {
        return revisionType;
    }
    
    public void setRevisionType(String revisionType) {
        this.revisionType = revisionType;
    }
    
    public String getRevisionTaskIds() {
        return revisionTaskIds;
    }
    
    public void setRevisionTaskIds(String revisionTaskIds) {
        this.revisionTaskIds = revisionTaskIds;
    }
    
    public String getRevisionStrategy() {
        return revisionStrategy;
    }
    
    public void setRevisionStrategy(String revisionStrategy) {
        this.revisionStrategy = revisionStrategy;
    }
    
    public Integer getRevisionPriority() {
        return revisionPriority;
    }
    
    public void setRevisionPriority(Integer revisionPriority) {
        this.revisionPriority = revisionPriority;
    }
    
    public String getRevisionConditions() {
        return revisionConditions;
    }
    
    public void setRevisionConditions(String revisionConditions) {
        this.revisionConditions = revisionConditions;
    }
    
    public String getAutoEscalate() {
        return autoEscalate;
    }
    
    public void setAutoEscalate(String autoEscalate) {
        this.autoEscalate = autoEscalate;
    }
    
    public Long getEscalationRoleId() {
        return escalationRoleId;
    }
    
    public void setEscalationRoleId(Long escalationRoleId) {
        this.escalationRoleId = escalationRoleId;
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
    
    public Integer getTaskSequence() {
        return taskSequence;
    }
    
    public void setTaskSequence(Integer taskSequence) {
        this.taskSequence = taskSequence;
    }
    
    public Integer getTargetTaskSequence() {
        return targetTaskSequence;
    }
    
    public void setTargetTaskSequence(Integer targetTaskSequence) {
        this.targetTaskSequence = targetTaskSequence;
    }
    
    public List<Integer> getRevisionTaskSequences() {
        return revisionTaskSequences;
    }
    
    public void setRevisionTaskSequences(List<Integer> revisionTaskSequences) {
        this.revisionTaskSequences = revisionTaskSequences;
    }
    
    // Helper methods
    public boolean isSingleRevision() {
        return "SINGLE".equals(revisionType);
    }
    
    public boolean isCascadeRevision() {
        return "CASCADE".equals(revisionType);
    }
    
    public boolean isSelectiveRevision() {
        return "SELECTIVE".equals(revisionType);
    }
    
    public boolean isReplaceStrategy() {
        return "REPLACE".equals(revisionStrategy);
    }
    
    public boolean isAddStrategy() {
        return "ADD".equals(revisionStrategy);
    }
    
    public boolean isMergeStrategy() {
        return "MERGE".equals(revisionStrategy);
    }
    
    public boolean shouldAutoEscalate() {
        return "Y".equals(autoEscalate);
    }
    
    @Override
    public String toString() {
        return "TaskDecisionOutcomeDto{" +
                "outcomeId=" + outcomeId +
                ", outcomeName='" + outcomeName + '\'' +
                ", taskId=" + taskId +
                ", nextTaskId=" + nextTaskId +
                ", targetTaskId=" + targetTaskId +
                '}';
    }
}