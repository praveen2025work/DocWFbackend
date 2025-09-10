package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTaskQueryDto {
    
    @NotNull(message = "Instance task ID is required")
    private Long instanceTaskId;
    
    @NotBlank(message = "Query title is required")
    private String queryTitle;
    
    private String queryDescription;
    
    @NotNull(message = "Raised by user ID is required")
    private Long raisedByUserId;
    
    @NotNull(message = "Assigned to user ID is required")
    private Long assignedToUserId;
    
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT
    
    private String createdBy;
    
    // Constructors
    public CreateTaskQueryDto() {}
    
    public CreateTaskQueryDto(Long instanceTaskId, String queryTitle, String queryDescription,
                            Long raisedByUserId, Long assignedToUserId, String createdBy) {
        this.instanceTaskId = instanceTaskId;
        this.queryTitle = queryTitle;
        this.queryDescription = queryDescription;
        this.raisedByUserId = raisedByUserId;
        this.assignedToUserId = assignedToUserId;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
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
    
    public Long getAssignedToUserId() {
        return assignedToUserId;
    }
    
    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    public String toString() {
        return "CreateTaskQueryDto{" +
                "instanceTaskId=" + instanceTaskId +
                ", queryTitle='" + queryTitle + '\'' +
                ", raisedByUserId=" + raisedByUserId +
                ", assignedToUserId=" + assignedToUserId +
                ", priority='" + priority + '\'' +
                '}';
    }
}
