package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
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
    @Column(name = "TASK_TYPE", length = 50, nullable = false)
    private TaskType taskType;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private WorkflowRole role;  // Role required to access this task
    
    @Column(name = "EXPECTED_COMPLETION")
    private Integer expectedCompletion;
    
    @NotNull
    @Column(name = "SEQUENCE_ORDER", nullable = false)
    private Integer sequenceOrder;
    
    @Column(name = "ESCALATION_RULES", length = 500)
    private String escalationRules;
    
    @Column(name = "PARENT_TASK_IDS")
    private String parentTaskIds;  // Comma-separated list of parent task IDs
    
    @Column(name = "CAN_BE_REVISITED", length = 1)
    private String canBeRevisited = "N";  // Y/N
    
    @Column(name = "MAX_REVISITS")
    private Integer maxRevisits = 0;
    
    @Column(name = "FILE_SELECTION_MODE", length = 50)
    private String fileSelectionMode = "USER_SELECT";  // USER_SELECT, ALL_FILES, AUTO_SELECT
    
    @Column(name = "SOURCE_TASK_IDS")
    private String sourceTaskIds;  // Comma-separated list of source task IDs for file selection
    
    @Column(name = "FILE_FILTER_JSON")
    private String fileFilterJson;  // JSON string for file filtering rules
    
    @Column(name = "CONSOLIDATION_RULES_JSON")
    private String consolidationRulesJson;  // JSON string for consolidation logic
    
    @Column(name = "DECISION_CRITERIA_JSON")
    private String decisionCriteriaJson;  // JSON string for decision logic
    
    @Column(name = "TASK_DESCRIPTION", length = 1000)
    private String taskDescription;
    
    @Column(name = "IS_DECISION_TASK", length = 1)
    private String isDecisionTask = "N";  // Y/N
    
    @Column(name = "DECISION_TYPE", length = 100)
    private String decisionType;  // APPROVAL, REJECTION, MULTI_CHOICE, RATING, etc.
    
    @Column(name = "TASK_PRIORITY", length = 20)
    private String taskPriority = "MEDIUM";  // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "AUTO_ESCALATION_ENABLED", length = 1)
    private String autoEscalationEnabled = "Y";  // Y/N
    
    @Column(name = "NOTIFICATION_REQUIRED", length = 1)
    private String notificationRequired = "Y";  // Y/N
    
    @Column(name = "TASK_STATUS", length = 50)
    private String taskStatus = "PENDING";  // PENDING, IN_PROGRESS, COMPLETED, REJECTED
    
    @Column(name = "TASK_STARTED_AT")
    private LocalDateTime taskStartedAt;  // When the task was started
    
    @Column(name = "TASK_COMPLETED_AT")
    private LocalDateTime taskCompletedAt;  // When the task was completed
    
    @Column(name = "TASK_REJECTED_AT")
    private LocalDateTime taskRejectedAt;  // When the task was rejected
    
    @Column(name = "TASK_REJECTION_REASON")
    private String taskRejectionReason;  // Reason for task rejection
    
    @Column(name = "TASK_COMPLETED_BY")
    private String taskCompletedBy;  // User who completed the task
    
    @Column(name = "TASK_REJECTED_BY")
    private String taskRejectedBy;  // User who rejected the task
    
    // File management fields
    @Column(name = "ALLOW_NEW_FILES", length = 1)
    private String allowNewFiles = "N";  // Y/N
    
    @Column(name = "FILE_SOURCE_TASK_IDS")
    private String fileSourceTaskIds;  // Comma-separated list of task IDs that provide files for this task
    
    @Column(name = "CONSOLIDATION_MODE", length = 50)
    private String consolidationMode = "MANUAL";  // MANUAL, AUTO, HYBRID
    
    @Column(name = "CONSOLIDATION_RULES", length = 1000)
    private String consolidationRules;  // JSON string for consolidation logic
    
    @Column(name = "FILE_SELECTION_STRATEGY", length = 50)
    private String fileSelectionStrategy = "ALL_AVAILABLE";  // ALL_AVAILABLE, USER_SELECT, AUTO_SELECT
    
    @Column(name = "MAX_FILE_SELECTIONS")
    private Integer maxFileSelections;  // Maximum number of files user can select for consolidation
    
    @Column(name = "MIN_FILE_SELECTIONS")
    private Integer minFileSelections = 1;  // Minimum number of files required for consolidation
    
    @Column(name = "CONSOLIDATION_TEMPLATE_ID")
    private Long consolidationTemplateId;  // Reference to consolidation template
    
    @Column(name = "DECISION_REQUIRES_APPROVAL", length = 1)
    private String decisionRequiresApproval = "Y";  // Y/N - whether decision needs approval
    
    @Column(name = "DECISION_APPROVAL_ROLE_ID")
    private Long decisionApprovalRoleId;  // Role that can approve decisions
    
    @Column(name = "REVISION_STRATEGY", length = 50)
    private String revisionStrategy = "SINGLE_TASK";  // SINGLE_TASK, CASCADE, SELECTIVE
    
    @Column(name = "REVISION_TASK_MAPPING")
    private String revisionTaskMapping;  // JSON string mapping decision outcomes to target tasks
    
    @Column(name = "CAN_RUN_IN_PARALLEL", length = 1)
    private String canRunInParallel = "N";  // Y/N - whether this task can run parallel to other tasks
    
    @Column(name = "PARALLEL_TASK_IDS")
    private String parallelTaskIds;  // Comma-separated list of task IDs that can run in parallel
    
    @Column(name = "FILE_RETENTION_DAYS")
    private Integer fileRetentionDays;  // How long to keep files after task completion
    
    @Column(name = "KEEP_FILE_VERSIONS", length = 1)
    private String keepFileVersions = "Y";  // Y/N - whether to keep file version history
    
    @Column(name = "MAX_FILE_VERSIONS")
    private Integer maxFileVersions = 10;  // Maximum number of file versions to keep
    
    @Column(name = "KEEP_FILE_HISTORY", length = 1)
    private String keepFileHistory = "Y";  // Y/N - whether to keep file change history
    
    @Column(name = "FILE_HISTORY_DETAILS", length = 1)
    private String fileHistoryDetails = "Y";  // Y/N - whether to track detailed file change history
    
    // Relationships
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowConfigTaskFile> taskFiles = new ArrayList<>();
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDecisionOutcome> decisionOutcomes = new ArrayList<>();
    
    // Enums - Simplified to match user requirements
    public enum TaskType {
        FILE_UPLOAD,      // Upload files to the workflow
        FILE_UPDATE,      // Update/modify existing files
        CONSOLIDATE_FILE, // Consolidate multiple files
        DECISION          // Make approval/rejection decisions
    }
    
    // Constructors
    public WorkflowConfigTask() {}
    
    public WorkflowConfigTask(String name, TaskType taskType, WorkflowRole role, Integer sequenceOrder) {
        this.name = name;
        this.taskType = taskType;
        this.role = role;
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
    
    public String getParentTaskIds() {
        return parentTaskIds;
    }
    
    public void setParentTaskIds(String parentTaskIds) {
        this.parentTaskIds = parentTaskIds;
    }
    
    public String getCanBeRevisited() {
        return canBeRevisited;
    }
    
    public void setCanBeRevisited(String canBeRevisited) {
        this.canBeRevisited = canBeRevisited;
    }
    
    public Integer getMaxRevisits() {
        return maxRevisits;
    }
    
    public void setMaxRevisits(Integer maxRevisits) {
        this.maxRevisits = maxRevisits;
    }
    
    public String getFileSelectionMode() {
        return fileSelectionMode;
    }
    
    public void setFileSelectionMode(String fileSelectionMode) {
        this.fileSelectionMode = fileSelectionMode;
    }
    
    public String getSourceTaskIds() {
        return sourceTaskIds;
    }
    
    public void setSourceTaskIds(String sourceTaskIds) {
        this.sourceTaskIds = sourceTaskIds;
    }
    
    public String getFileFilterJson() {
        return fileFilterJson;
    }
    
    public void setFileFilterJson(String fileFilterJson) {
        this.fileFilterJson = fileFilterJson;
    }
    
    public String getConsolidationRulesJson() {
        return consolidationRulesJson;
    }
    
    public void setConsolidationRulesJson(String consolidationRulesJson) {
        this.consolidationRulesJson = consolidationRulesJson;
    }
    
    public String getDecisionCriteriaJson() {
        return decisionCriteriaJson;
    }
    
    public void setDecisionCriteriaJson(String decisionCriteriaJson) {
        this.decisionCriteriaJson = decisionCriteriaJson;
    }
    
    public String getTaskDescription() {
        return taskDescription;
    }
    
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    
    public String getIsDecisionTask() {
        return isDecisionTask;
    }
    
    public void setIsDecisionTask(String isDecisionTask) {
        this.isDecisionTask = isDecisionTask;
    }
    
    public String getDecisionType() {
        return decisionType;
    }
    
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }
    
    public String getTaskPriority() {
        return taskPriority;
    }
    
    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }
    
    public String getAutoEscalationEnabled() {
        return autoEscalationEnabled;
    }
    
    public void setAutoEscalationEnabled(String autoEscalationEnabled) {
        this.autoEscalationEnabled = autoEscalationEnabled;
    }
    
    public String getNotificationRequired() {
        return notificationRequired;
    }
    
    public void setNotificationRequired(String notificationRequired) {
        this.notificationRequired = notificationRequired;
    }
    
    public String getTaskStatus() {
        return taskStatus;
    }
    
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    
    public LocalDateTime getTaskStartedAt() {
        return taskStartedAt;
    }
    
    public void setTaskStartedAt(LocalDateTime taskStartedAt) {
        this.taskStartedAt = taskStartedAt;
    }
    
    public LocalDateTime getTaskCompletedAt() {
        return taskCompletedAt;
    }
    
    public void setTaskCompletedAt(LocalDateTime taskCompletedAt) {
        this.taskCompletedAt = taskCompletedAt;
    }
    
    public LocalDateTime getTaskRejectedAt() {
        return taskRejectedAt;
    }
    
    public void setTaskRejectedAt(LocalDateTime taskRejectedAt) {
        this.taskRejectedAt = taskRejectedAt;
    }
    
    public String getTaskRejectionReason() {
        return taskRejectionReason;
    }
    
    public void setTaskRejectionReason(String taskRejectionReason) {
        this.taskRejectionReason = taskRejectionReason;
    }
    
    public String getTaskCompletedBy() {
        return taskCompletedBy;
    }
    
    public void setTaskCompletedBy(String taskCompletedBy) {
        this.taskCompletedBy = taskCompletedBy;
    }
    
    public String getTaskRejectedBy() {
        return taskRejectedBy;
    }
    
    public void setTaskRejectedBy(String taskRejectedBy) {
        this.taskRejectedBy = taskRejectedBy;
    }
    
    public String getAllowNewFiles() {
        return allowNewFiles;
    }
    
    public void setAllowNewFiles(String allowNewFiles) {
        this.allowNewFiles = allowNewFiles;
    }
    
    public String getFileSourceTaskIds() {
        return fileSourceTaskIds;
    }
    
    public void setFileSourceTaskIds(String fileSourceTaskIds) {
        this.fileSourceTaskIds = fileSourceTaskIds;
    }
    
    public String getConsolidationMode() {
        return consolidationMode;
    }
    
    public void setConsolidationMode(String consolidationMode) {
        this.consolidationMode = consolidationMode;
    }
    
    public String getConsolidationRules() {
        return consolidationRules;
    }
    
    public void setConsolidationRules(String consolidationRules) {
        this.consolidationRules = consolidationRules;
    }
    
    public String getFileSelectionStrategy() {
        return fileSelectionStrategy;
    }
    
    public void setFileSelectionStrategy(String fileSelectionStrategy) {
        this.fileSelectionStrategy = fileSelectionStrategy;
    }
    
    public Integer getMaxFileSelections() {
        return maxFileSelections;
    }
    
    public void setMaxFileSelections(Integer maxFileSelections) {
        this.maxFileSelections = maxFileSelections;
    }
    
    public Integer getMinFileSelections() {
        return minFileSelections;
    }
    
    public void setMinFileSelections(Integer minFileSelections) {
        this.minFileSelections = minFileSelections;
    }
    
    public Long getConsolidationTemplateId() {
        return consolidationTemplateId;
    }
    
    public void setConsolidationTemplateId(Long consolidationTemplateId) {
        this.consolidationTemplateId = consolidationTemplateId;
    }
    
    public String getDecisionRequiresApproval() {
        return decisionRequiresApproval;
    }
    
    public void setDecisionRequiresApproval(String decisionRequiresApproval) {
        this.decisionRequiresApproval = decisionRequiresApproval;
    }
    
    public Long getDecisionApprovalRoleId() {
        return decisionApprovalRoleId;
    }
    
    public void setDecisionApprovalRoleId(Long decisionApprovalRoleId) {
        this.decisionApprovalRoleId = decisionApprovalRoleId;
    }
    
    public String getRevisionStrategy() {
        return revisionStrategy;
    }
    
    public void setRevisionStrategy(String revisionStrategy) {
        this.revisionStrategy = revisionStrategy;
    }
    
    public String getRevisionTaskMapping() {
        return revisionTaskMapping;
    }
    
    public void setRevisionTaskMapping(String revisionTaskMapping) {
        this.revisionTaskMapping = revisionTaskMapping;
    }
    
    public String getCanRunInParallel() {
        return canRunInParallel;
    }
    
    public void setCanRunInParallel(String canRunInParallel) {
        this.canRunInParallel = canRunInParallel;
    }
    
    public String getParallelTaskIds() {
        return parallelTaskIds;
    }
    
    public void setParallelTaskIds(String parallelTaskIds) {
        this.parallelTaskIds = parallelTaskIds;
    }
    
    public Integer getFileRetentionDays() {
        return fileRetentionDays;
    }
    
    public void setFileRetentionDays(Integer fileRetentionDays) {
        this.fileRetentionDays = fileRetentionDays;
    }
    
    public String getKeepFileVersions() {
        return keepFileVersions;
    }
    
    public void setKeepFileVersions(String keepFileVersions) {
        this.keepFileVersions = keepFileVersions;
    }
    
    public Integer getMaxFileVersions() {
        return maxFileVersions;
    }
    
    public void setMaxFileVersions(Integer maxFileVersions) {
        this.maxFileVersions = maxFileVersions;
    }
    
    public String getKeepFileHistory() {
        return keepFileHistory;
    }
    
    public void setKeepFileHistory(String keepFileHistory) {
        this.keepFileHistory = keepFileHistory;
    }
    
    public String getFileHistoryDetails() {
        return fileHistoryDetails;
    }
    
    public void setFileHistoryDetails(String fileHistoryDetails) {
        this.fileHistoryDetails = fileHistoryDetails;
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
    
    public boolean canBeRevisited() {
        return "Y".equals(canBeRevisited);
    }
    
    public boolean isAllowNewFiles() {
        return "Y".equals(allowNewFiles);
    }
    
    public boolean canRunInParallel() {
        return "Y".equals(canRunInParallel);
    }
    
    public boolean isDecisionTask() {
        return "Y".equals(isDecisionTask);
    }
    
    public boolean isConsolidationTask() {
        return "CONSOLIDATE_FILE".equals(taskType);
    }
    
    public boolean isUpdateTask() {
        return "FILE_UPDATE".equals(taskType);
    }
    
    public boolean isUploadTask() {
        return "FILE_UPLOAD".equals(taskType);
    }
    
    public boolean isManualConsolidation() {
        return "MANUAL".equals(consolidationMode);
    }
    
    public boolean isAutoConsolidation() {
        return "AUTO".equals(consolidationMode);
    }
    
    public boolean isHybridConsolidation() {
        return "HYBRID".equals(consolidationMode);
    }
    
    public boolean requiresUserFileSelection() {
        return "USER_SELECT".equals(fileSelectionStrategy);
    }
    
    public boolean requiresDecisionApproval() {
        return "Y".equals(decisionRequiresApproval);
    }
    
    public boolean isSingleTaskRevision() {
        return "SINGLE_TASK".equals(revisionStrategy);
    }
    
    public boolean isCascadeRevision() {
        return "CASCADE".equals(revisionStrategy);
    }
    
    public boolean isSelectiveRevision() {
        return "SELECTIVE".equals(revisionStrategy);
    }
    
    public List<Long> getRevisionTaskMappingList() {
        if (revisionTaskMapping == null || revisionTaskMapping.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : revisionTaskMapping.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
    
    public boolean isAutoEscalationEnabled() {
        return "Y".equals(autoEscalationEnabled);
    }
    
    public boolean isNotificationRequired() {
        return "Y".equals(notificationRequired);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTask{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskType=" + taskType +
                ", role=" + (role != null ? role.getRoleName() : "null") +
                ", sequenceOrder=" + sequenceOrder +
                ", parentTaskIds='" + parentTaskIds + '\'' +
                ", canBeRevisited='" + canBeRevisited + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", allowNewFiles='" + allowNewFiles + '\'' +
                '}';
    }
}
