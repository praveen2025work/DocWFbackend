package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK_INSTANCE_DECISION_OUTCOME")
@Audited
public class TaskInstanceDecisionOutcome {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TASK_INSTANCE_DECISION_OUTCOME")
    @SequenceGenerator(name = "SEQ_TASK_INSTANCE_DECISION_OUTCOME", sequenceName = "SEQ_TASK_INSTANCE_DECISION_OUTCOME", allocationSize = 1)
    @Column(name = "OUTCOME_ID")
    private Long outcomeId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_TASK_ID", nullable = false)
    private WorkflowInstanceTask instanceTask;
    
    @NotBlank
    @Column(name = "OUTCOME_NAME", length = 255, nullable = false)
    private String outcomeName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_INSTANCE_TASK_ID")
    private WorkflowInstanceTask nextInstanceTask;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIOR_INSTANCE_TASK_ID")
    private WorkflowInstanceTask priorInstanceTask;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    // Constructors
    public TaskInstanceDecisionOutcome() {}
    
    public TaskInstanceDecisionOutcome(String outcomeName, String createdBy) {
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
    
    public WorkflowInstanceTask getInstanceTask() {
        return instanceTask;
    }
    
    public void setInstanceTask(WorkflowInstanceTask instanceTask) {
        this.instanceTask = instanceTask;
    }
    
    public String getOutcomeName() {
        return outcomeName;
    }
    
    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
    
    public WorkflowInstanceTask getNextInstanceTask() {
        return nextInstanceTask;
    }
    
    public void setNextInstanceTask(WorkflowInstanceTask nextInstanceTask) {
        this.nextInstanceTask = nextInstanceTask;
    }
    
    public WorkflowInstanceTask getPriorInstanceTask() {
        return priorInstanceTask;
    }
    
    public void setPriorInstanceTask(WorkflowInstanceTask priorInstanceTask) {
        this.priorInstanceTask = priorInstanceTask;
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
        return "TaskInstanceDecisionOutcome{" +
                "outcomeId=" + outcomeId +
                ", outcomeName='" + outcomeName + '\'' +
                ", instanceTaskId=" + (instanceTask != null ? instanceTask.getInstanceTaskId() : null) +
                ", nextInstanceTaskId=" + (nextInstanceTask != null ? nextInstanceTask.getInstanceTaskId() : null) +
                '}';
    }
}
