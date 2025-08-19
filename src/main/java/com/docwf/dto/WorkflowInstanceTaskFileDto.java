package com.docwf.dto;

import com.docwf.entity.WorkflowInstanceTaskFile.ActionType;
import java.time.LocalDateTime;

public class WorkflowInstanceTaskFileDto {
    
    private Long instanceFileId;
    
    private Long instanceTaskId;
    
    private String fileName;
    
    private String filePath;
    
    private Integer fileVersion;
    
    private ActionType actionType;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public WorkflowInstanceTaskFileDto() {}
    
    public WorkflowInstanceTaskFileDto(String fileName, String filePath, ActionType actionType, String createdBy) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.actionType = actionType;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getInstanceFileId() {
        return instanceFileId;
    }
    
    public void setInstanceFileId(Long instanceFileId) {
        this.instanceFileId = instanceFileId;
    }
    
    public Long getInstanceTaskId() {
        return instanceTaskId;
    }
    
    public void setInstanceTaskId(Long instanceTaskId) {
        this.instanceTaskId = instanceTaskId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Integer getFileVersion() {
        return fileVersion;
    }
    
    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
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
    
    @Override
    public String toString() {
        return "WorkflowInstanceTaskFileDto{" +
                "instanceFileId=" + instanceFileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", actionType=" + actionType +
                ", fileVersion=" + fileVersion +
                '}';
    }
}
