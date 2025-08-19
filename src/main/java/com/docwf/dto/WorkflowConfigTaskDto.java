package com.docwf.dto;

import com.docwf.entity.WorkflowConfigTask.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class WorkflowConfigTaskDto {
    
    private Long taskId;
    
    private Long workflowId;
    
    @NotBlank(message = "Task name is required")
    private String name;
    
    private TaskType taskType;
    
    private Long roleId;
    
    private Integer expectedCompletion;
    
    @NotNull(message = "Sequence order is required")
    private Integer sequenceOrder;
    
    private String escalationRules;
    
    // Related data
    private String roleName;
    private List<WorkflowConfigTaskFileDto> taskFiles;
    private List<TaskDecisionOutcomeDto> decisionOutcomes;
    
    // Constructors
    public WorkflowConfigTaskDto() {}
    
    public WorkflowConfigTaskDto(String name, TaskType taskType, Integer sequenceOrder) {
        this.name = name;
        this.taskType = taskType;
        this.sequenceOrder = sequenceOrder;
    }
    
    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
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
    
    public TaskType getTaskType() {
        return taskType;
    }
    
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public Integer getExpectedCompletion() {
        return expectedCompletion;
    }
    
    public void setExpectedCompletion(Integer expectedCompletion) {
        this.expectedCompletion = expectedCompletion;
    }
    
    public Integer getSequenceOrder() {
        return sequenceOrder;
    }
    
    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
    
    public String getEscalationRules() {
        return escalationRules;
    }
    
    public void setEscalationRules(String escalationRules) {
        this.escalationRules = escalationRules;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public List<WorkflowConfigTaskFileDto> getTaskFiles() {
        return taskFiles;
    }
    
    public void setTaskFiles(List<WorkflowConfigTaskFileDto> taskFiles) {
        this.taskFiles = taskFiles;
    }
    
    public List<TaskDecisionOutcomeDto> getDecisionOutcomes() {
        return decisionOutcomes;
    }
    
    public void setDecisionOutcomes(List<TaskDecisionOutcomeDto> decisionOutcomes) {
        this.decisionOutcomes = decisionOutcomes;
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskDto{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskType=" + taskType +
                ", sequenceOrder=" + sequenceOrder +
                '}';
    }
}
