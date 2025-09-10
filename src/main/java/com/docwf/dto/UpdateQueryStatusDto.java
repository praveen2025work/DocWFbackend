package com.docwf.dto;

import com.docwf.entity.WorkflowInstanceTaskQuery.QueryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateQueryStatusDto {
    
    @NotNull(message = "Query status is required")
    private QueryStatus queryStatus;
    
    private String resolutionNotes;
    
    @NotNull(message = "Updated by user ID is required")
    private Long updatedByUserId;
    
    private String updatedBy;
    
    // Constructors
    public UpdateQueryStatusDto() {}
    
    public UpdateQueryStatusDto(QueryStatus queryStatus, String resolutionNotes, 
                              Long updatedByUserId, String updatedBy) {
        this.queryStatus = queryStatus;
        this.resolutionNotes = resolutionNotes;
        this.updatedByUserId = updatedByUserId;
        this.updatedBy = updatedBy;
    }
    
    // Getters and Setters
    public QueryStatus getQueryStatus() {
        return queryStatus;
    }
    
    public void setQueryStatus(QueryStatus queryStatus) {
        this.queryStatus = queryStatus;
    }
    
    public String getResolutionNotes() {
        return resolutionNotes;
    }
    
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
    
    public Long getUpdatedByUserId() {
        return updatedByUserId;
    }
    
    public void setUpdatedByUserId(Long updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @Override
    public String toString() {
        return "UpdateQueryStatusDto{" +
                "queryStatus=" + queryStatus +
                ", updatedByUserId=" + updatedByUserId +
                '}';
    }
}
