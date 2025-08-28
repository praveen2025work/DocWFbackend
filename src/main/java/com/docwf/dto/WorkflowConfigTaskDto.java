package com.docwf.dto;

import com.docwf.entity.WorkflowConfigTask.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class WorkflowConfigTaskDto {
    
    private Long taskId;
    
    private Long workflowId;
    
    @NotBlank(message = "Task name is required")
    private String name;
    
    @NotNull(message = "Task type is required")
    private TaskType taskType;
    
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    private Integer expectedCompletion;
    
    @NotNull(message = "Sequence order is required")
    private Integer sequenceOrder;
    
    private String escalationRules;
    
    private String parentTaskIds;  // Comma-separated list of parent task IDs
    
    private String canBeRevisited;  // Y/N
    
    private Integer maxRevisits;
    
    private String fileSelectionMode;  // USER_SELECT, ALL_FILES, AUTO_SELECT
    
    private String sourceTaskIds;  // Comma-separated list of source task IDs
    
    private String fileFilterJson;  // JSON string for file filtering rules
    
    private String consolidationRulesJson;  // JSON string for consolidation logic
    
    private String decisionCriteriaJson;  // JSON string for decision logic
    
    private String taskDescription;
    
    private String isDecisionTask;  // Y/N
    
    private String decisionType;  // APPROVAL, REJECTION, MULTI_CHOICE, RATING, etc.
    
    private String taskPriority;  // LOW, MEDIUM, HIGH, CRITICAL
    
    private String autoEscalationEnabled;  // Y/N
    
    private String notificationRequired;  // Y/N
    
    private String taskStatus;  // PENDING, IN_PROGRESS, COMPLETED, REJECTED
    
    private LocalDateTime taskStartedAt;  // When the task was started
    
    private LocalDateTime taskCompletedAt;  // When the task was completed
    
    private LocalDateTime taskRejectedAt;  // When the task was rejected
    
    private String taskRejectionReason;  // Reason for task rejection
    
    private String taskCompletedBy;  // User who completed the task
    
    private String taskRejectedBy;  // User who rejected the task
    
    // File management fields
    private String allowNewFiles;  // Y/N - whether users can add new files in this task
    
    private String fileSourceTaskIds;  // Comma-separated list of task IDs whose files can be accessed
    
    private String canRunInParallel;  // Y/N - whether this task can run parallel to other tasks
    
    private String parallelTaskIds;  // Comma-separated list of task IDs that can run in parallel
    
    private Integer fileRetentionDays;  // How long to keep files after task completion
    
    private String keepFileVersions;  // Y/N - whether to keep file version history
    
    private Integer maxFileVersions;  // Maximum number of file versions to keep
    
    private String keepFileHistory;  // Y/N - whether to keep file change history
    
    private String fileHistoryDetails;  // Y/N - whether to track detailed file change history
    
    // Related data
    private String roleName;
    private List<WorkflowConfigTaskFileDto> taskFiles;
    private List<TaskDecisionOutcomeDto> decisionOutcomes;
    
    // Constructors
    public WorkflowConfigTaskDto() {}
    
    public WorkflowConfigTaskDto(String name, TaskType taskType, Long roleId, Integer sequenceOrder) {
        this.name = name;
        this.taskType = taskType;
        this.roleId = roleId;
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
    
    // Helper methods
    public boolean canBeRevisited() {
        return "Y".equals(canBeRevisited);
    }
    
    public boolean isDecisionTask() {
        return "Y".equals(isDecisionTask);
    }
    
    public boolean isAutoEscalationEnabled() {
        return "Y".equals(autoEscalationEnabled);
    }
    
    public boolean isNotificationRequired() {
        return "Y".equals(notificationRequired);
    }
    
    public boolean isAllowNewFiles() {
        return "Y".equals(allowNewFiles);
    }
    
    public boolean canRunInParallel() {
        return "Y".equals(canRunInParallel);
    }
    
    public boolean isPending() {
        return "PENDING".equals(taskStatus);
    }
    
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(taskStatus);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(taskStatus);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(taskStatus);
    }
    
    public List<Long> getParentTaskIdList() {
        if (parentTaskIds == null || parentTaskIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : parentTaskIds.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
    
    public List<Long> getSourceTaskIdList() {
        if (sourceTaskIds == null || sourceTaskIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : sourceTaskIds.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
    
    public List<Long> getFileSourceTaskIdList() {
        if (fileSourceTaskIds == null || fileSourceTaskIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : fileSourceTaskIds.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
    
    public List<Long> getParallelTaskIdList() {
        if (parallelTaskIds == null || parallelTaskIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : parallelTaskIds.split(",")) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        return ids;
    }
    
    public boolean keepFileVersions() {
        return "Y".equals(keepFileVersions);
    }
    
    public boolean keepFileHistory() {
        return "Y".equals(keepFileHistory);
    }
    
    public boolean fileHistoryDetails() {
        return "Y".equals(fileHistoryDetails);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskDto{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskType=" + taskType +
                ", roleId=" + roleId +
                ", sequenceOrder=" + sequenceOrder +
                ", parentTaskIds='" + parentTaskIds + '\'' +
                ", canBeRevisited='" + canBeRevisited + '\'' +
                '}';
    }
}
