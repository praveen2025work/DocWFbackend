package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_INSTANCE_TASK_FILE")
@IdClass(WorkflowInstanceTaskFileId.class)
@Audited
public class WorkflowInstanceTaskFile {
    
    @Id
    @Column(name = "INSTANCE_FILE_ID")
    private Long instanceFileId;
    
    @Id
    @Column(name = "VERSION")
    private Integer version = 1;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_TASK_ID", nullable = false)
    private WorkflowInstanceTask instanceTask;
    
    @Column(name = "FILE_NAME", length = 500)
    private String fileName;
    
    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;
    
    @Column(name = "FILE_LOCATION", length = 1000)
    private String fileLocation;
    
    @Column(name = "FILE_TYPE_REGEX", length = 100)
    private String fileTypeRegex;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", length = 50)
    private ActionType actionType;
    
    @Column(name = "FILE_DESCRIPTION", length = 500)
    private String fileDescription;
    
    @Column(name = "IS_REQUIRED", length = 1)
    private String isRequired = "N";
    
    @Column(name = "FILE_STATUS", length = 50)
    private String fileStatus = "PENDING";
    
    @Column(name = "KEEP_FILE_VERSIONS", length = 1)
    private String keepFileVersions = "Y";
    
    @Column(name = "RETAIN_FOR_CURRENT_PERIOD", length = 1)
    private String retainForCurrentPeriod = "Y";
    
    @Column(name = "FILE_COMMENTARY", length = 1000)
    private String fileCommentary;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    // Enums
    public enum ActionType {
        UPLOAD,
        UPDATE,
        CONSOLIDATE
    }
    
    // Constructors
    public WorkflowInstanceTaskFile() {}
    
    public WorkflowInstanceTaskFile(String fileName, String filePath, ActionType actionType, String createdBy) {
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
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public WorkflowInstanceTask getInstanceTask() {
        return instanceTask;
    }
    
    public void setInstanceTask(WorkflowInstanceTask instanceTask) {
        this.instanceTask = instanceTask;
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
    
    @Override
    public String toString() {
        return "WorkflowInstanceTaskFile{" +
                "instanceFileId=" + instanceFileId +
                ", version=" + version +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", actionType=" + actionType +
                '}';
    }
}
