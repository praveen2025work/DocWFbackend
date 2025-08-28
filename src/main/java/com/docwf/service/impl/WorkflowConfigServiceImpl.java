package com.docwf.service.impl;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigParamDto;
import com.docwf.entity.*;
import com.docwf.exception.WorkflowException;
import com.docwf.repository.*;
import com.docwf.service.WorkflowConfigService;
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
    
    @Override
    public WorkflowConfigDto createWorkflow(WorkflowConfigDto workflowDto) {
        // Validate unique name
        if (workflowRepository.existsByName(workflowDto.getName())) {
            throw new WorkflowException("Workflow name already exists: " + workflowDto.getName());
        }
        
        // Create workflow entity
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
}
