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
    
    // For sequence-based mapping during creation
    private Integer taskSequence;
    private List<Integer> parentTaskSequences;  // List of parent task sequences
    
    
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
    private String fileSourceTaskIds;  // Comma-separated list of task IDs whose files can be accessed
    
    private String parallelTaskIds;  // Comma-separated list of task IDs that can run in parallel
    
    private Integer fileRetentionDays;  // How long to keep files after task completion
    
    
    // Consolidation fields
    private String consolidationMode = "MANUAL";  // MANUAL, AUTO, HYBRID
    
    private String consolidationRules;  // JSON string for consolidation logic
    
    private String fileSelectionStrategy = "ALL_AVAILABLE";  // ALL_AVAILABLE, USER_SELECT, AUTO_SELECT
    
    private Integer maxFileSelections;  // Maximum number of files user can select for consolidation
    
    private Integer minFileSelections = 1;  // Minimum number of files required for consolidation
    
    private Long consolidationTemplateId;  // Reference to consolidation template
    
    // Decision fields
    private String decisionRequiresApproval = "Y";  // Y/N - whether decision needs approval
    
    private Long decisionApprovalRoleId;  // Role that can approve decisions
    
    private String revisionStrategy = "SINGLE_TASK";  // SINGLE_TASK, CASCADE, SELECTIVE
    
    private String revisionTaskMapping;  // JSON string mapping decision outcomes to target tasks
    
    // Task execution fields
    private String canBeRevisited = "N";  // Y/N - whether task can be revisited
    private Integer maxRevisits = 0;  // Maximum number of times task can be revisited
    private String canRunInParallel = "N";  // Y/N - whether task can run in parallel with other tasks
    
    // Related data
    private String roleName;
    private Integer roleSequence; // For UI-based workflow creation
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
    
    public Integer getTaskSequence() {
        return taskSequence;
    }
    
    public void setTaskSequence(Integer taskSequence) {
        this.taskSequence = taskSequence;
    }
    
    public List<Integer> getParentTaskSequences() {
        return parentTaskSequences;
    }
    
    public void setParentTaskSequences(List<Integer> parentTaskSequences) {
        this.parentTaskSequences = parentTaskSequences;
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
    
    
    public String getFileSourceTaskIds() {
        return fileSourceTaskIds;
    }
    
    public void setFileSourceTaskIds(String fileSourceTaskIds) {
        this.fileSourceTaskIds = fileSourceTaskIds;
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
    
    public String getCanRunInParallel() {
        return canRunInParallel;
    }
    
    public void setCanRunInParallel(String canRunInParallel) {
        this.canRunInParallel = canRunInParallel;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public Integer getRoleSequence() {
        return roleSequence;
    }
    
    public void setRoleSequence(Integer roleSequence) {
        this.roleSequence = roleSequence;
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
    
    public boolean isDecisionTask() {
        return "Y".equals(isDecisionTask);
    }
    
    public boolean isAutoEscalationEnabled() {
        return "Y".equals(autoEscalationEnabled);
    }
    
    public boolean isNotificationRequired() {
        return "Y".equals(notificationRequired);
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
    
    
    @Override
    public String toString() {
        return "WorkflowConfigTaskDto{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskType=" + taskType +
                ", roleId=" + roleId +
                ", sequenceOrder=" + sequenceOrder +
                ", parentTaskIds='" + parentTaskIds + '\'' +
                '}';
    }
}
