package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_INSTANCE_TASK_QUERY")
@Audited
public class WorkflowInstanceTaskQuery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_INSTANCE_TASK_QUERY")
    @SequenceGenerator(name = "SEQ_WORKFLOW_INSTANCE_TASK_QUERY", sequenceName = "SEQ_WORKFLOW_INSTANCE_TASK_QUERY", allocationSize = 1)
    @Column(name = "QUERY_ID")
    private Long queryId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_TASK_ID", nullable = false)
    private WorkflowInstanceTask instanceTask;
    
    @Column(name = "QUERY_TITLE", length = 500, nullable = false)
    private String queryTitle;
    
    @Column(name = "QUERY_DESCRIPTION", length = 2000)
    private String queryDescription;
    
    @NotNull
    @Column(name = "RAISED_BY_USER_ID", nullable = false)
    private Long raisedByUserId;
    
    @NotNull
    @Column(name = "ASSIGNED_TO_USER_ID", nullable = false)
    private Long assignedToUserId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "QUERY_STATUS", length = 50, nullable = false)
    private QueryStatus queryStatus = QueryStatus.OPEN;
    
    @Column(name = "PRIORITY", length = 20)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT
    
    @Column(name = "RESOLUTION_NOTES", length = 2000)
    private String resolutionNotes;
    
    @Column(name = "RESOLVED_BY_USER_ID")
    private Long resolvedByUserId;
    
    @Column(name = "RESOLVED_AT")
    private LocalDateTime resolvedAt;
    
    @Column(name = "CREATED_BY", length = 100, nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    // Enums
    public enum QueryStatus {
        OPEN,           // Query is open and waiting for response
        IN_PROGRESS,    // Query is being worked on
        RESOLVED,       // Query has been resolved
        CLOSED          // Query is closed
    }
    
    // Constructors
    public WorkflowInstanceTaskQuery() {}
    
    public WorkflowInstanceTaskQuery(WorkflowInstanceTask instanceTask, String queryTitle, 
                                   String queryDescription, Long raisedByUserId, 
                                   Long assignedToUserId, String createdBy) {
        this.instanceTask = instanceTask;
        this.queryTitle = queryTitle;
        this.queryDescription = queryDescription;
        this.raisedByUserId = raisedByUserId;
        this.assignedToUserId = assignedToUserId;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getQueryId() {
        return queryId;
    }
    
    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }
    
    public WorkflowInstanceTask getInstanceTask() {
        return instanceTask;
    }
    
    public void setInstanceTask(WorkflowInstanceTask instanceTask) {
        this.instanceTask = instanceTask;
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
    
    public Long getAssignedToUserId() {
        return assignedToUserId;
    }
    
    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
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
        return "WorkflowInstanceTaskQuery{" +
                "queryId=" + queryId +
                ", queryTitle='" + queryTitle + '\'' +
                ", queryStatus=" + queryStatus +
                ", priority='" + priority + '\'' +
                ", raisedByUserId=" + raisedByUserId +
                ", assignedToUserId=" + assignedToUserId +
                '}';
    }
}
