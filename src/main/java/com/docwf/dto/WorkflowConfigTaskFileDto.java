package com.docwf.dto;

import com.docwf.entity.WorkflowConfigTaskFile.ActionType;
import java.time.LocalDateTime;

public class WorkflowConfigTaskFileDto {
    
    private Long fileId;
    
    private Long taskId;
    
    private String fileName;
    
    private String filePath;
    
    private Integer fileVersion;
    
    private ActionType actionType;
    
    private Long parentFileId;
    
    private String consolidatedFlag;
    
    private String decisionOutcome;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    // Related data
    private String parentFileName;
    
    // Constructors
    public WorkflowConfigTaskFileDto() {}
    
    public WorkflowConfigTaskFileDto(String fileName, String filePath, ActionType actionType, String createdBy) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.actionType = actionType;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getFileId() {
        return fileId;
    }
    
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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
    
    public Long getParentFileId() {
        return parentFileId;
    }
    
    public void setParentFileId(Long parentFileId) {
        this.parentFileId = parentFileId;
    }
    
    public String getConsolidatedFlag() {
        return consolidatedFlag;
    }
    
    public void setConsolidatedFlag(String consolidatedFlag) {
        this.consolidatedFlag = consolidatedFlag;
    }
    
    public String getDecisionOutcome() {
        return decisionOutcome;
    }
    
    public void setDecisionOutcome(String decisionOutcome) {
        this.decisionOutcome = decisionOutcome;
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
    
    public String getParentFileName() {
        return parentFileName;
    }
    
    public void setParentFileName(String parentFileName) {
        this.parentFileName = parentFileName;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskFileDto{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", actionType=" + actionType +
                ", fileVersion=" + fileVersion +
                '}';
    }
}
