package com.docwf.dto;

import com.docwf.entity.WorkflowInstance.InstanceStatus;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowInstanceDto {
    
    private Long instanceId;
    
    private Long workflowId;
    
    private InstanceStatus status;
    
    private Long startedBy;
    
    private LocalDateTime startedOn;
    
    private LocalDateTime completedOn;
    
    private Long escalatedTo;
    
    // Related data
    private String workflowName;
    private String startedByUsername;
    private String escalatedToUsername;
    private List<WorkflowInstanceTaskDto> instanceTasks;
    
    // Constructors
    public WorkflowInstanceDto() {}
    
    public WorkflowInstanceDto(Long workflowId, Long startedBy) {
        this.workflowId = workflowId;
        this.startedBy = startedBy;
        this.status = InstanceStatus.PENDING;
    }
    
    // Getters and Setters
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public InstanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(InstanceStatus status) {
        this.status = status;
    }
    
    public Long getStartedBy() {
        return startedBy;
    }
    
    public void setStartedBy(Long startedBy) {
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
    
    public Long getEscalatedTo() {
        return escalatedTo;
    }
    
    public void setEscalatedTo(Long escalatedTo) {
        this.escalatedTo = escalatedTo;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    public String getStartedByUsername() {
        return startedByUsername;
    }
    
    public void setStartedByUsername(String startedByUsername) {
        this.startedByUsername = startedByUsername;
    }
    
    public String getEscalatedToUsername() {
        return escalatedToUsername;
    }
    
    public void setEscalatedToUsername(String escalatedToUsername) {
        this.escalatedToUsername = escalatedToUsername;
    }
    
    public List<WorkflowInstanceTaskDto> getInstanceTasks() {
        return instanceTasks;
    }
    
    public void setInstanceTasks(List<WorkflowInstanceTaskDto> instanceTasks) {
        this.instanceTasks = instanceTasks;
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceDto{" +
                "instanceId=" + instanceId +
                ", workflowId=" + workflowId +
                ", status=" + status +
                ", startedBy=" + startedBy +
                '}';
    }
}
