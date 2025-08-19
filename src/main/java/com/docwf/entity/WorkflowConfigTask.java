package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKFLOW_CONFIG_TASK")
@Audited
public class WorkflowConfigTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CONFIG_TASK")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CONFIG_TASK", sequenceName = "SEQ_WORKFLOW_CONFIG_TASK", allocationSize = 1)
    @Column(name = "TASK_ID")
    private Long taskId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKFLOW_ID", nullable = false)
    private WorkflowConfig workflow;
    
    @NotBlank
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TASK_TYPE", length = 50)
    private TaskType taskType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private WorkflowRole role;
    
    @Column(name = "EXPECTED_COMPLETION")
    private Integer expectedCompletion;
    
    @NotNull
    @Column(name = "SEQUENCE_ORDER", nullable = false)
    private Integer sequenceOrder;
    
    @Column(name = "ESCALATION_RULES", length = 500)
    private String escalationRules;
    
    // Relationships
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowConfigTaskFile> taskFiles = new ArrayList<>();
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDecisionOutcome> decisionOutcomes = new ArrayList<>();
    
    // Enums
    public enum TaskType {
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        FILE_UPDATE,
        CONSOLIDATE_FILES,
        DECISION
    }
    
    // Constructors
    public WorkflowConfigTask() {}
    
    public WorkflowConfigTask(String name, TaskType taskType, Integer sequenceOrder) {
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
    
    public WorkflowConfig getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(WorkflowConfig workflow) {
        this.workflow = workflow;
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
    
    public WorkflowRole getRole() {
        return role;
    }
    
    public void setRole(WorkflowRole role) {
        this.role = role;
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
    
    public List<WorkflowConfigTaskFile> getTaskFiles() {
        return taskFiles;
    }
    
    public void setTaskFiles(List<WorkflowConfigTaskFile> taskFiles) {
        this.taskFiles = taskFiles;
    }
    
    public List<TaskDecisionOutcome> getDecisionOutcomes() {
        return decisionOutcomes;
    }
    
    public void setDecisionOutcomes(List<TaskDecisionOutcome> decisionOutcomes) {
        this.decisionOutcomes = decisionOutcomes;
    }
    
    // Helper methods
    public void addTaskFile(WorkflowConfigTaskFile taskFile) {
        taskFiles.add(taskFile);
        taskFile.setTask(this);
    }
    
    public void addDecisionOutcome(TaskDecisionOutcome outcome) {
        decisionOutcomes.add(outcome);
        outcome.setTask(this);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTask{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskType=" + taskType +
                ", sequenceOrder=" + sequenceOrder +
                '}';
    }
}
