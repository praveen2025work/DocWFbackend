package com.docwf.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Escalation Queue Items
 */
public class EscalationItemDto {
    
    private Long escalationId;
    private String escalationType; // WORKFLOW, TASK, DECISION
    private Long entityId; // Workflow or Task ID
    private String entityName;
    private String escalationReason;
    private Long escalatedFromUserId;
    private String escalatedFromUsername;
    private Long escalatedToUserId;
    private String escalatedToUsername;
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String status; // PENDING, IN_PROGRESS, RESOLVED, CLOSED
    private LocalDateTime escalatedAt;
    private LocalDateTime dueBy;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;

    // Default constructor
    public EscalationItemDto() {}

    // Constructor with required fields
    public EscalationItemDto(Long escalationId, String escalationType, Long entityId, String escalationReason) {
        this.escalationId = escalationId;
        this.escalationType = escalationType;
        this.entityId = entityId;
        this.escalationReason = escalationReason;
        this.escalatedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getEscalationId() {
        return escalationId;
    }

    public void setEscalationId(Long escalationId) {
        this.escalationId = escalationId;
    }

    public String getEscalationType() {
        return escalationType;
    }

    public void setEscalationType(String escalationType) {
        this.escalationType = escalationType;
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

    public String getEscalationReason() {
        return escalationReason;
    }

    public void setEscalationReason(String escalationReason) {
        this.escalationReason = escalationReason;
    }

    public Long getEscalatedFromUserId() {
        return escalatedFromUserId;
    }

    public void setEscalatedFromUserId(Long escalatedFromUserId) {
        this.escalatedFromUserId = escalatedFromUserId;
    }

    public String getEscalatedFromUsername() {
        return escalatedFromUsername;
    }

    public void setEscalatedFromUsername(String escalatedFromUsername) {
        this.escalatedFromUsername = escalatedFromUsername;
    }

    public Long getEscalatedToUserId() {
        return escalatedToUserId;
    }

    public void setEscalatedToUserId(Long escalatedToUserId) {
        this.escalatedToUserId = escalatedToUserId;
    }

    public String getEscalatedToUsername() {
        return escalatedToUsername;
    }

    public void setEscalatedToUsername(String escalatedToUsername) {
        this.escalatedToUsername = escalatedToUsername;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(LocalDateTime escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public LocalDateTime getDueBy() {
        return dueBy;
    }

    public void setDueBy(LocalDateTime dueBy) {
        this.dueBy = dueBy;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
}
