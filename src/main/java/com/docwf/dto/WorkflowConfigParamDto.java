package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class WorkflowConfigParamDto {
    
    private Long paramId;
    
    private Long workflowId;
    
    @NotBlank(message = "Parameter key is required")
    private String paramKey;
    
    private String paramValue;
    
    private String createdBy;
    
    private LocalDateTime createdOn;
    
    private String updatedBy;
    
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowConfigParamDto() {}
    
    public WorkflowConfigParamDto(String paramKey, String paramValue, String createdBy) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getParamId() {
        return paramId;
    }
    
    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }
    
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getParamKey() {
        return paramKey;
    }
    
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
    
    public String getParamValue() {
        return paramValue;
    }
    
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigParamDto{" +
                "paramId=" + paramId +
                ", paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", workflowId=" + workflowId +
                '}';
    }
}
