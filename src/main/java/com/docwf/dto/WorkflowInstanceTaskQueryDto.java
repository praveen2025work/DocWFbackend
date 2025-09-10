package com.docwf.dto;

import com.docwf.entity.WorkflowInstanceTaskQuery.QueryStatus;
import java.time.LocalDateTime;

public class WorkflowInstanceTaskQueryDto {
    
    private Long queryId;
    
    private Long instanceTaskId;
    
    private String queryTitle;
    
    private String queryDescription;
    
    private Long raisedByUserId;
    
    private String raisedByUsername;
    
    private Long assignedToUserId;
    
    private String assignedToUsername;
    
    private QueryStatus queryStatus;
    
    private String priority;
    
    private String resolutionNotes;
    
    private Long resolvedByUserId;
    
    private String resolvedByUsername;
    
    private LocalDateTime resolvedAt;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    private String updatedBy;
    
    private LocalDateTime updatedAt;
    
    // Related data
    private String taskName;
    private String workflowName;
    private Long instanceId;
    
    // Constructors
    public WorkflowInstanceTaskQueryDto() {}
    
    public WorkflowInstanceTaskQueryDto(String queryTitle, String queryDescription, 
                                      Long raisedByUserId, Long assignedToUserId, 
                                      String createdBy) {
        this.queryTitle = queryTitle;
        this.queryDescription = queryDescription;
        this.raisedByUserId = raisedByUserId;
        this.assignedToUserId = assignedToUserId;
        this.createdBy = createdBy;
        this.queryStatus = QueryStatus.OPEN;
        this.priority = "MEDIUM";
    }
    
    // Getters and Setters
    public Long getQueryId() {
        return queryId;
    }
    
    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }
    
    public Long getInstanceTaskId() {
        return instanceTaskId;
    }
    
    public void setInstanceTaskId(Long instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }
    
    public String getQueryTitle() {
        return queryTitle;
    }
    
    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }
    
    public String getQueryDescription() {
        return queryDescription;
    }
    
    public void setQueryDescription(String queryDescription) {
        this.queryDescription = queryDescription;
    }
    
    public Long getRaisedByUserId() {
        return raisedByUserId;
    }
    
    public void setRaisedByUserId(Long raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
    }
    
    public String getRaisedByUsername() {
        return raisedByUsername;
    }
    
    public void setRaisedByUsername(String raisedByUsername) {
        this.raisedByUsername = raisedByUsername;
    }
    
    public Long getAssignedToUserId() {
        return assignedToUserId;
    }
    
    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
    
    public String getAssignedToUsername() {
        return assignedToUsername;
    }
    
    public void setAssignedToUsername(String assignedToUsername) {
        this.assignedToUsername = assignedToUsername;
    }
    
    public QueryStatus getQueryStatus() {
        return queryStatus;
    }
    
    public void setQueryStatus(QueryStatus queryStatus) {
        this.queryStatus = queryStatus;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getResolutionNotes() {
        return resolutionNotes;
    }
    
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
    
    public Long getResolvedByUserId() {
        return resolvedByUserId;
    }
    
    public void setResolvedByUserId(Long resolvedByUserId) {
        this.resolvedByUserId = resolvedByUserId;
    }
    
    public String getResolvedByUsername() {
        return resolvedByUsername;
    }
    
    public void setResolvedByUsername(String resolvedByUsername) {
        this.resolvedByUsername = resolvedByUsername;
    }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
    
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
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
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    // Helper methods
    public boolean isOpen() {
        return QueryStatus.OPEN.equals(queryStatus);
    }
    
    public boolean isInProgress() {
        return QueryStatus.IN_PROGRESS.equals(queryStatus);
    }
    
    public boolean isResolved() {
        return QueryStatus.RESOLVED.equals(queryStatus);
    }
    
    public boolean isClosed() {
        return QueryStatus.CLOSED.equals(queryStatus);
    }
    
    public boolean isHighPriority() {
        return "HIGH".equals(priority) || "URGENT".equals(priority);
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceTaskQueryDto{" +
                "queryId=" + queryId +
                ", queryTitle='" + queryTitle + '\'' +
                ", queryStatus=" + queryStatus +
                ", priority='" + priority + '\'' +
                ", raisedByUserId=" + raisedByUserId +
                ", assignedToUserId=" + assignedToUserId +
                '}';
    }
}
