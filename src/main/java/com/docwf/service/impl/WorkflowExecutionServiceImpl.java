package com.docwf.service.impl;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.dto.WorkflowProgressDto;
import com.docwf.dto.WorkflowInstanceStatsDto;
import com.docwf.dto.UserWorkloadDto;
import com.docwf.entity.*;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.*;
import com.docwf.service.WorkflowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {
    
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
            // TODO: Add proper logging
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
        
        // Auto-assign task if not assigned
        if (nextTask.getAssignedTo() == null) {
            // Find users with the required role for this task
            Long taskId = nextTask.getTask().getTaskId();
            WorkflowConfigTask configTask = configTaskRepository.findById(taskId)
                    .orElseThrow(() -> new WorkflowException("Config task not found with ID: " + taskId));
            
            if (configTask.getRole() != null) {
                // Find users with this role in the workflow
                // TODO: Implement role-based assignment logic
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
        // TODO: Implement reminder logic
        // This would involve checking workflows that need reminders and sending notifications
    }
    
    @Override
    public void triggerWorkflowEscalations() {
        // TODO: Implement escalation logic
        // This would involve checking overdue tasks and escalating them
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
}
