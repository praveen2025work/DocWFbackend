package com.docwf.dto;

import com.docwf.entity.WorkflowInstanceTask.TaskInstanceStatus;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowInstanceTaskDto {
    
    private Long instanceTaskId;
    
    private Long instanceId;
    
    private Long taskId;
    
    private TaskInstanceStatus status;
    
    private Long assignedTo;
    
    private LocalDateTime startedOn;
    
    private LocalDateTime completedOn;
    
    private LocalDateTime rejectedOn;
    
    private String reason;
    
    private String completedBy;
    
    private String rejectedBy;
    
    private String decisionOutcome;
    
    private String parentTaskIds;
    
    // Related data
    private String taskName;
    private String taskType;
    private String assignedToUsername;
    private List<WorkflowInstanceTaskFileDto> instanceTaskFiles;
    private List<TaskInstanceDecisionOutcomeDto> decisionOutcomes;
    
    // Constructors
    public WorkflowInstanceTaskDto() {}
    
    public WorkflowInstanceTaskDto(Long instanceId, Long taskId) {
        this.instanceId = instanceId;
        this.taskId = taskId;
        this.status = TaskInstanceStatus.PENDING;
    }
    
    // Getters and Setters
    public Long getInstanceTaskId() {
        return instanceTaskId;
    }
    
    public void setInstanceTaskId(Long instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }
    
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public TaskInstanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskInstanceStatus status) {
        this.status = status;
    }
    
    public Long getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(Long assignedTo) {
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
    
    public LocalDateTime getRejectedOn() {
        return rejectedOn;
    }
    
    public void setRejectedOn(LocalDateTime rejectedOn) {
        this.rejectedOn = rejectedOn;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getCompletedBy() {
        return completedBy;
    }
    
    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }
    
    public String getRejectedBy() {
        return rejectedBy;
    }
    
    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }
    
    public String getDecisionOutcome() {
        return decisionOutcome;
    }
    
    public void setDecisionOutcome(String decisionOutcome) {
        this.decisionOutcome = decisionOutcome;
    }
    
    public String getParentTaskIds() {
        return parentTaskIds;
    }
    
    public void setParentTaskIds(String parentTaskIds) {
        this.parentTaskIds = parentTaskIds;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getTaskType() {
        return taskType;
    }
    
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    
    public String getAssignedToUsername() {
        return assignedToUsername;
    }
    
    public void setAssignedToUsername(String assignedToUsername) {
        this.assignedToUsername = assignedToUsername;
    }
    
    public List<WorkflowInstanceTaskFileDto> getInstanceTaskFiles() {
        return instanceTaskFiles;
    }
    
    public void setInstanceTaskFiles(List<WorkflowInstanceTaskFileDto> instanceTaskFiles) {
        this.instanceTaskFiles = instanceTaskFiles;
    }
    
    public List<TaskInstanceDecisionOutcomeDto> getDecisionOutcomes() {
        return decisionOutcomes;
    }
    
    public void setDecisionOutcomes(List<TaskInstanceDecisionOutcomeDto> decisionOutcomes) {
        this.decisionOutcomes = decisionOutcomes;
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceTaskDto{" +
                "instanceTaskId=" + instanceTaskId +
                ", instanceId=" + instanceId +
                ", taskId=" + taskId +
                ", status=" + status +
                ", assignedTo=" + assignedTo +
                '}';
    }
}
