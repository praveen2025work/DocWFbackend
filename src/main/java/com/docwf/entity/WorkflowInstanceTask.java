package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKFLOW_INSTANCE_TASK")
@Audited
public class WorkflowInstanceTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_INSTANCE_TASK")
    @SequenceGenerator(name = "SEQ_WORKFLOW_INSTANCE_TASK", sequenceName = "SEQ_WORKFLOW_INSTANCE_TASK", allocationSize = 1)
    @Column(name = "INSTANCE_TASK_ID")
    private Long instanceTaskId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_ID", nullable = false)
    private WorkflowInstance workflowInstance;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID", nullable = false)
    private WorkflowConfigTask task;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50)
    private TaskInstanceStatus status = TaskInstanceStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private WorkflowUser assignedTo;
    
    @CreationTimestamp
    @Column(name = "STARTED_ON")
    private LocalDateTime startedOn;
    
    @Column(name = "COMPLETED_ON")
    private LocalDateTime completedOn;
    
    @Column(name = "DECISION_OUTCOME", length = 255)
    private String decisionOutcome;
    
    // Relationships
    @OneToMany(mappedBy = "instanceTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowInstanceTaskFile> instanceTaskFiles = new ArrayList<>();
    
    @OneToMany(mappedBy = "instanceTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInstanceDecisionOutcome> decisionOutcomes = new ArrayList<>();
    
    // Enums
    public enum TaskInstanceStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        ESCALATED
    }
    
    // Constructors
    public WorkflowInstanceTask() {}
    
    public WorkflowInstanceTask(WorkflowInstance workflowInstance, WorkflowConfigTask task) {
        this.workflowInstance = workflowInstance;
        this.task = task;
    }
    
    // Getters and Setters
    public Long getInstanceTaskId() {
        return instanceTaskId;
    }
    
    public void setInstanceTaskId(Long instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }
    
    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }
    
    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }
    
    public WorkflowConfigTask getTask() {
        return task;
    }
    
    public void setTask(WorkflowConfigTask task) {
        this.task = task;
    }
    
    public TaskInstanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskInstanceStatus status) {
        this.status = status;
    }
    
    public WorkflowUser getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(WorkflowUser assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public LocalDateTime getStartedOn() {
        return startedOn;
    }
    
    public void setStartedOn(LocalDateTime startedOn) {
        this.startedOn = startedOn;
    }
    
    public LocalDateTime getCompletedOn() {
        return completedOn;
    }
    
    public void setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
    }
    
    public String getDecisionOutcome() {
        return decisionOutcome;
    }
    
    public void setDecisionOutcome(String decisionOutcome) {
        this.decisionOutcome = decisionOutcome;
    }
    
    public List<WorkflowInstanceTaskFile> getInstanceTaskFiles() {
        return instanceTaskFiles;
    }
    
    public void setInstanceTaskFiles(List<WorkflowInstanceTaskFile> instanceTaskFiles) {
        this.instanceTaskFiles = instanceTaskFiles;
    }
    
    public List<TaskInstanceDecisionOutcome> getDecisionOutcomes() {
        return decisionOutcomes;
    }
    
    public void setDecisionOutcomes(List<TaskInstanceDecisionOutcome> decisionOutcomes) {
        this.decisionOutcomes = decisionOutcomes;
    }
    
    // Helper methods
    public void addInstanceTaskFile(WorkflowInstanceTaskFile instanceTaskFile) {
        instanceTaskFiles.add(instanceTaskFile);
        instanceTaskFile.setInstanceTask(this);
    }
    
    public void addDecisionOutcome(TaskInstanceDecisionOutcome decisionOutcome) {
        decisionOutcomes.add(decisionOutcome);
        decisionOutcome.setInstanceTask(this);
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceTask{" +
                "instanceTaskId=" + instanceTaskId +
                ", instanceId=" + (workflowInstance != null ? workflowInstance.getInstanceId() : null) +
                ", taskId=" + (task != null ? task.getTaskId() : null) +
                ", status=" + status +
                ", assignedTo=" + (assignedTo != null ? assignedTo.getUserId() : null) +
                '}';
    }
}
