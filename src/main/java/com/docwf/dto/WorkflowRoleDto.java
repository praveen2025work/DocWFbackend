package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WorkflowRoleDto {
    
    private Long roleId;
    
    @NotBlank(message = "Role name is required")
    private String roleName;
    
    @NotNull(message = "Active status is required")
    private String isActive;
    
    private String createdBy;
    
    private LocalDateTime createdOn;
    
    private String updatedBy;
    
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowRoleDto() {}
    
    public WorkflowRoleDto(String roleName, String createdBy) {
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
        return "WorkflowRoleDto{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
