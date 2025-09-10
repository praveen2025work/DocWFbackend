package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowRoleDto {
    
    @NotNull(message = "Role sequence is required")
    private Integer roleSequence;
    
    @NotBlank(message = "Role name is required")
    private String roleName;
    
    private String roleDescription;
    
    private List<String> users;
    
    private String createdBy;
    
    private Long roleId;
    private String isActive;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
    
    // Constructors
    public WorkflowRoleDto() {}
    
    public WorkflowRoleDto(Integer roleSequence, String roleName) {
        this.roleSequence = roleSequence;
        this.roleName = roleName;
    }
    
    // Getters and Setters
    public Integer getRoleSequence() {
        return roleSequence;
    }
    
    public void setRoleSequence(Integer roleSequence) {
        this.roleSequence = roleSequence;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleDescription() {
        return roleDescription;
    }
    
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    
    public List<String> getUsers() {
        return users;
    }
    
    public void setUsers(List<String> users) {
        this.users = users;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
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
                "roleSequence=" + roleSequence +
                ", roleName='" + roleName + '\'' +
                ", users=" + (users != null ? users.size() : 0) +
                '}';
    }
}