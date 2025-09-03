package com.docwf.dto;

import com.docwf.entity.WorkflowConfigTaskFile.ActionType;
import java.time.LocalDateTime;

public class WorkflowConfigTaskFileDto {
    
    private Long fileId;
    
    private Long taskId;
    
    private Long parentFileId;  // Reference to parent file for UPDATE/CONSOLIDATE actions
    
    private String fileName;
    
    private String filePath;
    
    private String fileLocation;  // Physical storage location
    
    private String fileTypeRegex;  // File type pattern (e.g., "*.*", "*.xls", "*.pdf")
    
    private ActionType actionType;
    
    private String fileDescription;
    
    private String isRequired;  // Y/N
    
    private String fileStatus;  // PENDING, IN_PROGRESS, COMPLETED, REJECTED
    
    private String keepFileVersions;  // Y/N
    
    private String retainForCurrentPeriod;  // Y/N
    
    private String fileCommentary;  // Additional comments about the file
    
    private String createdBy;
    
    private LocalDateTime createdOn;
    
    private String updatedBy;
    
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowConfigTaskFileDto() {}
    
    public WorkflowConfigTaskFileDto(String fileName, String filePath, ActionType actionType, String createdBy) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.actionType = actionType;
        this.createdBy = createdBy;
    }
    
    public WorkflowConfigTaskFileDto(String fileName, String filePath, String fileLocation, ActionType actionType, String createdBy) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLocation = fileLocation;
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
    
    public String getFileLocation() {
        return fileLocation;
    }
    
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    
    public String getFileTypeRegex() {
        return fileTypeRegex;
    }
    
    public void setFileTypeRegex(String fileTypeRegex) {
        this.fileTypeRegex = fileTypeRegex;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
    
    public String getFileDescription() {
        return fileDescription;
    }
    
    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }
    
    public String getIsRequired() {
        return isRequired;
    }
    
    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }
    
    public String getFileStatus() {
        return fileStatus;
    }
    
    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
    
    public String getKeepFileVersions() {
        return keepFileVersions;
    }
    
    public void setKeepFileVersions(String keepFileVersions) {
        this.keepFileVersions = keepFileVersions;
    }
    
    public String getRetainForCurrentPeriod() {
        return retainForCurrentPeriod;
    }
    
    public void setRetainForCurrentPeriod(String retainForCurrentPeriod) {
        this.retainForCurrentPeriod = retainForCurrentPeriod;
    }
    
    public String getFileCommentary() {
        return fileCommentary;
    }
    
    public void setFileCommentary(String fileCommentary) {
        this.fileCommentary = fileCommentary;
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
    
    // Helper methods
    public boolean isRequired() {
        return "Y".equals(isRequired);
    }
    
    public boolean keepFileVersions() {
        return "Y".equals(keepFileVersions);
    }
    
    public boolean retainForCurrentPeriod() {
        return "Y".equals(retainForCurrentPeriod);
    }
    
    public boolean isPending() {
        return "PENDING".equals(fileStatus);
    }
    
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(fileStatus);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(fileStatus);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(fileStatus);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskFileDto{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", fileTypeRegex='" + fileTypeRegex + '\'' +
                ", actionType=" + actionType +
                ", fileStatus='" + fileStatus + '\'' +
                ", isRequired='" + isRequired + '\'' +
                '}';
    }
}
