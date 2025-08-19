package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "WORKFLOW_INSTANCE_ROLE")
@Audited
public class WorkflowInstanceRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_INSTANCE_ROLE")
    @SequenceGenerator(name = "SEQ_WORKFLOW_INSTANCE_ROLE", sequenceName = "SEQ_WORKFLOW_INSTANCE_ROLE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTANCE_ID", nullable = false)
    private WorkflowInstance workflowInstance;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private WorkflowRole role;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private WorkflowUser user;
    
    @NotNull
    @Column(name = "IS_ACTIVE", length = 1, nullable = false)
    private String isActive = "Y";
    
    // Constructors
    public WorkflowInstanceRole() {}
    
    public WorkflowInstanceRole(WorkflowInstance workflowInstance, WorkflowRole role, WorkflowUser user) {
        this.workflowInstance = workflowInstance;
        this.role = role;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }
    
    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }
    
    public WorkflowRole getRole() {
        return role;
    }
    
    public void setRole(WorkflowRole role) {
        this.role = role;
    }
    
    public WorkflowUser getUser() {
        return user;
    }
    
    public void setUser(WorkflowUser user) {
        this.user = user;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "WorkflowInstanceRole{" +
                "id=" + id +
                ", instanceId=" + (workflowInstance != null ? workflowInstance.getInstanceId() : null) +
                ", roleId=" + (role != null ? role.getRoleId() : null) +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
