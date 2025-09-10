package com.docwf.service.impl;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.dto.WorkflowProgressDto;
import com.docwf.dto.WorkflowInstanceStatsDto;
import com.docwf.dto.UserWorkloadDto;
import com.docwf.dto.CreateWorkflowInstanceDto;
import com.docwf.dto.ProcessOwnerDashboardDto;
import com.docwf.dto.EscalationItemDto;
import com.docwf.dto.ProcessOwnerStatsDto;
import com.docwf.dto.UserDashboardDto;
import com.docwf.dto.UserActivityDto;
import com.docwf.dto.UserNotificationDto;
import com.docwf.dto.UserCalendarDto;
import com.docwf.dto.UserPerformanceDto;
import com.docwf.dto.WorkflowRoleDto;
import com.docwf.dto.UserPermissionDto;
import com.docwf.dto.UserTeamDto;
import com.docwf.dto.UserPreferencesDto;
import com.docwf.dto.ManagerDashboardDto;
import com.docwf.dto.AdminDashboardDto;
import com.docwf.dto.WorkflowUserDto;
import com.docwf.dto.ProcessOwnerWorkloadDto;
import com.docwf.dto.ProcessOwnerPerformanceDto;
import com.docwf.entity.*;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.*;
import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
@Transactional
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutionServiceImpl.class);

    @Autowired
    private WorkflowInstanceRepository instanceRepository;
    
    @Autowired
    private WorkflowInstanceTaskRepository instanceTaskRepository;
    
    @Autowired
    private WorkflowConfigRepository workflowRepository;
    
    @Autowired
    private WorkflowConfigTaskRepository configTaskRepository;
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    @Autowired
    private TaskInstanceDecisionOutcomeRepository decisionOutcomeRepository;
    
    @Autowired
    private WorkflowCalendarService calendarService;
    
    @Override
    public WorkflowInstanceDto startWorkflow(Long workflowId, Long startedByUserId) {
        // Validate workflow exists and is active
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        if (!"Y".equals(workflow.getIsActive())) {
            throw new WorkflowException("Workflow is not active: " + workflowId);
        }
        
        // Validate user exists
        WorkflowUser startedByUser = userRepository.findById(startedByUserId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + startedByUserId));
        
        // Create workflow instance
        WorkflowInstance instance = new WorkflowInstance();
        instance.setWorkflow(workflow);
        instance.setStatus(WorkflowInstance.InstanceStatus.PENDING);
        instance.setStartedBy(startedByUser);
        instance.setStartedOn(LocalDateTime.now());
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        
        // Create instance tasks for all workflow tasks
        List<WorkflowConfigTask> configTasks = configTaskRepository.findByWorkflowWorkflowIdOrderBySequenceOrder(workflowId);
        for (WorkflowConfigTask configTask : configTasks) {
            WorkflowInstanceTask instanceTask = new WorkflowInstanceTask();
            instanceTask.setWorkflowInstance(savedInstance);
            instanceTask.setTask(configTask);
            instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
            instanceTask.setStartedOn(LocalDateTime.now());
            
            // Assign first task to the user who started the workflow
            if (configTask.getSequenceOrder() == 1) {
                instanceTask.setAssignedTo(startedByUser);
            }
            
            instanceTaskRepository.save(instanceTask);
        }
        
        return convertToInstanceDto(savedInstance);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowInstanceDto> getWorkflowInstance(Long instanceId) {
        return instanceRepository.findById(instanceId).map(this::convertToInstanceDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceDto> getWorkflowInstances(Long workflowId) {
        return instanceRepository.findByWorkflowWorkflowId(workflowId)
                .stream()
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceDto> getWorkflowInstancesByStatus(String status) {
        return instanceRepository.findByStatus(WorkflowInstance.InstanceStatus.valueOf(status.toUpperCase()))
                .stream()
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowInstanceDto updateInstanceStatus(Long instanceId, String status) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + instanceId));
        
        instance.setStatus(WorkflowInstance.InstanceStatus.valueOf(status.toUpperCase()));
        
        if (WorkflowInstance.InstanceStatus.COMPLETED.toString().equals(status)) {
            instance.setCompletedOn(LocalDateTime.now());
        }
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        return convertToInstanceDto(savedInstance);
    }
    
    @Override
    public WorkflowInstanceDto completeWorkflowInstance(Long instanceId) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + instanceId));
        
        // Check if all tasks are completed
        List<WorkflowInstanceTask> tasks = instanceTaskRepository.findByWorkflowInstanceInstanceId(instanceId);
        boolean allCompleted = tasks.stream()
                .allMatch(task -> WorkflowInstanceTask.TaskInstanceStatus.COMPLETED.equals(task.getStatus()));
        
        if (!allCompleted) {
            throw new WorkflowException("Cannot complete workflow. Not all tasks are completed.");
        }
        
        instance.setStatus(WorkflowInstance.InstanceStatus.COMPLETED);
        instance.setCompletedOn(LocalDateTime.now());
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        return convertToInstanceDto(savedInstance);
    }
    
    @Override
    public WorkflowInstanceDto cancelWorkflowInstance(Long instanceId) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + instanceId));
        
        instance.setStatus(WorkflowInstance.InstanceStatus.CANCELLED);
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        return convertToInstanceDto(savedInstance);
    }
    
    @Override
    public WorkflowInstanceDto escalateWorkflowInstance(Long instanceId, Long escalatedToUserId) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + instanceId));
        
        WorkflowUser escalatedToUser = userRepository.findById(escalatedToUserId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + escalatedToUserId));
        
        instance.setEscalatedTo(escalatedToUser);
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        return convertToInstanceDto(savedInstance);
    }
    
    // Task Instance Management
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getInstanceTasks(Long instanceId) {
        return instanceTaskRepository.findByWorkflowInstanceInstanceId(instanceId)
                .stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowInstanceTaskDto> getInstanceTask(Long instanceTaskId) {
        return instanceTaskRepository.findById(instanceTaskId).map(this::convertToInstanceTaskDto);
    }
    
    @Override
    public WorkflowInstanceTaskDto assignTask(Long instanceTaskId, Long userId) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        instanceTask.setAssignedTo(user);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(instanceTask);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public WorkflowInstanceTaskDto startTask(Long instanceTaskId) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        if (instanceTask.getAssignedTo() == null) {
            throw new WorkflowException("Task must be assigned before starting");
        }
        
        instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.IN_PROGRESS);
        instanceTask.setStartedOn(LocalDateTime.now());
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(instanceTask);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public WorkflowInstanceTaskDto completeTask(Long instanceTaskId, String decisionOutcome) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.COMPLETED);
        instanceTask.setCompletedOn(LocalDateTime.now());
        instanceTask.setDecisionOutcome(decisionOutcome);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(instanceTask);
        
        // Try to move to next task
        try {
            executeNextTask(instanceTask.getWorkflowInstance().getInstanceId());
        } catch (Exception e) {
            // Log error but don't fail the task completion
            // Add proper logging
            logger.error("Error executing next task for instance {}: {}", 
                instanceTask.getWorkflowInstance().getInstanceId(), e.getMessage(), e);
        }
        
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public WorkflowInstanceTaskDto failTask(Long instanceTaskId, String reason) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.FAILED);
        instanceTask.setDecisionOutcome("FAILED: " + reason);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(instanceTask);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public WorkflowInstanceTaskDto escalateTask(Long instanceTaskId, Long escalatedToUserId) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        WorkflowUser escalatedToUser = userRepository.findById(escalatedToUserId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + escalatedToUserId));
        
        instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.ESCALATED);
        instanceTask.setAssignedTo(escalatedToUser);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(instanceTask);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowInstanceTaskDto> getNextPendingTask(Long instanceId) {
        return instanceTaskRepository.findFirstPendingTask(instanceId)
                .map(this::convertToInstanceTaskDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getTasksAssignedToUser(Long userId) {
        return instanceTaskRepository.findByAssignedToUserId(userId)
                .stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getPendingTasksForUser(Long userId) {
        return instanceTaskRepository.findPendingTasksByAssignedToUserId(userId)
                .stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    // Task Decision Management
    @Override
    public TaskInstanceDecisionOutcomeDto recordDecisionOutcome(Long instanceTaskId, String outcomeName, String createdBy) {
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        TaskInstanceDecisionOutcome outcome = new TaskInstanceDecisionOutcome();
        outcome.setInstanceTask(instanceTask);
        outcome.setOutcomeName(outcomeName);
        outcome.setCreatedBy(createdBy);
        outcome.setCreatedAt(LocalDateTime.now());
        
        TaskInstanceDecisionOutcome savedOutcome = decisionOutcomeRepository.save(outcome);
        return convertToDecisionOutcomeDto(savedOutcome);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TaskInstanceDecisionOutcomeDto> getTaskDecisionOutcomes(Long instanceTaskId) {
        return decisionOutcomeRepository.findByInstanceTaskInstanceTaskId(instanceTaskId)
                .stream()
                .map(this::convertToDecisionOutcomeDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowInstanceTaskDto processDecisionOutcome(Long instanceTaskId, String outcomeName) {
        // Record the decision outcome
        recordDecisionOutcome(instanceTaskId, outcomeName, "system");
        
        // Process the decision and move to next task
        WorkflowInstanceTask instanceTask = instanceTaskRepository.findById(instanceTaskId)
                .orElseThrow(() -> new WorkflowException("Instance task not found with ID: " + instanceTaskId));
        
        Long instanceId = instanceTask.getWorkflowInstance().getInstanceId();
        return executeNextTask(instanceId);
    }
    
    // Workflow Execution Logic
    @Override
    public WorkflowInstanceTaskDto executeNextTask(Long instanceId) {
        // Find the next pending task
        Optional<WorkflowInstanceTask> nextTaskOpt = instanceTaskRepository.findFirstPendingTask(instanceId);
        if (nextTaskOpt.isEmpty()) {
            // Check if workflow is complete
            if (isWorkflowComplete(instanceId)) {
                updateInstanceStatus(instanceId, "COMPLETED");
            }
            throw new WorkflowException("No pending tasks found for instance: " + instanceId);
        }
        
        WorkflowInstanceTask nextTask = nextTaskOpt.get();
        
        if (nextTask != null) {
            // Assign the next task to a user based on role
            if (nextTask.getTask().getRole() != null) {
                // Find users with this role in the workflow
                // Implement role-based assignment logic
                List<WorkflowUser> usersWithRole = userRepository.findUsersByRoleName(nextTask.getTask().getRole().getRoleName());
                
                if (!usersWithRole.isEmpty()) {
                    // Simple round-robin assignment - in production, you might want more sophisticated logic
                    WorkflowUser assignedUser = usersWithRole.get(0);
                    nextTask.setAssignedTo(assignedUser);
                    nextTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
                    nextTask.setStartedOn(LocalDateTime.now());
                    
                    logger.info("Assigned task {} to user {} based on role {}", 
                        nextTask.getTask().getName(), assignedUser.getUsername(), 
                        nextTask.getTask().getRole().getRoleName());
                } else {
                    logger.warn("No users found with role {} for task {}", 
                        nextTask.getTask().getRole().getRoleName(), nextTask.getTask().getName());
                    nextTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
                }
            } else {
                // No role specified, leave unassigned
                nextTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
                logger.info("Task {} has no role specified, leaving unassigned", nextTask.getTask().getName());
            }
        }
        
        return convertToInstanceTaskDto(nextTask);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isWorkflowComplete(Long instanceId) {
        List<WorkflowInstanceTask> tasks = instanceTaskRepository.findByWorkflowInstanceInstanceId(instanceId);
        return tasks.stream()
                .allMatch(task -> WorkflowInstanceTask.TaskInstanceStatus.COMPLETED.equals(task.getStatus()) ||
                        WorkflowInstanceTask.TaskInstanceStatus.FAILED.equals(task.getStatus()));
    }
    
    @Override
    public void triggerWorkflowReminders() {
        // Implement reminder logic
        // This would involve checking workflows that need reminders and sending notifications
        logger.info("Starting workflow reminder trigger");
        
        try {
            // Get workflows that need reminders
            List<WorkflowConfig> workflowsNeedingReminders = workflowRepository.findWorkflowsNeedingReminders();
            
            for (WorkflowConfig workflow : workflowsNeedingReminders) {
                // Get active instances for this workflow
                List<WorkflowInstance> activeInstances = instanceRepository.findByWorkflowWorkflowIdAndStatus(
                    workflow.getWorkflowId(), WorkflowInstance.InstanceStatus.IN_PROGRESS);
                
                for (WorkflowInstance instance : activeInstances) {
                    // Check if reminder is due
                    if (shouldSendReminder(instance, workflow)) {
                        sendReminderNotification(instance, workflow);
                    }
                }
            }
            
            logger.info("Completed workflow reminder trigger for {} workflows", workflowsNeedingReminders.size());
        } catch (Exception e) {
            logger.error("Error in workflow reminder trigger", e);
        }
    }
    
    @Override
    public void triggerWorkflowEscalations() {
        // Implement escalation logic
        // This would involve checking overdue tasks and escalating them
        logger.info("Starting workflow escalation trigger");
        
        try {
            // Get overdue tasks directly from repository
            List<WorkflowInstanceTask> overdueTasks = instanceTaskRepository.findOverdueTasks(LocalDateTime.now().minusHours(1));
            
            for (WorkflowInstanceTask task : overdueTasks) {
                if (shouldEscalateTask(task)) {
                    escalateTask(task);
                }
            }
            
            logger.info("Completed workflow escalation trigger for {} overdue tasks", overdueTasks.size());
        } catch (Exception e) {
            logger.error("Error in workflow escalation trigger", e);
        }
    }
    
    private boolean shouldSendReminder(WorkflowInstance instance, WorkflowConfig workflow) {
        if (workflow.getReminderBeforeDueMins() == null) {
            return false;
        }
        
        // Check if reminder is due based on workflow configuration
        LocalDateTime reminderTime = instance.getStartedOn().plusMinutes(workflow.getDueInMins() - workflow.getReminderBeforeDueMins());
        return LocalDateTime.now().isAfter(reminderTime);
    }
    
    private void sendReminderNotification(WorkflowInstance instance, WorkflowConfig workflow) {
        // This would integrate with your notification system
        // For now, just log the reminder
        logger.info("Sending reminder for workflow instance {} (workflow: {})", 
            instance.getInstanceId(), workflow.getName());
        
        // TODO: Integrate with actual notification service
        // notificationService.sendReminder(instance, workflow);
    }
    
    private boolean shouldEscalateTask(WorkflowInstanceTask task) {
        // Check if task has exceeded escalation threshold
        if (task.getWorkflowInstance().getWorkflow().getEscalationAfterMins() == null) {
            return false;
        }
        
        LocalDateTime escalationTime = task.getStartedOn().plusMinutes(
            task.getWorkflowInstance().getWorkflow().getEscalationAfterMins());
        return LocalDateTime.now().isAfter(escalationTime);
    }
    
    private void escalateTask(WorkflowInstanceTask task) {
        // This would integrate with your escalation system
        // For now, just log the escalation
        logger.info("Escalating task {} for instance {}", 
            task.getTask().getName(), task.getWorkflowInstance().getInstanceId());
        
        // TODO: Integrate with actual escalation service
        // escalationService.escalateTask(task);
    }
    
    // Utility Methods
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getOverdueTasks() {
        return instanceTaskRepository.findOverdueTasks(LocalDateTime.now().minusHours(1))
                .stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getTasksNeedingAttention() {
        return instanceTaskRepository.findTasksNeedingAttention(LocalDateTime.now().minusHours(2))
                .stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    // Conversion methods
    private WorkflowInstanceDto convertToInstanceDto(WorkflowInstance instance) {
        WorkflowInstanceDto dto = new WorkflowInstanceDto();
        dto.setInstanceId(instance.getInstanceId());
        dto.setWorkflowId(instance.getWorkflow().getWorkflowId());
        dto.setStatus(instance.getStatus());
        dto.setStartedBy(instance.getStartedBy().getUserId());
        dto.setStartedOn(instance.getStartedOn());
        dto.setCompletedOn(instance.getCompletedOn());
        
        if (instance.getEscalatedTo() != null) {
            dto.setEscalatedTo(instance.getEscalatedTo().getUserId());
        }
        
        // Set calendar information
        if (instance.getCalendar() != null) {
            dto.setCalendarId(instance.getCalendar().getCalendarId());
            dto.setCalendarName(instance.getCalendar().getCalendarName());
        }
        
        dto.setWorkflowName(instance.getWorkflow().getName());
        dto.setStartedByUsername(instance.getStartedBy().getUsername());
        if (instance.getEscalatedTo() != null) {
            dto.setEscalatedToUsername(instance.getEscalatedTo().getUsername());
        }
        
        return dto;
    }
    
    private WorkflowInstanceTaskDto convertToInstanceTaskDto(WorkflowInstanceTask instanceTask) {
        WorkflowInstanceTaskDto dto = new WorkflowInstanceTaskDto();
        dto.setInstanceTaskId(instanceTask.getInstanceTaskId());
        dto.setInstanceId(instanceTask.getWorkflowInstance().getInstanceId());
        dto.setTaskId(instanceTask.getTask().getTaskId());
        dto.setStatus(instanceTask.getStatus());
        dto.setStartedOn(instanceTask.getStartedOn());
        dto.setCompletedOn(instanceTask.getCompletedOn());
        dto.setDecisionOutcome(instanceTask.getDecisionOutcome());
        
        if (instanceTask.getAssignedTo() != null) {
            dto.setAssignedTo(instanceTask.getAssignedTo().getUserId());
            dto.setAssignedToUsername(instanceTask.getAssignedTo().getUsername());
        }
        
        dto.setTaskName(instanceTask.getTask().getName());
        dto.setTaskType(instanceTask.getTask().getTaskType().toString());
        
        return dto;
    }
    
    private TaskInstanceDecisionOutcomeDto convertToDecisionOutcomeDto(TaskInstanceDecisionOutcome outcome) {
        TaskInstanceDecisionOutcomeDto dto = new TaskInstanceDecisionOutcomeDto();
        dto.setOutcomeId(outcome.getOutcomeId());
        dto.setInstanceTaskId(outcome.getInstanceTask().getInstanceTaskId());
        dto.setOutcomeName(outcome.getOutcomeName());
        dto.setCreatedBy(outcome.getCreatedBy());
        dto.setCreatedAt(outcome.getCreatedAt());
        
        if (outcome.getNextInstanceTask() != null) {
            dto.setNextInstanceTaskId(outcome.getNextInstanceTask().getInstanceTaskId());
        }
        if (outcome.getPriorInstanceTask() != null) {
            dto.setPriorInstanceTaskId(outcome.getPriorInstanceTask().getInstanceTaskId());
        }
        
        return dto;
    }
    
    @Override
    public WorkflowProgressDto getWorkflowProgress(Long instanceId) {
        WorkflowInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + instanceId));
        
        List<WorkflowInstanceTask> tasks = instanceTaskRepository.findByWorkflowInstanceInstanceId(instanceId);
        
        WorkflowProgressDto progress = new WorkflowProgressDto(instanceId, instance.getWorkflow().getName());
        progress.setTotalTasks(tasks.size());
        progress.setCompletedTasks((int) tasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.COMPLETED.equals(task.getStatus()))
                .count());
        progress.setPendingTasks((int) tasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.PENDING.equals(task.getStatus()))
                .count());
        progress.setInProgressTasks((int) tasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.IN_PROGRESS.equals(task.getStatus()))
                .count());
        progress.setFailedTasks((int) tasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.FAILED.equals(task.getStatus()))
                .count());
        
        progress.calculateProgress();
        progress.setCurrentStatus(instance.getStatus().toString());
        
        return progress;
    }
    
    @Override
    public WorkflowInstanceStatsDto getWorkflowInstanceStats(Long workflowId) {
        List<WorkflowInstance> instances = instanceRepository.findByWorkflowWorkflowId(workflowId);
        
        WorkflowInstanceStatsDto stats = new WorkflowInstanceStatsDto(workflowId, "Workflow");
        stats.setTotalInstances(instances.size());
        stats.setCompletedInstances((int) instances.stream()
                .filter(instance -> WorkflowInstance.InstanceStatus.COMPLETED.equals(instance.getStatus()))
                .count());
        stats.setInProgressInstances((int) instances.stream()
                .filter(instance -> WorkflowInstance.InstanceStatus.IN_PROGRESS.equals(instance.getStatus()))
                .count());
        stats.setPendingInstances((int) instances.stream()
                .filter(instance -> WorkflowInstance.InstanceStatus.PENDING.equals(instance.getStatus()))
                .count());
        stats.setFailedInstances((int) instances.stream()
                .filter(instance -> WorkflowInstance.InstanceStatus.FAILED.equals(instance.getStatus()))
                .count());
        stats.setCancelledInstances((int) instances.stream()
                .filter(instance -> WorkflowInstance.InstanceStatus.CANCELLED.equals(instance.getStatus()))
                .count());
        
        stats.calculateSuccessRate();
        
        return stats;
    }
    
    @Override
    public UserWorkloadDto getUserWorkload(Long userId) {
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        List<WorkflowInstanceTask> assignedTasks = instanceTaskRepository.findByAssignedToUserId(userId);
        
        UserWorkloadDto workload = new UserWorkloadDto(userId, user.getUsername(), 
                user.getFirstName() + " " + user.getLastName());
        workload.setTotalAssignedTasks(assignedTasks.size());
        workload.setPendingTasks((int) assignedTasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.PENDING.equals(task.getStatus()))
                .count());
        workload.setInProgressTasks((int) assignedTasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.IN_PROGRESS.equals(task.getStatus()))
                .count());
        workload.setCompletedTasks((int) assignedTasks.stream()
                .filter(task -> WorkflowInstanceTask.TaskInstanceStatus.COMPLETED.equals(task.getStatus()))
                .count());
        
        workload.calculateWorkloadPercentage();
        
        return workload;
    }
    
    // Calendar Integration Methods
    @Override
    public WorkflowInstanceDto startWorkflowWithCalendar(Long workflowId, Long startedByUserId, Long calendarId) {
        // Validate calendar exists and date is valid
        if (calendarId != null) {
            LocalDateTime now = LocalDateTime.now();
            if (!calendarService.canExecuteWorkflow(calendarId, now.toLocalDate())) {
                throw new WorkflowException("Workflow cannot execute on this date according to calendar: " + calendarId);
            }
        }
        
        // Start workflow normally
        WorkflowInstanceDto instance = startWorkflow(workflowId, startedByUserId);
        
        // If calendar is provided, associate it with the instance
        if (calendarId != null) {
            // Note: This would require updating the instance with calendar ID
            // For now, we'll just validate the calendar
        }
        
        return instance;
    }
    
    @Override
    public WorkflowInstanceDto startWorkflowWithCalendar(CreateWorkflowInstanceDto createInstanceDto) {
        // Validate workflow exists and is active
        WorkflowConfig workflow = workflowRepository.findById(createInstanceDto.getWorkflowId())
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + createInstanceDto.getWorkflowId()));
        
        if (!"Y".equals(workflow.getIsActive())) {
            throw new WorkflowException("Workflow is not active: " + createInstanceDto.getWorkflowId());
        }
        
        // Validate user exists
        WorkflowUser startedByUser = userRepository.findById(createInstanceDto.getStartedBy())
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + createInstanceDto.getStartedBy()));
        
        // Validate calendar if provided
        WorkflowCalendar calendar = null;
        if (createInstanceDto.getCalendarId() != null) {
            calendar = calendarService.getCalendarById(createInstanceDto.getCalendarId())
                    .map(calendarDto -> {
                        // Convert DTO to entity for association
                        WorkflowCalendar cal = new WorkflowCalendar();
                        cal.setCalendarId(calendarDto.getCalendarId());
                        cal.setCalendarName(calendarDto.getCalendarName());
                        return cal;
                    })
                    .orElseThrow(() -> new WorkflowException("Calendar not found with ID: " + createInstanceDto.getCalendarId()));
            
            // Check if workflow can execute on current date according to calendar
            LocalDateTime now = LocalDateTime.now();
            if (!calendarService.canExecuteWorkflow(createInstanceDto.getCalendarId(), now.toLocalDate())) {
                throw new WorkflowException("Workflow cannot execute on this date according to calendar: " + createInstanceDto.getCalendarId());
            }
        }
        
        // Create workflow instance
        WorkflowInstance instance = new WorkflowInstance();
        instance.setWorkflow(workflow);
        instance.setStatus(WorkflowInstance.InstanceStatus.PENDING);
        instance.setStartedBy(startedByUser);
        instance.setStartedOn(LocalDateTime.now());
        instance.setCalendar(calendar); // Associate calendar with instance
        
        WorkflowInstance savedInstance = instanceRepository.save(instance);
        
        // Create instance tasks for all workflow tasks
        List<WorkflowConfigTask> configTasks = configTaskRepository.findByWorkflowWorkflowIdOrderBySequenceOrder(createInstanceDto.getWorkflowId());
        for (WorkflowConfigTask configTask : configTasks) {
            WorkflowInstanceTask instanceTask = new WorkflowInstanceTask();
            instanceTask.setWorkflowInstance(savedInstance);
            instanceTask.setTask(configTask);
            instanceTask.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
            instanceTask.setStartedOn(LocalDateTime.now());
            
            // Assign first task to the user who started the workflow
            if (configTask.getSequenceOrder() == 1) {
                instanceTask.setAssignedTo(startedByUser);
            }
            
            instanceTaskRepository.save(instanceTask);
        }
        
        return convertToInstanceDto(savedInstance);
    }
    
    @Override
    public boolean canExecuteWorkflowOnDate(Long workflowId, Long calendarId, java.time.LocalDate date) {
        if (calendarId == null) {
            return true; // No calendar restriction
        }
        
        return calendarService.canExecuteWorkflow(calendarId, date);
    }
    
    @Override
    public java.time.LocalDate getNextValidExecutionDate(Long workflowId, Long calendarId, java.time.LocalDate fromDate) {
        if (calendarId == null) {
            return fromDate.plusDays(1); // No calendar restriction
        }
        
        return calendarService.getNextValidDate(calendarId, fromDate);
    }
    
    // Process Owner specific method implementations
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceDto> getWorkflowInstancesByProcessOwner(Long processOwnerId, String status, String priority) {
        WorkflowInstance.InstanceStatus instanceStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                instanceStatus = WorkflowInstance.InstanceStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status, return empty list
                return new ArrayList<>();
            }
        }
        
        List<WorkflowInstance> instances = instanceRepository.findByProcessOwnerRole(processOwnerId, instanceStatus);
        
        // Note: Priority filtering removed as priority field doesn't exist in entities
        // TODO: Add priority field to WorkflowConfig entity if needed
        
        return instances.stream()
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskDto> getTasksByProcessOwner(Long processOwnerId, String status, String priority) {
        WorkflowInstanceTask.TaskInstanceStatus taskStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                taskStatus = WorkflowInstanceTask.TaskInstanceStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status, return empty list
                return new ArrayList<>();
            }
        }
        
        List<WorkflowInstanceTask> tasks = instanceTaskRepository.findTasksByProcessOwnerRole(processOwnerId, taskStatus);
        
        // Note: Priority filtering removed as priority field doesn't exist in entities
        // TODO: Add priority field to WorkflowConfigTask entity if needed
        
        return tasks.stream()
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceDto> getWorkflowInstancesNeedingProcessOwnerAttention(Long processOwnerId) {
        List<WorkflowInstance> instances = instanceRepository.findInstancesNeedingProcessOwnerAttention(processOwnerId);
        return instances.stream()
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowInstanceDto> getOverdueWorkflowsForProcessOwner(Long processOwnerId) {
        // Define overdue threshold (e.g., 7 days)
        LocalDateTime overdueThreshold = LocalDateTime.now().minusDays(7);
        List<WorkflowInstance> instances = instanceRepository.findOverdueInstancesForProcessOwner(processOwnerId, overdueThreshold);
        return instances.stream()
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowInstanceTaskDto reassignTask(Long taskId, Long newUserId, String reason) {
        WorkflowInstanceTask task = instanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
        
        WorkflowUser newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + newUserId));
        
        // Update task assignment
        task.setAssignedTo(newUser);
        // Note: WorkflowInstanceTask doesn't have updatedOn field, removing this line
        
        // Log the reassignment
        logger.info("Task {} reassigned from {} to {} by process owner. Reason: {}", 
            task.getTask().getName(), 
            task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "unassigned",
            newUser.getUsername(), reason);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(task);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public WorkflowInstanceTaskDto overrideTaskDecision(Long taskId, String decision, String reason) {
        WorkflowInstanceTask task = instanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
        
        // Override the decision with process owner authority
        task.setDecisionOutcome("OVERRIDDEN: " + decision + " (Reason: " + reason + ")");
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.COMPLETED);
        task.setCompletedOn(LocalDateTime.now());
        // Note: WorkflowInstanceTask doesn't have updatedOn field, removing this line
        
        logger.info("Task {} decision overridden by process owner. Decision: {}, Reason: {}", 
            task.getTask().getName(), decision, reason);
        
        WorkflowInstanceTask savedTask = instanceTaskRepository.save(task);
        return convertToInstanceTaskDto(savedTask);
    }
    
    @Override
    public List<WorkflowUserDto> getProcessOwnerTeam(Long processOwnerId) {
        // Get users who report to this process owner
        List<WorkflowUser> teamMembers = userRepository.findUsersWhoEscalateTo(
            userRepository.findById(processOwnerId)
                .orElseThrow(() -> new WorkflowException("Process owner not found with ID: " + processOwnerId))
        );
        
        return teamMembers.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowInstanceDto assignWorkflowToProcessOwner(Long workflowId, Long processOwnerId) {
        WorkflowInstance instance = instanceRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + workflowId));
        
        WorkflowUser processOwner = userRepository.findById(processOwnerId)
                .orElseThrow(() -> new WorkflowException("Process owner not found with ID: " + processOwnerId));
        
        // Assign the process owner role to this instance
        // This would typically involve creating a WorkflowInstanceRole entry
        logger.info("Workflow instance {} assigned to process owner {}", workflowId, processOwner.getUsername());
        
        return convertToInstanceDto(instance);
    }
    
    @Override
    public void unassignWorkflowFromProcessOwner(Long workflowId, Long processOwnerId) {
        WorkflowInstance instance = instanceRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found with ID: " + workflowId));
        
        // Remove the process owner role from this instance
        logger.info("Workflow instance {} unassigned from process owner {}", workflowId, processOwnerId);
        
        // This would typically involve removing WorkflowInstanceRole entries
    }
    
    @Override
    public ProcessOwnerWorkloadDto getProcessOwnerWorkload(Long processOwnerId) {
        // Get workload statistics for the process owner
        ProcessOwnerWorkloadDto workload = new ProcessOwnerWorkloadDto();
        workload.setProcessOwnerId(processOwnerId);
        
        // Count active workflows
        long activeWorkflows = instanceRepository.countByStatus(WorkflowInstance.InstanceStatus.IN_PROGRESS);
        workload.setActiveWorkflows((int) activeWorkflows);
        
        // Count pending tasks
        long pendingTasks = instanceTaskRepository.countByStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        workload.setPendingTasks((int) pendingTasks);
        
        // Count overdue tasks
        List<WorkflowInstanceTask> overdueTasks = instanceTaskRepository.findOverdueTasks(LocalDateTime.now().minusHours(1));
        workload.setOverdueTasks(overdueTasks.size());
        
        return workload;
    }
    
    @Override
    public ProcessOwnerPerformanceDto getProcessOwnerPerformance(Long processOwnerId, String period) {
        // Get performance metrics for the process owner
        ProcessOwnerPerformanceDto performance = new ProcessOwnerPerformanceDto();
        performance.setProcessOwnerId(processOwnerId);
        performance.setPeriod(period);
        
        // Calculate completion rate
        long totalTasks = instanceTaskRepository.countByStatus(WorkflowInstanceTask.TaskInstanceStatus.COMPLETED);
        long totalAssignedTasks = instanceTaskRepository.countByStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING) +
                                 instanceTaskRepository.countByStatus(WorkflowInstanceTask.TaskInstanceStatus.IN_PROGRESS) +
                                 totalTasks;
        
        if (totalAssignedTasks > 0) {
            double completionRate = (double) totalTasks / totalAssignedTasks * 100;
            // Note: Using reflection or checking if the field exists
            try {
                performance.getClass().getMethod("setCompletionRate", double.class).invoke(performance, completionRate);
            } catch (Exception e) {
                logger.warn("Could not set completion rate on ProcessOwnerPerformanceDto: {}", e.getMessage());
            }
        } else {
            try {
                performance.getClass().getMethod("setCompletionRate", double.class).invoke(performance, 0.0);
            } catch (Exception e) {
                logger.warn("Could not set completion rate on ProcessOwnerPerformanceDto: {}", e.getMessage());
            }
        }
        
        // Calculate average completion time
        List<WorkflowInstanceTask> completedTasks = instanceTaskRepository.findByStatus(WorkflowInstanceTask.TaskInstanceStatus.COMPLETED);
        if (!completedTasks.isEmpty()) {
            long totalCompletionTime = completedTasks.stream()
                    .filter(task -> task.getStartedOn() != null && task.getCompletedOn() != null)
                    .mapToLong(task -> 
                        java.time.Duration.between(task.getStartedOn(), task.getCompletedOn()).toMinutes())
                    .sum();
            
            double avgCompletionTime = (double) totalCompletionTime / completedTasks.size();
            try {
                performance.getClass().getMethod("setAverageCompletionTimeMinutes", double.class).invoke(performance, avgCompletionTime);
            } catch (Exception e) {
                logger.warn("Could not set average completion time on ProcessOwnerPerformanceDto: {}", e.getMessage());
            }
        } else {
            try {
                performance.getClass().getMethod("setAverageCompletionTimeMinutes", double.class).invoke(performance, 0.0);
            } catch (Exception e) {
                logger.warn("Could not set average completion time on ProcessOwnerPerformanceDto: {}", e.getMessage());
            }
        }
        
        return performance;
    }
    
    private WorkflowUserDto convertToUserDto(WorkflowUser user) {
        WorkflowUserDto dto = new WorkflowUserDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
    
    // Dashboard specific method implementations
    @Override
    @Transactional(readOnly = true)
    public ProcessOwnerDashboardDto getProcessOwnerDashboard(Long processOwnerId) {
        // Get user information
        WorkflowUser user = userRepository.findById(processOwnerId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + processOwnerId));
        
        // Create dashboard DTO
        ProcessOwnerDashboardDto dashboard = new ProcessOwnerDashboardDto(processOwnerId, user.getUsername());
        
        // Get active workflows (PENDING and IN_PROGRESS)
        List<WorkflowInstance> activeWorkflows = instanceRepository.findByProcessOwnerRole(processOwnerId, null);
        activeWorkflows = activeWorkflows.stream()
                .filter(instance -> instance.getStatus() == WorkflowInstance.InstanceStatus.PENDING || 
                                  instance.getStatus() == WorkflowInstance.InstanceStatus.IN_PROGRESS)
                .collect(Collectors.toList());
        
        // Limit to recent workflows for dashboard
        List<WorkflowInstanceDto> activeWorkflowDtos = activeWorkflows.stream()
                .sorted((w1, w2) -> w2.getStartedOn().compareTo(w1.getStartedOn()))
                .limit(10) // Show only 10 most recent active workflows
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
        
        dashboard.setActiveWorkflows(activeWorkflowDtos);
        
        // Get pending tasks
        List<WorkflowInstanceTask> pendingTasks = instanceTaskRepository.findPendingTasksByProcessOwnerForDashboard(processOwnerId);
        List<WorkflowInstanceTaskDto> pendingTaskDtos = pendingTasks.stream()
                .limit(10) // Show only 10 most recent pending tasks
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
        
        dashboard.setPendingTasks(pendingTaskDtos);
        
        // Get escalation queue
        List<EscalationItemDto> escalationQueue = getEscalationQueueForProcessOwner(processOwnerId);
        dashboard.setEscalationQueue(escalationQueue);
        
        // Get statistics
        ProcessOwnerStatsDto stats = getProcessOwnerStatistics(processOwnerId);
        dashboard.setRecentStats(List.of(stats)); // Wrap in list as expected by DTO
        
        // Create dashboard summary
        ProcessOwnerDashboardDto.DashboardSummaryDto summary = new ProcessOwnerDashboardDto.DashboardSummaryDto();
        summary.setTotalWorkflows((int) instanceRepository.countTotalWorkflowsByProcessOwner(processOwnerId));
        summary.setActiveWorkflows((int) instanceRepository.countActiveWorkflowsByProcessOwner(processOwnerId));
        summary.setPendingTasks((int) instanceTaskRepository.countPendingTasksByProcessOwner(processOwnerId));
        summary.setEscalatedItems((int) instanceRepository.countEscalatedWorkflowsByProcessOwner(processOwnerId));
        // Get start and end of current day
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        summary.setCompletedToday((int) instanceRepository.countCompletedWorkflowsTodayByProcessOwner(processOwnerId, startOfDay, endOfDay));
        
        // Calculate completion rate
        long totalWorkflows = instanceRepository.countTotalWorkflowsByProcessOwner(processOwnerId);
        if (totalWorkflows > 0) {
            long completedWorkflows = instanceRepository.countByStatus(WorkflowInstance.InstanceStatus.COMPLETED);
            summary.setCompletionRate((double) completedWorkflows / totalWorkflows * 100);
        } else {
            summary.setCompletionRate(0.0);
        }
        
        dashboard.setSummary(summary);
        
        return dashboard;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EscalationItemDto> getEscalationQueueForProcessOwner(Long processOwnerId) {
        // Get escalated workflows for this process owner
        List<WorkflowInstance> escalatedInstances = instanceRepository.findByProcessOwnerRole(processOwnerId, null);
        escalatedInstances = escalatedInstances.stream()
                .filter(instance -> instance.getEscalatedTo() != null)
                .collect(Collectors.toList());
        
        List<EscalationItemDto> escalationItems = new ArrayList<>();
        
        for (WorkflowInstance instance : escalatedInstances) {
            EscalationItemDto escalationItem = new EscalationItemDto();
            escalationItem.setEscalationId(instance.getInstanceId());
            escalationItem.setEscalationType("WORKFLOW");
            escalationItem.setEntityId(instance.getInstanceId());
            escalationItem.setEntityName(instance.getWorkflow().getName());
            escalationItem.setEscalatedToUserId(instance.getEscalatedTo().getUserId());
            escalationItem.setEscalatedToUsername(instance.getEscalatedTo().getUsername());
            escalationItem.setEscalatedAt(instance.getStartedOn()); // Using startedOn as placeholder
            escalationItem.setStatus("PENDING");
            escalationItem.setPriority("MEDIUM"); // Default priority
            
            escalationItems.add(escalationItem);
        }
        
        return escalationItems;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProcessOwnerStatsDto getProcessOwnerStatistics(Long processOwnerId) {
        // Get user information
        WorkflowUser user = userRepository.findById(processOwnerId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + processOwnerId));
        
        // Create stats DTO
        ProcessOwnerStatsDto stats = new ProcessOwnerStatsDto();
        stats.setProcessOwnerId(processOwnerId);
        stats.setProcessOwnerName(user.getUsername());
        
        // Get counts
        long totalWorkflows = instanceRepository.countTotalWorkflowsByProcessOwner(processOwnerId);
        long activeWorkflows = instanceRepository.countActiveWorkflowsByProcessOwner(processOwnerId);
        long pendingTasks = instanceTaskRepository.countPendingTasksByProcessOwner(processOwnerId);
        long escalatedItems = instanceRepository.countEscalatedWorkflowsByProcessOwner(processOwnerId);
        long completedToday = instanceRepository.countCompletedWorkflowsTodayByProcessOwner(processOwnerId,
            LocalDateTime.now().toLocalDate().atStartOfDay(),
            LocalDateTime.now().toLocalDate().atTime(23, 59, 59));
        
        // Set stats
        stats.setTotalWorkflows((int) totalWorkflows);
        stats.setActiveWorkflows((int) activeWorkflows);
        stats.setPendingTasks((int) pendingTasks);
        stats.setEscalatedWorkflows((int) escalatedItems);
        stats.setCompletedWorkflows((int) completedToday);
        
        // Calculate completion rate
        if (totalWorkflows > 0) {
            long completedWorkflows = instanceRepository.countByStatus(WorkflowInstance.InstanceStatus.COMPLETED);
            stats.setWorkflowCompletionRate((double) completedWorkflows / totalWorkflows * 100);
        } else {
            stats.setWorkflowCompletionRate(0.0);
        }
        
        return stats;
    }
    
    // User Dashboard specific methods
    
    @Override
    @Transactional(readOnly = true)
    public UserDashboardDto getUserDashboard(Long userId) {
        // Get user information
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + userId));
        
        // Create dashboard DTO
        UserDashboardDto dashboard = new UserDashboardDto();
        dashboard.setUserId(userId);
        dashboard.setUsername(user.getUsername());
        
        // Get user's workflows
        List<WorkflowInstance> userWorkflows = instanceRepository.findWorkflowsByUserParticipation(userId, null);
        List<WorkflowInstanceDto> workflowDtos = userWorkflows.stream()
                .sorted((w1, w2) -> w2.getStartedOn().compareTo(w1.getStartedOn()))
                .limit(10) // Show only 10 most recent workflows
                .map(this::convertToInstanceDto)
                .collect(Collectors.toList());
        
        dashboard.setMyWorkflows(workflowDtos);
        
        // Get user's tasks
        List<WorkflowInstanceTask> userTasks = instanceTaskRepository.findRecentTasksByUserForDashboard(userId);
        List<WorkflowInstanceTaskDto> taskDtos = userTasks.stream()
                .limit(10) // Show only 10 most recent tasks
                .map(this::convertToInstanceTaskDto)
                .collect(Collectors.toList());
        
        dashboard.setMyTasks(taskDtos);
        
        // Get user's workload
        UserWorkloadDto workload = getUserWorkload(userId);
        dashboard.setWorkload(workload);
        
        // Set dashboard summary
        UserDashboardDto.DashboardSummaryDto summary = new UserDashboardDto.DashboardSummaryDto();
        summary.setTotalWorkflows((int) instanceRepository.countTotalWorkflowsByUser(userId));
        summary.setActiveWorkflows((int) instanceRepository.countActiveWorkflowsByUser(userId));
        summary.setTotalTasks((int) instanceTaskRepository.countTotalTasksByUser(userId));
        summary.setPendingTasks((int) instanceTaskRepository.countPendingTasksByUser(userId));
        // Get start and end of current day
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        summary.setCompletedTasks((int) instanceTaskRepository.countCompletedTasksTodayByUser(userId, startOfDay, endOfDay));
        
        // Calculate completion rates
        long totalWorkflows = instanceRepository.countTotalWorkflowsByUser(userId);
        if (totalWorkflows > 0) {
            long completedWorkflows = instanceRepository.countByStatus(WorkflowInstance.InstanceStatus.COMPLETED);
            summary.setTaskCompletionRate((double) completedWorkflows / totalWorkflows * 100);
        } else {
            summary.setTaskCompletionRate(0.0);
        }
        
        long totalTasks = instanceTaskRepository.countTotalTasksByUser(userId);
        if (totalTasks > 0) {
            long completedTasks = instanceTaskRepository.countByStatus(WorkflowInstanceTask.TaskInstanceStatus.COMPLETED);
            summary.setTaskCompletionRate((double) completedTasks / totalTasks * 100);
        } else {
            summary.setTaskCompletionRate(0.0);
        }
        
        dashboard.setSummary(summary);
        
        return dashboard;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserActivityDto> getUserActivities(Long userId, Integer limit) {
        // TODO: Implement user activities tracking
        // This would involve tracking user actions like task completions, workflow starts, etc.
        logger.info("Getting user activities for user {} with limit {}", userId, limit);
        
        // Placeholder implementation - return empty list
        return new ArrayList<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserNotificationDto> getUserNotifications(Long userId, String status) {
        // TODO: Implement user notifications
        // This would involve retrieving notifications from a notification system
        logger.info("Getting notifications for user {} with status {}", userId, status);
        
        // Placeholder implementation - return empty list
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public UserNotificationDto markNotificationAsRead(Long notificationId) {
        // TODO: Implement mark notification as read
        // This would involve updating notification status in a notification system
        logger.info("Marking notification {} as read", notificationId);
        
        // Placeholder implementation - return empty DTO
        return new UserNotificationDto();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserCalendarDto getUserCalendar(Long userId, String startDate, String endDate) {
        // TODO: Implement user calendar
        // This would involve retrieving user's calendar information
        logger.info("Getting calendar for user {} from {} to {}", userId, startDate, endDate);
        
        // Placeholder implementation - return empty DTO
        return new UserCalendarDto();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserPerformanceDto getUserPerformance(Long userId, String period) {
        // TODO: Implement user performance metrics
        // This would involve calculating performance metrics based on task completion, time, etc.
        logger.info("Getting performance metrics for user {} for period {}", userId, period);
        
        // Placeholder implementation - return empty DTO
        return new UserPerformanceDto();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowRoleDto> getUserRoles(Long userId) {
        // TODO: Implement user roles
        // This would involve retrieving user's role assignments
        logger.info("Getting roles for user {}", userId);
        
        // Placeholder implementation - return empty list
        return new ArrayList<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserPermissionDto> getUserPermissions(Long userId) {
        // TODO: Implement user permissions
        // This would involve retrieving user's permissions based on roles
        logger.info("Getting permissions for user {}", userId);
        
        // Placeholder implementation - return empty list
        return new ArrayList<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserTeamDto getUserTeam(Long userId) {
        // TODO: Implement user team information
        // This would involve retrieving user's team structure and members
        logger.info("Getting team information for user {}", userId);
        
        // Placeholder implementation - return empty DTO
        return new UserTeamDto();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserPreferencesDto getUserPreferences(Long userId) {
        // TODO: Implement user preferences
        // This would involve retrieving user's preferences and settings
        logger.info("Getting preferences for user {}", userId);
        
        // Placeholder implementation - return empty DTO
        return new UserPreferencesDto();
    }
    
    @Override
    @Transactional
    public UserPreferencesDto updateUserPreferences(Long userId, UserPreferencesDto preferences) {
        // TODO: Implement update user preferences
        // This would involve updating user's preferences and settings
        logger.info("Updating preferences for user {}", userId);
        
        // Placeholder implementation - return the preferences
        return preferences;
    }
    
    // Manager and Admin Dashboard method implementations (placeholder implementations)
    @Override
    @Transactional(readOnly = true)
    public ManagerDashboardDto getManagerDashboard(Long managerId) {
        // TODO: Implement manager dashboard
        // This would involve aggregating data for manager-level view
        logger.info("Getting manager dashboard for manager {}", managerId);
        
        // Placeholder implementation - return empty DTO
        return new ManagerDashboardDto();
    }
    
    @Override
    @Transactional(readOnly = true)
    public AdminDashboardDto getAdminDashboard(Long adminId) {
        // TODO: Implement admin dashboard
        // This would involve aggregating system-wide data for admin view
        logger.info("Getting admin dashboard for admin {}", adminId);
        
        // Placeholder implementation - return empty DTO
        return new AdminDashboardDto();
    }

    @Override
    public List<EscalationItemDto> searchProcessOwnerEscalations(Long processOwnerId, String status, String type,
                                                               String escalatedAfter, String escalatedBefore) {
        logger.debug("Searching process owner escalations for processOwnerId: {}, status: {}, type: {}", 
                    processOwnerId, status, type);
        
        try {
            // Get all workflow instances where the user is the process owner
            List<WorkflowInstance> instances = instanceRepository.findByWorkflowWorkflowIdAndStatusIn(
                processOwnerId, List.of(WorkflowInstance.InstanceStatus.FAILED, WorkflowInstance.InstanceStatus.CANCELLED));
            
            List<EscalationItemDto> escalations = new ArrayList<>();
            
            for (WorkflowInstance instance : instances) {
                // Filter by status if specified
                if (status != null && !status.isEmpty() && !status.equals(instance.getStatus().toString())) {
                    continue;
                }
                
                // Filter by type if specified (escalation type)
                if (type != null && !type.isEmpty()) {
                    // Add type filtering logic here based on your escalation type field
                    // For now, we'll include all escalated instances
                }
                
                // Filter by escalation date if specified
                if (escalatedAfter != null && !escalatedAfter.isEmpty()) {
                    LocalDateTime escalatedAfterDate = LocalDateTime.parse(escalatedAfter);
                    if (instance.getEscalatedTo() != null && instance.getStartedOn().isBefore(escalatedAfterDate)) {
                        continue;
                    }
                }
                
                if (escalatedBefore != null && !escalatedBefore.isEmpty()) {
                    LocalDateTime escalatedBeforeDate = LocalDateTime.parse(escalatedBefore);
                    if (instance.getEscalatedTo() != null && instance.getStartedOn().isAfter(escalatedBeforeDate)) {
                        continue;
                    }
                }
                
                // Create escalation item DTO
                EscalationItemDto escalation = new EscalationItemDto();
                escalation.setEntityId(instance.getInstanceId());
                escalation.setEntityName(instance.getWorkflow().getName());
                escalation.setStatus(instance.getStatus().toString());
                escalation.setEscalatedToUserId(instance.getEscalatedTo() != null ? instance.getEscalatedTo().getUserId() : null);
                escalation.setEscalatedAt(instance.getStartedOn());
                escalation.setEscalationReason("Process owner escalation required");
                
                escalations.add(escalation);
            }
            
            logger.debug("Found {} escalations for process owner {}", escalations.size(), processOwnerId);
            return escalations;
            
        } catch (Exception e) {
            logger.error("Error searching process owner escalations for processOwnerId: {}", processOwnerId, e);
            throw new WorkflowException("Failed to search process owner escalations: " + e.getMessage());
        }
    }

    @Override
    public List<UserNotificationDto> searchUserNotifications(Long userId, String status, String type,
                                                           String createdAfter, String createdBefore) {
        logger.debug("Searching user notifications for userId: {}, status: {}, type: {}", userId, status, type);
        // TODO: Implement user notification search
        return new ArrayList<>();
    }

    @Override
    public Page<WorkflowInstanceDto> searchInstances(Long workflowId, String status, Long startedBy,
                                                    String startedAfter, String startedBefore,
                                                    String completedAfter, String completedBefore,
                                                    Pageable pageable) {
        logger.debug("Searching workflow instances with criteria");
        // TODO: Implement workflow instance search with pagination
        return Page.empty(pageable);
    }

    @Override
    public List<WorkflowInstanceDto> getOverdueInstances(Integer thresholdHours) {
        logger.debug("Getting overdue workflow instances with threshold: {} hours", thresholdHours);
        // TODO: Implement overdue instances retrieval
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowInstanceTaskDto> searchProcessOwnerTasks(Long processOwnerId, String status, String priority, Long assignedTo,
                                                                String startedAfter, String startedBefore,
                                                                String completedAfter, String completedBefore) {
        logger.debug("Searching process owner tasks for processOwnerId: {}", processOwnerId);
        // TODO: Implement process owner task search
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowUserDto> searchProcessOwnerTeam(Long processOwnerId, String username, String firstName, String lastName,
                                                        String isActive, String roleName) {
        logger.debug("Searching process owner team for processOwnerId: {}", processOwnerId);
        // TODO: Implement process owner team search
        return new ArrayList<>();
    }

    @Override
    public Page<WorkflowInstanceTaskDto> searchTasks(Long instanceId, String status, Long assignedTo,
                                                     String startedAfter, String startedBefore,
                                                     String completedAfter, String completedBefore,
                                                     Pageable pageable) {
        logger.debug("Searching workflow tasks with criteria");
        // TODO: Implement workflow task search with pagination
        return Page.empty(pageable);
    }

    @Override
    public List<WorkflowInstanceDto> searchProcessOwnerWorkflows(Long processOwnerId, String status,
                                                                String startedAfter, String startedBefore,
                                                                String completedAfter, String completedBefore) {
        logger.debug("Searching process owner workflows for processOwnerId: {}", processOwnerId);
        // TODO: Implement process owner workflow search
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowInstanceTaskDto> searchUserTasks(Long userId, String status, String priority,
                                                        String startedAfter, String startedBefore,
                                                        String completedAfter, String completedBefore) {
        logger.debug("Searching user tasks for userId: {}", userId);
        // TODO: Implement user task search
        return new ArrayList<>();
    }

    @Override
    public List<UserActivityDto> searchUserActivities(Long userId, String activityType,
                                                     String startedAfter, String startedBefore, Integer limit) {
        logger.debug("Searching user activities for userId: {}", userId);
        // TODO: Implement user activity search
        return new ArrayList<>();
    }

    @Override
    public List<WorkflowInstanceDto> searchUserWorkflows(Long userId, String status,
                                                        String startedAfter, String startedBefore,
                                                        String completedAfter, String completedBefore) {
        logger.debug("Searching user workflows for userId: {}", userId);
        // TODO: Implement user workflow search
        return new ArrayList<>();
    }
}
