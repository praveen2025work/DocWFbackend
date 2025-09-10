package com.docwf.entity;

import java.io.Serializable;
import java.util.Objects;

public class WorkflowInstanceTaskFileId implements Serializable {
    
    private Long instanceFileId;
    private Integer version;
    
    // Default constructor
    public WorkflowInstanceTaskFileId() {}
    
    // Constructor with parameters
    public WorkflowInstanceTaskFileId(Long instanceFileId, Integer version) {
        this.instanceFileId = instanceFileId;
        this.version = version;
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
    
    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowInstanceTaskFileId that = (WorkflowInstanceTaskFileId) o;
        return Objects.equals(instanceFileId, that.instanceFileId) &&
               Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(instanceFileId, version);
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceTaskFileId{" +
                "instanceFileId=" + instanceFileId +
                ", version=" + version +
                '}';
    }
}
