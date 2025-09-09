package com.docwf.dto;

import jakarta.validation.constraints.NotNull;

public class WorkflowConfigTaskFileDependencyDto {
    
    @NotNull(message = "File ID is required")
    private Long fileId;
    
    @NotNull(message = "Parent file ID is required")
    private Long parentFileId;
    
    private String createdBy;
    
    private String updatedBy;
    
    // For sequence-based mapping during creation
    private Integer fileSequence;
    private Integer parentFileSequence;
    
    // Constructors
    public WorkflowConfigTaskFileDependencyDto() {}
    
    public WorkflowConfigTaskFileDependencyDto(Long fileId, Long parentFileId) {
        this.fileId = fileId;
        this.parentFileId = parentFileId;
    }
    
    public WorkflowConfigTaskFileDependencyDto(Integer fileSequence, Integer parentFileSequence) {
        this.fileSequence = fileSequence;
        this.parentFileSequence = parentFileSequence;
    }
    
    // Getters and Setters
    public Long getFileId() {
        return fileId;
    }
    
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    
    public Long getParentFileId() {
        return parentFileId;
    }
    
    public void setParentFileId(Long parentFileId) {
        this.parentFileId = parentFileId;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Integer getFileSequence() {
        return fileSequence;
    }
    
    public void setFileSequence(Integer fileSequence) {
        this.fileSequence = fileSequence;
    }
    
    public Integer getParentFileSequence() {
        return parentFileSequence;
    }
    
    public void setParentFileSequence(Integer parentFileSequence) {
        this.parentFileSequence = parentFileSequence;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskFileDependencyDto{" +
                "fileId=" + fileId +
                ", parentFileId=" + parentFileId +
                ", fileSequence=" + fileSequence +
                ", parentFileSequence=" + parentFileSequence +
                '}';
    }
}
