package com.docwf.dto;

/**
 * Data Transfer Object for User Permissions
 */
public class UserPermissionDto {
    
    private Long permissionId;
    private Long userId;
    private String username;
    private String permissionName;
    private String permissionDescription;
    private String resource; // WORKFLOW, TASK, USER, ROLE, FILE
    private String action; // CREATE, READ, UPDATE, DELETE, EXECUTE
    private String scope; // OWN, TEAM, ALL
    private boolean isActive;
    private String grantedBy;
    private String grantedFrom; // ROLE, DIRECT, INHERITED

    // Default constructor
    public UserPermissionDto() {}

    // Constructor with required fields
    public UserPermissionDto(Long userId, String permissionName, String resource, String action) {
        this.userId = userId;
        this.permissionName = permissionName;
        this.resource = resource;
        this.action = action;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public String getGrantedFrom() {
        return grantedFrom;
    }

    public void setGrantedFrom(String grantedFrom) {
        this.grantedFrom = grantedFrom;
    }

    /**
     * Check if user has permission for specific resource and action
     */
    public boolean hasPermission(String resource, String action) {
        return this.isActive && 
               this.resource.equals(resource) && 
               this.action.equals(action);
    }

    /**
     * Check if permission is inherited from role
     */
    public boolean isInheritedFromRole() {
        return "ROLE".equals(this.grantedFrom);
    }

    /**
     * Check if permission is directly granted
     */
    public boolean isDirectlyGranted() {
        return "DIRECT".equals(this.grantedFrom);
    }

    /**
     * Get full permission string
     */
    public String getFullPermission() {
        return resource + ":" + action + ":" + scope;
    }
}
