package com.docwf.dto;

import jakarta.validation.constraints.NotNull;

public class WorkflowConfigRoleDto {
    
    private Long id;
    
    @NotNull(message = "Workflow ID is required")
    private Long workflowId;
    
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Active status is required")
    private String isActive;
    
    // Related data
    private String roleName;
    private String userName;
    
    // Constructors
    public WorkflowConfigRoleDto() {}
    
    public WorkflowConfigRoleDto(Long workflowId, Long roleId, Long userId, String isActive) {
        this.workflowId = workflowId;
        this.roleId = roleId;
        this.userId = userId;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigRoleDto{" +
                "id=" + id +
                ", workflowId=" + workflowId +
                ", roleId=" + roleId +
                ", userId=" + userId +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
