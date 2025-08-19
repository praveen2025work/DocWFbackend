package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_CONFIG_PARAM")
@Audited
public class WorkflowConfigParam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CONFIG_PARAM")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CONFIG_PARAM", sequenceName = "SEQ_WORKFLOW_CONFIG_PARAM", allocationSize = 1)
    @Column(name = "PARAM_ID")
    private Long paramId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKFLOW_ID", nullable = false)
    private WorkflowConfig workflow;
    
    @NotBlank
    @Column(name = "PARAM_KEY", length = 100, nullable = false)
    private String paramKey;
    
    @Column(name = "PARAM_VALUE", length = 1000)
    private String paramValue;
    
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowConfigParam() {}
    
    public WorkflowConfigParam(String paramKey, String paramValue, String createdBy) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getParamId() {
        return paramId;
    }
    
    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }
    
    public WorkflowConfig getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(WorkflowConfig workflow) {
        this.workflow = workflow;
    }
    
    public String getParamKey() {
        return paramKey;
    }
    
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
    
    public String getParamValue() {
        return paramValue;
    }
    
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
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
    
    @Override
    public String toString() {
        return "WorkflowConfigParam{" +
                "paramId=" + paramId +
                ", paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", workflowId=" + (workflow != null ? workflow.getWorkflowId() : null) +
                '}';
    }
}
