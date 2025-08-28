package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_CONFIG_TASK_FILE")
@Audited
public class WorkflowConfigTaskFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CONFIG_TASK_FILE")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CONFIG_TASK_FILE", sequenceName = "SEQ_WORKFLOW_CONFIG_TASK_FILE", allocationSize = 1)
    @Column(name = "FILE_ID")
    private Long fileId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID", nullable = false)
    private WorkflowConfigTask task;
    
    @Column(name = "FILE_NAME", length = 500, nullable = false)
    private String fileName;
    
    @Column(name = "FILE_PATH", length = 1000, nullable = false)
    private String filePath;
    
    @Column(name = "FILE_LOCATION", length = 1000)
    private String fileLocation;  // Physical storage location (e.g., S3 bucket, local directory)
    
    @Column(name = "FILE_TYPE_REGEX", length = 100)
    private String fileTypeRegex;  // File type pattern (e.g., "*.*", "*.xls", "*.pdf")
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", length = 50, nullable = false)
    private ActionType actionType;
    
    @Column(name = "FILE_DESCRIPTION", length = 500)
    private String fileDescription;
    
    @Column(name = "IS_REQUIRED", length = 1)
    private String isRequired = "N";  // Y/N - whether this file is required for task completion
    
    @Column(name = "FILE_STATUS", length = 50)
    private String fileStatus = "PENDING";  // PENDING, IN_PROGRESS, COMPLETED, REJECTED
    
    @Column(name = "KEEP_FILE_VERSIONS", length = 1)
    private String keepFileVersions = "Y";  // Y/N - whether to keep file version history
    
    @Column(name = "RETAIN_FOR_CURRENT_PERIOD", length = 1)
    private String retainForCurrentPeriod = "Y";  // Y/N - whether to retain file for current workflow period
    
    @Column(name = "FILE_COMMENTARY", length = 1000)
    private String fileCommentary;  // Additional comments about the file
    
    @Column(name = "CREATED_BY", length = 100, nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;
    
    // Enums
    public enum ActionType {
        UPLOAD,      // Files that users upload
        UPDATE,      // Files that users modify/update
        CONSOLIDATE  // Files created by combining multiple files
    }
    
    // Constructors
    public WorkflowConfigTaskFile() {}
    
    public WorkflowConfigTaskFile(String fileName, String filePath, ActionType actionType, String createdBy) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.actionType = actionType;
        this.createdBy = createdBy;
    }
    
    public WorkflowConfigTaskFile(String fileName, String filePath, String fileLocation, ActionType actionType, String createdBy) {
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
    
    public WorkflowConfigTask getTask() {
        return task;
    }
    
    public void setTask(WorkflowConfigTask task) {
        this.task = task;
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
        return "WorkflowConfigTaskFile{" +
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
