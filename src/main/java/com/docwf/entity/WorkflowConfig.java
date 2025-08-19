package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKFLOW_CONFIG")
@Audited
public class WorkflowConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CONFIG")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CONFIG", sequenceName = "SEQ_WORKFLOW_CONFIG", allocationSize = 1)
    @Column(name = "WORKFLOW_ID")
    private Long workflowId;
    
    @NotBlank
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    
    @Column(name = "REMINDER_BEFORE_DUE_MINS")
    private Integer reminderBeforeDueMins;
    
    @Column(name = "MINUTES_AFTER_DUE")
    private Integer minutesAfterDue;
    
    @Column(name = "ESCALATION_AFTER_MINS")
    private Integer escalationAfterMins;
    
    @Column(name = "DUE_IN_MINS")
    private Integer dueInMins;
    
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
    
    // Relationships
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowConfigRole> workflowRoles = new ArrayList<>();
    
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowConfigTask> tasks = new ArrayList<>();
    
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowConfigParam> parameters = new ArrayList<>();
    
    // Constructors
    public WorkflowConfig() {}
    
    public WorkflowConfig(String name, String description, String createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getReminderBeforeDueMins() {
        return reminderBeforeDueMins;
    }
    
    public void setReminderBeforeDueMins(Integer reminderBeforeDueMins) {
        this.reminderBeforeDueMins = reminderBeforeDueMins;
    }
    
    public Integer getMinutesAfterDue() {
        return minutesAfterDue;
    }
    
    public void setMinutesAfterDue(Integer minutesAfterDue) {
        this.minutesAfterDue = minutesAfterDue;
    }
    
    public Integer getEscalationAfterMins() {
        return escalationAfterMins;
    }
    
    public void setEscalationAfterMins(Integer escalationAfterMins) {
        this.escalationAfterMins = escalationAfterMins;
    }
    
    public Integer getDueInMins() {
        return dueInMins;
    }
    
    public void setDueInMins(Integer dueInMins) {
        this.dueInMins = dueInMins;
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
    
    public List<WorkflowConfigRole> getWorkflowRoles() {
        return workflowRoles;
    }
    
    public void setWorkflowRoles(List<WorkflowConfigRole> workflowRoles) {
        this.workflowRoles = workflowRoles;
    }
    
    public List<WorkflowConfigTask> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<WorkflowConfigTask> tasks) {
        this.tasks = tasks;
    }
    
    public List<WorkflowConfigParam> getParameters() {
        return parameters;
    }
    
    public void setParameters(List<WorkflowConfigParam> parameters) {
        this.parameters = parameters;
    }
    
    // Helper methods
    public void addWorkflowRole(WorkflowConfigRole workflowRole) {
        workflowRoles.add(workflowRole);
        workflowRole.setWorkflow(this);
    }
    
    public void addTask(WorkflowConfigTask task) {
        tasks.add(task);
        task.setWorkflow(this);
    }
    
    public void addParameter(WorkflowConfigParam param) {
        parameters.add(param);
        param.setWorkflow(this);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfig{" +
                "workflowId=" + workflowId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
