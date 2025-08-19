package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKFLOW_INSTANCE")
@Audited
public class WorkflowInstance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_INSTANCE")
    @SequenceGenerator(name = "SEQ_WORKFLOW_INSTANCE", sequenceName = "SEQ_WORKFLOW_INSTANCE", allocationSize = 1)
    @Column(name = "INSTANCE_ID")
    private Long instanceId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKFLOW_ID", nullable = false)
    private WorkflowConfig workflow;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50)
    private InstanceStatus status = InstanceStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STARTED_BY")
    private WorkflowUser startedBy;
    
    @CreationTimestamp
    @Column(name = "STARTED_ON")
    private LocalDateTime startedOn;
    
    @Column(name = "COMPLETED_ON")
    private LocalDateTime completedOn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCALATED_TO")
    private WorkflowUser escalatedTo;
    
    // Relationships
    @OneToMany(mappedBy = "workflowInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowInstanceRole> instanceRoles = new ArrayList<>();
    
    @OneToMany(mappedBy = "workflowInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowInstanceTask> instanceTasks = new ArrayList<>();
    
    // Enums
    public enum InstanceStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }
    
    // Constructors
    public WorkflowInstance() {}
    
    public WorkflowInstance(WorkflowConfig workflow, WorkflowUser startedBy) {
        this.workflow = workflow;
        this.startedBy = startedBy;
    }
    
    // Getters and Setters
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    public WorkflowConfig getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(WorkflowConfig workflow) {
        this.workflow = workflow;
    }
    
    public InstanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(InstanceStatus status) {
        this.status = status;
    }
    
    public WorkflowUser getStartedBy() {
        return startedBy;
    }
    
    public void setStartedBy(WorkflowUser startedBy) {
        this.startedBy = startedBy;
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
    
    public WorkflowUser getEscalatedTo() {
        return escalatedTo;
    }
    
    public void setEscalatedTo(WorkflowUser escalatedTo) {
        this.escalatedTo = escalatedTo;
    }
    
    public List<WorkflowInstanceRole> getInstanceRoles() {
        return instanceRoles;
    }
    
    public void setInstanceRoles(List<WorkflowInstanceRole> instanceRoles) {
        this.instanceRoles = instanceRoles;
    }
    
    public List<WorkflowInstanceTask> getInstanceTasks() {
        return instanceTasks;
    }
    
    public void setInstanceTasks(List<WorkflowInstanceTask> instanceTasks) {
        this.instanceTasks = instanceTasks;
    }
    
    // Helper methods
    public void addInstanceRole(WorkflowInstanceRole instanceRole) {
        instanceRoles.add(instanceRole);
        instanceRole.setWorkflowInstance(this);
    }
    
    public void addInstanceTask(WorkflowInstanceTask instanceTask) {
        instanceTasks.add(instanceTask);
        instanceTask.setWorkflowInstance(this);
    }
    
    @Override
    public String toString() {
        return "WorkflowInstance{" +
                "instanceId=" + instanceId +
                ", workflowId=" + (workflow != null ? workflow.getWorkflowId() : null) +
                ", status=" + status +
                ", startedBy=" + (startedBy != null ? startedBy.getUserId() : null) +
                '}';
    }
}
