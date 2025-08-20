package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User Activities
 */
public class UserActivityDto {
    
    private Long activityId;
    private Long userId;
    private String username;
    private String activityType; // TASK_COMPLETED, WORKFLOW_STARTED, DECISION_MADE, etc.
    private String activityDescription;
    private String entityType; // WORKFLOW, TASK, DECISION, FILE
    private Long entityId;
    private String entityName;
    private LocalDateTime activityTimestamp;
    private String details;
    private String ipAddress;
    private String userAgent;

    // Default constructor
    public UserActivityDto() {}

    // Constructor with required fields
    public UserActivityDto(Long userId, String activityType, String activityDescription) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityDescription = activityDescription;
        this.activityTimestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public LocalDateTime getActivityTimestamp() {
        return activityTimestamp;
    }

    public void setActivityTimestamp(LocalDateTime activityTimestamp) {
        this.activityTimestamp = activityTimestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
