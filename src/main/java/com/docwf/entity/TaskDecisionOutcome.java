package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

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
