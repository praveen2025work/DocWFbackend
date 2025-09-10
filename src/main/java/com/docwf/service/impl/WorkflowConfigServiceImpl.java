package com.docwf.service.impl;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigTaskFileDto;
import com.docwf.dto.WorkflowConfigTaskFileDependencyDto;
import com.docwf.dto.TaskDecisionOutcomeDto;
import com.docwf.dto.WorkflowConfigParamDto;
import com.docwf.dto.WorkflowRoleDto;
import com.docwf.entity.*;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.*;
import com.docwf.service.WorkflowConfigService;
import com.docwf.service.WorkflowConfigTaskFileDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

@Service
@Transactional
public class WorkflowConfigServiceImpl implements WorkflowConfigService {
    
    @Autowired
    private WorkflowConfigRepository workflowRepository;
    
    @Autowired
    private WorkflowConfigRoleRepository configRoleRepository;
    
    @Autowired
    private WorkflowConfigTaskRepository configTaskRepository;
    
    @Autowired
    private WorkflowConfigParamRepository configParamRepository;
    
    @Autowired
    private WorkflowRoleRepository roleRepository;
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    
    @Autowired
    private WorkflowConfigTaskFileDependencyService dependencyService;
    
    @Autowired
    private WorkflowConfigTaskFileRepository fileRepository;
    
    @Autowired
    private TaskDecisionOutcomeRepository taskDecisionOutcomeRepository;
    
    @Override
    public WorkflowConfigDto createWorkflow(WorkflowConfigDto workflowDto) {
        // Validate unique name
        if (workflowRepository.existsByName(workflowDto.getName())) {
            throw new WorkflowException("Workflow name already exists: " + workflowDto.getName());
        }
        
        // Check if this is a complex workflow with sequence-based mapping
        if (isComplexWorkflow(workflowDto)) {
            return createComplexWorkflowInternal(workflowDto);
        }
        
        // Create simple workflow entity
        WorkflowConfig workflow = new WorkflowConfig();
        workflow.setName(workflowDto.getName());
        workflow.setDescription(workflowDto.getDescription());
        workflow.setReminderBeforeDueMins(workflowDto.getReminderBeforeDueMins());
        workflow.setMinutesAfterDue(workflowDto.getMinutesAfterDue());
        workflow.setEscalationAfterMins(workflowDto.getEscalationAfterMins());
        workflow.setDueInMins(workflowDto.getDueInMins());
        workflow.setIsActive(workflowDto.getIsActive() != null ? workflowDto.getIsActive() : "Y");
        workflow.setCreatedBy(workflowDto.getCreatedBy() != null ? workflowDto.getCreatedBy() : "system");
        
        WorkflowConfig savedWorkflow = workflowRepository.save(workflow);
        return convertToDto(savedWorkflow);
    }
    
    private boolean isComplexWorkflow(WorkflowConfigDto workflowDto) {
        // Check if workflow has roles with sequence mapping or tasks with sequence mapping
        return (workflowDto.getRoles() != null && !workflowDto.getRoles().isEmpty()) ||
               (workflowDto.getTasks() != null && workflowDto.getTasks().stream()
                   .anyMatch(task -> task.getTaskSequence() != null || task.getRoleSequence() != null));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowConfigDto> getWorkflowById(Long workflowId) {
        return workflowRepository.findById(workflowId).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowConfigDto> getWorkflowByName(String name) {
        return workflowRepository.findByName(name).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigDto> getAllActiveWorkflows() {
        return workflowRepository.findByIsActive("Y")
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowConfigDto> getAllWorkflows(String isActive, Pageable pageable) {
        Page<WorkflowConfig> workflows;
        if (isActive != null && !isActive.isEmpty()) {
            workflows = workflowRepository.findByIsActive(isActive, pageable);
        } else {
            workflows = workflowRepository.findAll(pageable);
        }
        return workflows.map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowConfigDto> searchWorkflows(String name, String description, String isActive, 
                                                 String createdBy, Integer minDueTime, Integer maxDueTime,
                                                 String createdAfter, String createdBefore, Pageable pageable) {
        // Use repository method for better performance
        java.time.LocalDateTime afterDate = null;
        java.time.LocalDateTime beforeDate = null;
        
        try {
            if (createdAfter != null) {
                afterDate = java.time.LocalDateTime.parse(createdAfter);
            }
            if (createdBefore != null) {
                beforeDate = java.time.LocalDateTime.parse(createdBefore);
            }
        } catch (Exception e) {
            // If date parsing fails, return empty results
            return Page.empty(pageable);
        }
        
        Page<WorkflowConfig> workflows = workflowRepository.searchWorkflows(
                name, description, isActive, createdBy, minDueTime, maxDueTime, afterDate, beforeDate, pageable);
        
        return workflows.map(this::convertToDto);
    }
    
    @Override
    public WorkflowConfigDto updateWorkflow(Long workflowId, WorkflowConfigDto workflowDto) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        // Update fields
        if (workflowDto.getName() != null) {
            // Check if new name is unique (excluding current workflow)
            Optional<WorkflowConfig> existingWorkflow = workflowRepository.findByName(workflowDto.getName());
            if (existingWorkflow.isPresent() && !existingWorkflow.get().getWorkflowId().equals(workflowId)) {
                throw new WorkflowException("Workflow name already exists: " + workflowDto.getName());
            }
            workflow.setName(workflowDto.getName());
        }
        if (workflowDto.getDescription() != null) {
            workflow.setDescription(workflowDto.getDescription());
        }
        if (workflowDto.getReminderBeforeDueMins() != null) {
            workflow.setReminderBeforeDueMins(workflowDto.getReminderBeforeDueMins());
        }
        if (workflowDto.getMinutesAfterDue() != null) {
            workflow.setMinutesAfterDue(workflowDto.getMinutesAfterDue());
        }
        if (workflowDto.getEscalationAfterMins() != null) {
            workflow.setEscalationAfterMins(workflowDto.getEscalationAfterMins());
        }
        if (workflowDto.getDueInMins() != null) {
            workflow.setDueInMins(workflowDto.getDueInMins());
        }
        
        workflow.setUpdatedBy(workflowDto.getUpdatedBy() != null ? workflowDto.getUpdatedBy() : "system");
        workflow.setUpdatedOn(LocalDateTime.now());
        
        WorkflowConfig savedWorkflow = workflowRepository.save(workflow);
        return convertToDto(savedWorkflow);
    }
    
    @Override
    public void deleteWorkflow(Long workflowId) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        // Check if workflow has running instances
        // Add check for running instances
        List<WorkflowInstance> runningInstances = workflowInstanceRepository.findByWorkflowWorkflowIdAndStatusIn(
                workflowId, Arrays.asList(WorkflowInstance.InstanceStatus.IN_PROGRESS, 
                                      WorkflowInstance.InstanceStatus.PENDING));
        
        if (!runningInstances.isEmpty()) {
            throw new WorkflowException("Cannot delete workflow. There are " + runningInstances.size() + " running instances.");
        }
        
        workflowRepository.delete(workflow);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigDto> getWorkflowsByUserId(Long userId) {
        return workflowRepository.findWorkflowsByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigDto> getWorkflowsByRoleId(Long roleId) {
        // TODO: Implement role-based workflow filtering
        return new ArrayList<>();
    }
    
    @Override
    public List<WorkflowConfigDto> getWorkflowsNeedingReminders() {
        List<WorkflowConfig> workflows = workflowRepository.findWorkflowsNeedingReminders();
        return workflows.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    

    
    @Override
    public List<WorkflowConfig> getActiveWorkflowEntitiesForExecution() {
        return workflowRepository.findActiveWorkflowsForExecution();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfig> getWorkflowsByCalendarId(Long calendarId) {
        // Get workflows assigned to a specific calendar
        return workflowRepository.findByCalendarIdAndIsActive(calendarId, "Y");
    }
    
    // Additional methods for test compatibility
    @Override
    public List<WorkflowConfigDto> getWorkflowsByStatus(String status) {
        List<WorkflowConfig> workflows = workflowRepository.findByIsActive(status);
        return workflows.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public Page<WorkflowConfigDto> getWorkflowConfigs(Pageable pageable) {
        return getAllWorkflows(null, pageable);
    }
    
    @Override
    public List<WorkflowConfigDto> getWorkflowConfigsByStatus(String status) {
        return getWorkflowsByStatus(status);
    }
    
    @Override
    public void deleteWorkflowConfig(Long workflowId) {
        deleteWorkflow(workflowId);
    }
    
    @Override
    public WorkflowConfigDto createWorkflowConfig(WorkflowConfigDto workflowDto) {
        return createWorkflow(workflowDto);
    }
    
    @Override
    public WorkflowConfigDto updateWorkflowConfig(Long workflowId, WorkflowConfigDto workflowDto) {
        return updateWorkflow(workflowId, workflowDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean workflowNameExists(String name) {
        return workflowRepository.existsByName(name);
    }
    
    @Override
    public WorkflowConfigDto toggleWorkflowStatus(Long workflowId, String isActive) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        workflow.setIsActive(isActive);
        workflow.setUpdatedBy("system");
        workflow.setUpdatedOn(LocalDateTime.now());
        
        WorkflowConfig savedWorkflow = workflowRepository.save(workflow);
        return convertToDto(savedWorkflow);
    }
    
    // Workflow Role Management
    @Override
    public WorkflowConfigRoleDto assignRoleToWorkflow(Long workflowId, WorkflowConfigRoleDto roleDto) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        WorkflowRole role = roleRepository.findById(roleDto.getRoleId())
                .orElseThrow(() -> new WorkflowException("Role not found with ID: " + roleDto.getRoleId()));
        
        WorkflowUser user = userRepository.findById(roleDto.getUserId())
                .orElseThrow(() -> new WorkflowException("User not found with ID: " + roleDto.getUserId()));
        
        WorkflowConfigRole configRole = new WorkflowConfigRole();
        configRole.setWorkflow(workflow);
        configRole.setRole(role);
        configRole.setUser(user);
        configRole.setIsActive(roleDto.getIsActive() != null ? roleDto.getIsActive() : "Y");
        
        WorkflowConfigRole savedConfigRole = configRoleRepository.save(configRole);
        return convertToRoleDto(savedConfigRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigRoleDto> getWorkflowRoles(Long workflowId) {
        return configRoleRepository.findByWorkflowWorkflowId(workflowId)
                .stream()
                .map(this::convertToRoleDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowConfigRoleDto updateWorkflowRole(Long workflowId, Long configRoleId, WorkflowConfigRoleDto roleDto) {
        WorkflowConfigRole configRole = configRoleRepository.findById(configRoleId)
                .orElseThrow(() -> new WorkflowException("Workflow config role not found with ID: " + configRoleId));
        
        if (roleDto.getIsActive() != null) {
            configRole.setIsActive(roleDto.getIsActive());
        }
        
        WorkflowConfigRole savedConfigRole = configRoleRepository.save(configRole);
        return convertToRoleDto(savedConfigRole);
    }
    
    @Override
    public void removeRoleFromWorkflow(Long workflowId, Long configRoleId) {
        WorkflowConfigRole configRole = configRoleRepository.findById(configRoleId)
                .orElseThrow(() -> new WorkflowException("Workflow config role not found with ID: " + configRoleId));
        
        configRoleRepository.delete(configRole);
    }
    
    // Workflow Task Management
    @Override
    public WorkflowConfigTaskDto addTask(Long workflowId, WorkflowConfigTaskDto taskDto) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        WorkflowConfigTask task = new WorkflowConfigTask();
        task.setWorkflow(workflow);
        task.setName(taskDto.getName());
        task.setTaskType(taskDto.getTaskType());
        task.setSequenceOrder(taskDto.getSequenceOrder());
        task.setExpectedCompletion(taskDto.getExpectedCompletion());
        task.setEscalationRules(taskDto.getEscalationRules());
        
        if (taskDto.getRoleId() != null) {
            WorkflowRole role = roleRepository.findById(taskDto.getRoleId())
                    .orElseThrow(() -> new WorkflowException("Role not found with ID: " + taskDto.getRoleId()));
            task.setRole(role);
        }
        
        WorkflowConfigTask savedTask = configTaskRepository.save(task);
        return convertToTaskDto(savedTask);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskDto> getWorkflowTasks(Long workflowId) {
        return configTaskRepository.findByWorkflowWorkflowIdOrderBySequenceOrder(workflowId)
                .stream()
                .map(this::convertToTaskDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowConfigTaskDto updateTask(Long workflowId, Long taskId, WorkflowConfigTaskDto taskDto) {
        WorkflowConfigTask task = configTaskRepository.findById(taskId)
                .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
        
        if (taskDto.getName() != null) {
            task.setName(taskDto.getName());
        }
        if (taskDto.getTaskType() != null) {
            task.setTaskType(taskDto.getTaskType());
        }
        if (taskDto.getSequenceOrder() != null) {
            task.setSequenceOrder(taskDto.getSequenceOrder());
        }
        if (taskDto.getExpectedCompletion() != null) {
            task.setExpectedCompletion(taskDto.getExpectedCompletion());
        }
        if (taskDto.getEscalationRules() != null) {
            task.setEscalationRules(taskDto.getEscalationRules());
        }
        
        if (taskDto.getRoleId() != null) {
            WorkflowRole role = roleRepository.findById(taskDto.getRoleId())
                    .orElseThrow(() -> new WorkflowException("Role not found with ID: " + taskDto.getRoleId()));
            task.setRole(role);
        }
        
        WorkflowConfigTask savedTask = configTaskRepository.save(task);
        return convertToTaskDto(savedTask);
    }
    
    @Override
    public void deleteTask(Long workflowId, Long taskId) {
        WorkflowConfigTask task = configTaskRepository.findById(taskId)
                .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
        
        configTaskRepository.delete(task);
    }
    
    @Override
    public List<WorkflowConfigTaskDto> reorderTasks(Long workflowId, List<Long> taskIds) {
        // Validate all tasks belong to the workflow
        List<WorkflowConfigTask> tasks = configTaskRepository.findByWorkflowWorkflowId(workflowId);
        if (tasks.size() != taskIds.size()) {
            throw new WorkflowException("Invalid task count for reordering");
        }
        
        // Update sequence order
        for (int i = 0; i < taskIds.size(); i++) {
            Long taskId = taskIds.get(i);
            WorkflowConfigTask task = tasks.stream()
                    .filter(t -> t.getTaskId().equals(taskId))
                    .findFirst()
                    .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
            
            task.setSequenceOrder(i + 1);
            configTaskRepository.save(task);
        }
        
        return getWorkflowTasks(workflowId);
    }
    
    // Workflow Parameter Management
    @Override
    public WorkflowConfigParamDto addParameter(Long workflowId, WorkflowConfigParamDto paramDto) {
        WorkflowConfig workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow not found with ID: " + workflowId));
        
        WorkflowConfigParam param = new WorkflowConfigParam();
        param.setWorkflow(workflow);
        param.setParamKey(paramDto.getParamKey());
        param.setParamValue(paramDto.getParamValue());
        param.setCreatedBy(paramDto.getCreatedBy() != null ? paramDto.getCreatedBy() : "system");
        
        WorkflowConfigParam savedParam = configParamRepository.save(param);
        return convertToParamDto(savedParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigParamDto> getWorkflowParameters(Long workflowId) {
        return configParamRepository.findByWorkflowWorkflowId(workflowId)
                .stream()
                .map(this::convertToParamDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowConfigParamDto updateParameter(Long workflowId, Long paramId, WorkflowConfigParamDto paramDto) {
        WorkflowConfigParam param = configParamRepository.findById(paramId)
                .orElseThrow(() -> new WorkflowException("Parameter not found with ID: " + paramId));
        
        if (paramDto.getParamValue() != null) {
            param.setParamValue(paramDto.getParamValue());
        }
        
        param.setUpdatedBy(paramDto.getUpdatedBy() != null ? paramDto.getUpdatedBy() : "system");
        param.setUpdatedOn(LocalDateTime.now());
        
        WorkflowConfigParam savedParam = configParamRepository.save(param);
        return convertToParamDto(savedParam);
    }
    
    @Override
    public void deleteParameter(Long workflowId, Long paramId) {
        WorkflowConfigParam param = configParamRepository.findById(paramId)
                .orElseThrow(() -> new WorkflowException("Parameter not found with ID: " + paramId));
        
        configParamRepository.delete(param);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String getParameterValue(Long workflowId, String paramKey) {
        Optional<WorkflowConfigParam> param = configParamRepository.findByWorkflowWorkflowIdAndParamKey(workflowId, paramKey);
        return param.map(WorkflowConfigParam::getParamValue).orElse(null);
    }
    
    // Conversion methods
    private WorkflowConfigDto convertToDto(WorkflowConfig workflow) {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setWorkflowId(workflow.getWorkflowId());
        dto.setName(workflow.getName());
        dto.setDescription(workflow.getDescription());
        dto.setReminderBeforeDueMins(workflow.getReminderBeforeDueMins());
        dto.setMinutesAfterDue(workflow.getMinutesAfterDue());
        dto.setEscalationAfterMins(workflow.getEscalationAfterMins());
        dto.setDueInMins(workflow.getDueInMins());
        dto.setIsActive(workflow.getIsActive());
        dto.setCreatedBy(workflow.getCreatedBy());
        dto.setCreatedOn(workflow.getCreatedOn());
        dto.setUpdatedBy(workflow.getUpdatedBy());
        dto.setUpdatedOn(workflow.getUpdatedOn());
        return dto;
    }
    
    private WorkflowConfigRoleDto convertToRoleDto(WorkflowConfigRole configRole) {
        WorkflowConfigRoleDto dto = new WorkflowConfigRoleDto();
        dto.setId(configRole.getId());
        dto.setWorkflowId(configRole.getWorkflow().getWorkflowId());
        dto.setRoleId(configRole.getRole().getRoleId());
        dto.setUserId(configRole.getUser().getUserId());
        dto.setIsActive(configRole.getIsActive());
        dto.setRoleName(configRole.getRole().getRoleName());
        dto.setUserName(configRole.getUser().getUsername());
        return dto;
    }
    
    private WorkflowConfigTaskDto convertToTaskDto(WorkflowConfigTask task) {
        WorkflowConfigTaskDto dto = new WorkflowConfigTaskDto();
        dto.setTaskId(task.getTaskId());
        dto.setWorkflowId(task.getWorkflow().getWorkflowId());
        dto.setName(task.getName());
        dto.setTaskType(task.getTaskType());
        dto.setSequenceOrder(task.getSequenceOrder());
        dto.setExpectedCompletion(task.getExpectedCompletion());
        dto.setEscalationRules(task.getEscalationRules());
        
        if (task.getRole() != null) {
            dto.setRoleId(task.getRole().getRoleId());
            dto.setRoleName(task.getRole().getRoleName());
        }
        
        return dto;
    }
    
    private WorkflowConfigParamDto convertToParamDto(WorkflowConfigParam param) {
        WorkflowConfigParamDto dto = new WorkflowConfigParamDto();
        dto.setParamId(param.getParamId());
        dto.setWorkflowId(param.getWorkflow().getWorkflowId());
        dto.setParamKey(param.getParamKey());
        dto.setParamValue(param.getParamValue());
        dto.setCreatedBy(param.getCreatedBy());
        dto.setCreatedOn(param.getCreatedOn());
        dto.setUpdatedBy(param.getUpdatedBy());
        dto.setUpdatedOn(param.getUpdatedOn());
        return dto;
    }
    
    // Enhanced Workflow Creation with Sequence-based Mapping
    @Override
    public WorkflowConfigDto createWorkflowWithSequenceMapping(WorkflowConfigDto workflowDto) {
        // First create the workflow
        WorkflowConfigDto createdWorkflow = createWorkflow(workflowDto);
        
        if (workflowDto.getTasks() != null && !workflowDto.getTasks().isEmpty()) {
            // Create tasks with sequence mapping
            List<Long> taskIds = new ArrayList<>();
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                taskDto.setWorkflowId(createdWorkflow.getWorkflowId());
                WorkflowConfigTaskDto createdTask = addTask(createdWorkflow.getWorkflowId(), taskDto);
                taskIds.add(createdTask.getTaskId());
                
                // Create files for this task with sequence mapping
                if (taskDto.getTaskFiles() != null && !taskDto.getTaskFiles().isEmpty()) {
                    List<Long> fileIds = new ArrayList<>();
                    for (WorkflowConfigTaskFileDto fileDto : taskDto.getTaskFiles()) {
                        fileDto.setTaskId(createdTask.getTaskId());
                        WorkflowConfigTaskFileDto createdFile = addFileToTask(createdTask.getTaskId(), fileDto);
                        fileIds.add(createdFile.getFileId());
                    }
                    
                    // Create file dependencies using sequence mapping
                    if (taskDto.getTaskFiles() != null && !taskDto.getTaskFiles().isEmpty()) {
                        for (WorkflowConfigTaskFileDto fileDto : taskDto.getTaskFiles()) {
                            if (fileDto.getDependencies() != null && !fileDto.getDependencies().isEmpty()) {
                                dependencyService.createDependenciesWithSequenceMapping(
                                    fileDto.getDependencies(), fileIds);
                            }
                        }
                    }
                    
                    // Create decision outcomes
                    if (taskDto.getDecisionOutcomes() != null && !taskDto.getDecisionOutcomes().isEmpty()) {
                        for (TaskDecisionOutcomeDto outcomeDto : taskDto.getDecisionOutcomes()) {
                            TaskDecisionOutcome outcome = new TaskDecisionOutcome();
                            outcome.setTask(configTaskRepository.findById(createdTask.getTaskId()).orElse(null));
                            outcome.setOutcomeName(outcomeDto.getOutcomeName());
                            outcome.setRevisionType(outcomeDto.getRevisionType());
                            outcome.setRevisionTaskIds(outcomeDto.getRevisionTaskIds());
                            outcome.setRevisionStrategy(outcomeDto.getRevisionStrategy());
                            outcome.setRevisionPriority(outcomeDto.getRevisionPriority());
                            outcome.setRevisionConditions(outcomeDto.getRevisionConditions());
                            outcome.setAutoEscalate(outcomeDto.getAutoEscalate());
                            outcome.setEscalationRoleId(outcomeDto.getEscalationRoleId());
                            outcome.setCreatedBy(workflowDto.getCreatedBy() != null ? workflowDto.getCreatedBy() : "system");
                            
                            taskDecisionOutcomeRepository.save(outcome);
                        }
                    }
                }
            }
            
            // Create task dependencies using sequence mapping
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getParentTaskSequences() != null && !taskDto.getParentTaskSequences().isEmpty()) {
                    // Map parent task sequences to actual task IDs
                    for (Integer parentSequence : taskDto.getParentTaskSequences()) {
                        if (parentSequence > 0 && parentSequence <= taskIds.size()) {
                            Long parentTaskId = taskIds.get(parentSequence - 1);
                            // Update the task with parent task ID
                            String currentParentIds = taskDto.getParentTaskIds();
                            String newParentIds = currentParentIds == null ? 
                                parentTaskId.toString() : currentParentIds + "," + parentTaskId;
                            taskDto.setParentTaskIds(newParentIds);
                        }
                    }
                    updateTask(createdWorkflow.getWorkflowId(), taskDto.getTaskId(), taskDto);
                }
            }
        }
        
        return getWorkflowById(createdWorkflow.getWorkflowId()).orElse(createdWorkflow);
    }
    
    @Override
    public WorkflowConfigDto updateWorkflowWithSequenceMapping(Long workflowId, WorkflowConfigDto workflowDto) {
        // Update the workflow itself
        WorkflowConfigDto updatedWorkflow = updateWorkflow(workflowId, workflowDto);
        
        if (workflowDto.getTasks() != null && !workflowDto.getTasks().isEmpty()) {
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskId() != null) {
                    // Update existing task
                    updateTask(workflowId, taskDto.getTaskId(), taskDto);
                    
                    // Update task files
                    if (taskDto.getTaskFiles() != null && !taskDto.getTaskFiles().isEmpty()) {
                        for (WorkflowConfigTaskFileDto fileDto : taskDto.getTaskFiles()) {
                            if (fileDto.getFileId() != null) {
                                updateTaskFile(taskDto.getTaskId(), fileDto.getFileId(), fileDto);
                            } else {
                                addFileToTask(taskDto.getTaskId(), fileDto);
                            }
                        }
                    }
                } else {
                    // Create new task
                    taskDto.setWorkflowId(workflowId);
                    addTask(workflowId, taskDto);
                }
            }
        }
        
        return getWorkflowById(workflowId).orElse(updatedWorkflow);
    }
    
    // Task File Management
    @Override
    public WorkflowConfigTaskFileDto addFileToTask(Long taskId, WorkflowConfigTaskFileDto fileDto) {
        WorkflowConfigTask task = configTaskRepository.findById(taskId)
                .orElseThrow(() -> new WorkflowException("Task not found with ID: " + taskId));
        
        WorkflowConfigTaskFile file = new WorkflowConfigTaskFile();
        file.setTask(task);
        file.setFileName(fileDto.getFileName());
        file.setFilePath(fileDto.getFilePath());
        file.setFileLocation(fileDto.getFileLocation());
        file.setFileTypeRegex(fileDto.getFileTypeRegex());
        file.setActionType(fileDto.getActionType());
        file.setFileDescription(fileDto.getFileDescription());
        file.setIsRequired(fileDto.getIsRequired() != null ? fileDto.getIsRequired() : "N");
        file.setFileStatus(fileDto.getFileStatus() != null ? fileDto.getFileStatus() : "PENDING");
        file.setKeepFileVersions(fileDto.getKeepFileVersions() != null ? fileDto.getKeepFileVersions() : "Y");
        file.setRetainForCurrentPeriod(fileDto.getRetainForCurrentPeriod() != null ? fileDto.getRetainForCurrentPeriod() : "Y");
        file.setFileCommentary(fileDto.getFileCommentary());
        file.setCreatedBy(fileDto.getCreatedBy() != null ? fileDto.getCreatedBy() : "system");
        
        WorkflowConfigTaskFile savedFile = fileRepository.save(file);
        return convertToFileDto(savedFile);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDto> getTaskFiles(Long taskId) {
        return fileRepository.findByTaskTaskId(taskId)
                .stream()
                .map(this::convertToFileDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowConfigTaskFileDto updateTaskFile(Long taskId, Long fileId, WorkflowConfigTaskFileDto fileDto) {
        WorkflowConfigTaskFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new WorkflowException("File not found with ID: " + fileId));
        
        if (fileDto.getFileName() != null) {
            file.setFileName(fileDto.getFileName());
        }
        if (fileDto.getFilePath() != null) {
            file.setFilePath(fileDto.getFilePath());
        }
        if (fileDto.getFileLocation() != null) {
            file.setFileLocation(fileDto.getFileLocation());
        }
        if (fileDto.getFileTypeRegex() != null) {
            file.setFileTypeRegex(fileDto.getFileTypeRegex());
        }
        if (fileDto.getActionType() != null) {
            file.setActionType(fileDto.getActionType());
        }
        if (fileDto.getFileDescription() != null) {
            file.setFileDescription(fileDto.getFileDescription());
        }
        if (fileDto.getIsRequired() != null) {
            file.setIsRequired(fileDto.getIsRequired());
        }
        if (fileDto.getFileStatus() != null) {
            file.setFileStatus(fileDto.getFileStatus());
        }
        if (fileDto.getKeepFileVersions() != null) {
            file.setKeepFileVersions(fileDto.getKeepFileVersions());
        }
        if (fileDto.getRetainForCurrentPeriod() != null) {
            file.setRetainForCurrentPeriod(fileDto.getRetainForCurrentPeriod());
        }
        if (fileDto.getFileCommentary() != null) {
            file.setFileCommentary(fileDto.getFileCommentary());
        }
        if (fileDto.getUpdatedBy() != null) {
            file.setUpdatedBy(fileDto.getUpdatedBy());
        }
        
        WorkflowConfigTaskFile savedFile = fileRepository.save(file);
        return convertToFileDto(savedFile);
    }
    
    @Override
    public void deleteTaskFile(Long taskId, Long fileId) {
        WorkflowConfigTaskFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new WorkflowException("File not found with ID: " + fileId));
        
        // Delete all dependencies for this file
        dependencyService.deleteDependenciesByFileId(fileId);
        dependencyService.deleteDependenciesByParentFileId(fileId);
        
        fileRepository.delete(file);
    }
    
    // File Dependency Management
    @Override
    public WorkflowConfigTaskFileDependencyDto addFileDependency(WorkflowConfigTaskFileDependencyDto dependencyDto) {
        return dependencyService.createDependency(dependencyDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDependencyDto> getFileDependencies(Long fileId) {
        return dependencyService.getDependenciesByFileId(fileId);
    }
    
    @Override
    public WorkflowConfigTaskFileDependencyDto updateFileDependency(Long dependencyId, WorkflowConfigTaskFileDependencyDto dependencyDto) {
        return dependencyService.updateDependency(dependencyId, dependencyDto);
    }
    
    @Override
    public void deleteFileDependency(Long dependencyId) {
        dependencyService.deleteDependency(dependencyId);
    }
    
    private WorkflowConfigTaskFileDto convertToFileDto(WorkflowConfigTaskFile file) {
        WorkflowConfigTaskFileDto dto = new WorkflowConfigTaskFileDto();
        dto.setFileId(file.getFileId());
        dto.setTaskId(file.getTask().getTaskId());
        dto.setFileName(file.getFileName());
        dto.setFilePath(file.getFilePath());
        dto.setFileLocation(file.getFileLocation());
        dto.setFileTypeRegex(file.getFileTypeRegex());
        dto.setActionType(file.getActionType());
        dto.setFileDescription(file.getFileDescription());
        dto.setIsRequired(file.getIsRequired());
        dto.setFileStatus(file.getFileStatus());
        dto.setKeepFileVersions(file.getKeepFileVersions());
        dto.setRetainForCurrentPeriod(file.getRetainForCurrentPeriod());
        dto.setFileCommentary(file.getFileCommentary());
        dto.setCreatedBy(file.getCreatedBy());
        dto.setCreatedOn(file.getCreatedOn());
        dto.setUpdatedBy(file.getUpdatedBy());
        dto.setUpdatedOn(file.getUpdatedOn());
        return dto;
    }
    
    private WorkflowConfigDto createComplexWorkflowInternal(WorkflowConfigDto workflowDto) {
        // Create the workflow entity
        WorkflowConfig workflow = new WorkflowConfig();
        workflow.setName(workflowDto.getName());
        workflow.setDescription(workflowDto.getDescription());
        workflow.setReminderBeforeDueMins(workflowDto.getReminderBeforeDueMins());
        workflow.setMinutesAfterDue(workflowDto.getMinutesAfterDue());
        workflow.setEscalationAfterMins(workflowDto.getEscalationAfterMins());
        workflow.setDueInMins(workflowDto.getDueInMins());
        workflow.setIsActive(workflowDto.getIsActive() != null ? workflowDto.getIsActive() : "Y");
        workflow.setCreatedBy(workflowDto.getCreatedBy() != null ? workflowDto.getCreatedBy() : "system");
        
        WorkflowConfig savedWorkflow = workflowRepository.save(workflow);
        WorkflowConfigDto createdWorkflow = convertToDto(savedWorkflow);
        
        // Create roles with sequence mapping
        Map<Integer, Long> roleSequenceToIdMap = new HashMap<>();
        if (workflowDto.getRoles() != null) {
            for (WorkflowRoleDto roleDto : workflowDto.getRoles()) {
                // For now, we'll use a simple mapping - in a real implementation,
                // you'd need to create roles first and then map them
                // This is a placeholder that assumes role IDs are available
                roleSequenceToIdMap.put(roleDto.getRoleSequence(), (long) roleDto.getRoleSequence());
            }
        }
        
        // Create tasks with sequence mapping
        Map<Integer, Long> taskSequenceToIdMap = new HashMap<>();
        Map<Integer, Long> fileSequenceToIdMap = new HashMap<>(); // Global file sequence to ID mapping
        
        if (workflowDto.getTasks() != null) {
            // First pass: Create all tasks
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskSequence() != null) {
                    // Map role sequence to role ID if provided
                    if (taskDto.getRoleSequence() != null) {
                        Long roleId = roleSequenceToIdMap.get(taskDto.getRoleSequence());
                        if (roleId != null) {
                            taskDto.setRoleId(roleId);
                        }
                    }
                    
                    taskDto.setWorkflowId(createdWorkflow.getWorkflowId());
                    WorkflowConfigTaskDto createdTask = addTask(createdWorkflow.getWorkflowId(), taskDto);
                    taskSequenceToIdMap.put(taskDto.getTaskSequence(), createdTask.getTaskId());
                }
            }
            
            // Second pass: Create files for each task and build global file sequence mapping
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskSequence() != null) {
                    Long taskId = taskSequenceToIdMap.get(taskDto.getTaskSequence());
                    
                    if (taskDto.getTaskFiles() != null) {
                        for (WorkflowConfigTaskFileDto fileDto : taskDto.getTaskFiles()) {
                            fileDto.setTaskId(taskId);
                            WorkflowConfigTaskFileDto createdFile = addFileToTask(taskId, fileDto);
                            
                            // Map file sequence to file ID globally
                            if (fileDto.getFileSequence() != null) {
                                fileSequenceToIdMap.put(fileDto.getFileSequence(), createdFile.getFileId());
                            }
                        }
                    }
                }
            }
            
            // Third pass: Create file dependencies
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskSequence() != null && taskDto.getTaskFiles() != null) {
                    for (WorkflowConfigTaskFileDto fileDto : taskDto.getTaskFiles()) {
                        if (fileDto.getDependencies() != null) {
                            for (WorkflowConfigTaskFileDependencyDto depDto : fileDto.getDependencies()) {
                                depDto.setCreatedBy(workflowDto.getCreatedBy());
                                
                                // Find the file IDs for both files using global mapping
                                Long fileId = fileSequenceToIdMap.get(fileDto.getFileSequence());
                                Long parentFileId = fileSequenceToIdMap.get(depDto.getParentFileSequence());
                                
                                if (fileId != null && parentFileId != null) {
                                    depDto.setFileId(fileId);
                                    depDto.setParentFileId(parentFileId);
                                    dependencyService.createDependency(depDto);
                                }
                            }
                        }
                    }
                }
            }
            
            // Fourth pass: Create decision outcomes
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskSequence() != null && taskDto.getDecisionOutcomes() != null && !taskDto.getDecisionOutcomes().isEmpty()) {
                    Long taskId = taskSequenceToIdMap.get(taskDto.getTaskSequence());
                    
                    for (TaskDecisionOutcomeDto outcomeDto : taskDto.getDecisionOutcomes()) {
                        TaskDecisionOutcome outcome = new TaskDecisionOutcome();
                        outcome.setTask(configTaskRepository.findById(taskId).orElse(null));
                        outcome.setOutcomeName(outcomeDto.getOutcomeName());
                        outcome.setRevisionType(outcomeDto.getRevisionType());
                        outcome.setRevisionStrategy(outcomeDto.getRevisionStrategy());
                        outcome.setRevisionPriority(outcomeDto.getRevisionPriority());
                        outcome.setRevisionConditions(outcomeDto.getRevisionConditions());
                        outcome.setAutoEscalate(outcomeDto.getAutoEscalate());
                        outcome.setCreatedBy(workflowDto.getCreatedBy());
                        
                        // Map next task sequence to task ID
                        if (outcomeDto.getTargetTaskSequence() != null) {
                            Long nextTaskId = taskSequenceToIdMap.get(outcomeDto.getTargetTaskSequence());
                            outcome.setTargetTaskId(nextTaskId);
                        }
                        
                        // Map revision task sequences to task IDs
                        if (outcomeDto.getRevisionTaskSequences() != null) {
                            List<String> revisionTaskIds = outcomeDto.getRevisionTaskSequences().stream()
                                .map(seq -> taskSequenceToIdMap.get(seq))
                                .filter(Objects::nonNull)
                                .map(String::valueOf)
                                .collect(Collectors.toList());
                            outcome.setRevisionTaskIds(String.join(",", revisionTaskIds));
                        }
                        
                        taskDecisionOutcomeRepository.save(outcome);
                    }
                }
            }
            
            // Fifth pass: Update tasks with parent task IDs
            for (WorkflowConfigTaskDto taskDto : workflowDto.getTasks()) {
                if (taskDto.getTaskSequence() != null && taskDto.getParentTaskSequences() != null && !taskDto.getParentTaskSequences().isEmpty()) {
                    Long taskId = taskSequenceToIdMap.get(taskDto.getTaskSequence());
                    List<String> parentTaskIds = taskDto.getParentTaskSequences().stream()
                        .map(seq -> taskSequenceToIdMap.get(seq))
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .collect(Collectors.toList());
                    
                    WorkflowConfigTaskDto existingTaskDto = convertToTaskDto(configTaskRepository.findById(taskId).orElse(null));
                    existingTaskDto.setParentTaskIds(String.join(",", parentTaskIds));
                    updateTask(createdWorkflow.getWorkflowId(), taskId, existingTaskDto);
                }
            }
        }
        
        return getWorkflowById(createdWorkflow.getWorkflowId()).orElse(createdWorkflow);
    }
    
}
