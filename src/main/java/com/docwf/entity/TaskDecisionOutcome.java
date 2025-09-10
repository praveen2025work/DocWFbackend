package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TASK_DECISION_OUTCOME")
@Audited
public class TaskDecisionOutcome {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TASK_DECISION_OUTCOME")
    @SequenceGenerator(name = "SEQ_TASK_DECISION_OUTCOME", sequenceName = "SEQ_TASK_DECISION_OUTCOME", allocationSize = 1)
    @Column(name = "OUTCOME_ID")
    private Long outcomeId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID", nullable = false)
    private WorkflowConfigTask task;
    
    @NotBlank
    @Column(name = "OUTCOME_NAME", length = 255, nullable = false)
    private String outcomeName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_TASK_ID")
    private WorkflowConfigTask nextTask;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIOR_TASK_ID")
    private WorkflowConfigTask priorTask;
    
    @Column(name = "TARGET_TASK_ID")
    private Long targetTaskId;  // Task to go to after this decision
    
    @Column(name = "REVISION_TYPE", length = 50)
    private String revisionType = "SINGLE";  // SINGLE, CASCADE, SELECTIVE
    
    @Column(name = "REVISION_TASK_IDS")
    private String revisionTaskIds;  // Comma-separated list of tasks to open for revision
    
    @Column(name = "REVISION_STRATEGY", length = 50)
    private String revisionStrategy = "REPLACE";  // REPLACE, ADD, MERGE
    
    @Column(name = "REVISION_PRIORITY")
    private Integer revisionPriority = 1;  // Priority of this revision path
    
    @Column(name = "REVISION_CONDITIONS")
    private String revisionConditions;  // JSON string for additional conditions
    
    @Column(name = "AUTO_ESCALATE", length = 1)
    private String autoEscalate = "N";  // Y/N - whether to auto-escalate on this path
    
    @Column(name = "ESCALATION_ROLE_ID")
    private Long escalationRoleId;  // Role to escalate to on this path
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    // Constructors
    public TaskDecisionOutcome() {}
    
    public TaskDecisionOutcome(String outcomeName, String createdBy) {
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
    
    public WorkflowConfigTask getTask() {
        return task;
    }
    
    public void setTask(WorkflowConfigTask task) {
        this.task = task;
    }
    
    public String getOutcomeName() {
        return outcomeName;
    }
    
    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
    
    public WorkflowConfigTask getNextTask() {
        return nextTask;
    }
    
    public void setNextTask(WorkflowConfigTask nextTask) {
        this.nextTask = nextTask;
    }
    
    public WorkflowConfigTask getPriorTask() {
        return priorTask;
    }
    
    public void setPriorTask(WorkflowConfigTask priorTask) {
        this.priorTask = priorTask;
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
    
    public List<Long> getRevisionTaskIdList() {
        if (revisionTaskIds == null || revisionTaskIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : revisionTaskIds.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
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
    
    @Override
    public String toString() {
        return "TaskDecisionOutcome{" +
                "outcomeId=" + outcomeId +
                ", outcomeName='" + outcomeName + '\'' +
                ", taskId=" + (task != null ? task.getTaskId() : null) +
                ", nextTaskId=" + (nextTask != null ? nextTask.getTaskId() : null) +
                '}';
    }
}
