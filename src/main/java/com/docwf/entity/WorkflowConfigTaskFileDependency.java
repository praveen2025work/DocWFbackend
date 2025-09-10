package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_CONFIG_TASK_FILE_DEPENDENCY")
@Audited
public class WorkflowConfigTaskFileDependency {
    
    @Id
    @Column(name = "FILE_ID")
    private Long fileId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", insertable = false, updatable = false)
    private WorkflowConfigTaskFile file;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_FILE_ID", nullable = false)
    private WorkflowConfigTaskFile parentFile;
    
    @Column(name = "CREATED_BY", length = 100, nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    // Constructors
    public WorkflowConfigTaskFileDependency() {}
    
    public WorkflowConfigTaskFileDependency(Long fileId, WorkflowConfigTaskFile parentFile, String createdBy) {
        this.fileId = fileId;
        this.parentFile = parentFile;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getFileId() {
        return fileId;
    }
    
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    
    public WorkflowConfigTaskFile getFile() {
        return file;
    }
    
    public void setFile(WorkflowConfigTaskFile file) {
        this.file = file;
    }
    
    public WorkflowConfigTaskFile getParentFile() {
        return parentFile;
    }
    
    public void setParentFile(WorkflowConfigTaskFile parentFile) {
        this.parentFile = parentFile;
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
    
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskFileDependency{" +
                "fileId=" + fileId +
                ", parentFile=" + (parentFile != null ? parentFile.getFileId() : null) +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
