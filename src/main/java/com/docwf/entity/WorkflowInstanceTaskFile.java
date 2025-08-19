package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_INSTANCE_TASK_FILE")
@Audited
public class WorkflowInstanceTaskFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_INSTANCE_TASK_FILE")
    @SequenceGenerator(name = "SEQ_WORKFLOW_INSTANCE_TASK_FILE", sequenceName = "SEQ_WORKFLOW_INSTANCE_TASK_FILE", allocationSize = 1)
    @Column(name = "INSTANCE_FILE_ID")
    private Long instanceFileId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_TASK_ID", nullable = false)
    private WorkflowInstanceTask instanceTask;
    
    @Column(name = "FILE_NAME", length = 500)
    private String fileName;
    
    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;
    
    @Column(name = "FILE_VERSION")
    private Integer fileVersion = 1;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", length = 50)
    private ActionType actionType;
    
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
        return "WorkflowInstanceTaskFile{" +
                "instanceFileId=" + instanceFileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", actionType=" + actionType +
                ", fileVersion=" + fileVersion +
                '}';
    }
}
