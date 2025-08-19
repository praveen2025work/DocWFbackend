package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "WORKFLOW_ROLE")
@Audited
public class WorkflowRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_ROLE")
    @SequenceGenerator(name = "SEQ_WORKFLOW_ROLE", sequenceName = "SEQ_WORKFLOW_ROLE", allocationSize = 1)
    @Column(name = "ROLE_ID")
    private Long roleId;
    
    @NotBlank
    @Column(name = "ROLE_NAME", length = 100, unique = true, nullable = false)
    private String roleName;
    
    @NotNull
    @Column(name = "IS_ACTIVE", length = 1, nullable = false)
    private String isActive = "Y";
    
    @NotBlank
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
    
    // Constructors
    public WorkflowRole() {}
    
    public WorkflowRole(String roleName, String createdBy) {
        this.roleName = roleName;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
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
        return "WorkflowRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
