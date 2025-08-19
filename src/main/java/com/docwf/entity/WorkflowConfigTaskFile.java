package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
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
    
    @Column(name = "FILE_NAME", length = 500)
    private String fileName;
    
    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;
    
    @Column(name = "FILE_VERSION")
    private Integer fileVersion = 1;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", length = 50)
    private ActionType actionType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_FILE_ID")
    private WorkflowConfigTaskFile parentFile;
    
    @Column(name = "CONSOLIDATED_FLAG", length = 1)
    private String consolidatedFlag = "N";
    
    @Column(name = "DECISION_OUTCOME", length = 255)
    private String decisionOutcome;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    // Enums
    public enum ActionType {
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        FILE_UPDATE,
        CONSOLIDATE_FILES,
        DECISION
    }
    
    // Constructors
    public WorkflowConfigTaskFile() {}
    
    public WorkflowConfigTaskFile(String fileName, String filePath, ActionType actionType, String createdBy) {
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
    
    public WorkflowConfigTaskFile getParentFile() {
        return parentFile;
    }
    
    public void setParentFile(WorkflowConfigTaskFile parentFile) {
        this.parentFile = parentFile;
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
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskFile{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", actionType=" + actionType +
                ", fileVersion=" + fileVersion +
                '}';
    }
}
