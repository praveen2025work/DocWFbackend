package com.docwf.service;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.CreateWorkflowInstanceDto;
import com.docwf.entity.WorkflowInstance;
import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowConfig;
import com.docwf.entity.WorkflowConfigTask;
import com.docwf.entity.WorkflowUser;
import com.docwf.repository.WorkflowInstanceRepository;
import com.docwf.repository.WorkflowInstanceTaskRepository;
import com.docwf.repository.WorkflowConfigRepository;
import com.docwf.repository.WorkflowUserRepository;
import com.docwf.service.impl.WorkflowExecutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test scenarios for Workflow Instance Execution
 * Covers all different execution paths and scenarios
 */
@ExtendWith(MockitoExtension.class)
public class WorkflowInstanceExecutionTest {

    @Mock
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Mock
    private WorkflowInstanceTaskRepository workflowInstanceTaskRepository;

    @Mock
    private WorkflowConfigRepository workflowConfigRepository;

    @Mock
    private WorkflowUserRepository workflowUserRepository;

    @InjectMocks
    private WorkflowExecutionServiceImpl workflowExecutionService;

    private WorkflowConfig testWorkflowConfig;
    private WorkflowInstance testWorkflowInstance;
    private WorkflowUser testUser;

    @BeforeEach
    void setUp() {
        testWorkflowConfig = createTestWorkflowConfig();
        testWorkflowInstance = createTestWorkflowInstance();
        testUser = createTestUser();
    }

    @Test
    void testStartWorkflowInstance_SimpleUploadWorkflow() {
        // Given
        CreateWorkflowInstanceDto createDto = new CreateWorkflowInstanceDto(1L, 101L, 1L);
        when(workflowConfigRepository.findById(1L)).thenReturn(Optional.of(testWorkflowConfig));
        when(workflowUserRepository.findById(101L)).thenReturn(Optional.of(testUser));
        when(workflowInstanceRepository.save(any(WorkflowInstance.class))).thenReturn(testWorkflowInstance);
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(new WorkflowInstanceTask());

        // When
        WorkflowInstanceDto result = workflowExecutionService.startWorkflowWithCalendar(createDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getWorkflowId());
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceRepository).save(any(WorkflowInstance.class));
    }

    @Test
    void testStartWorkflowInstance_ComplexMultiStepWorkflow() {
        // Given
        WorkflowConfig complexConfig = createComplexMultiStepWorkflowConfig();
        CreateWorkflowInstanceDto createDto = new CreateWorkflowInstanceDto(1L, 101L, 1L);
        when(workflowConfigRepository.findById(1L)).thenReturn(Optional.of(complexConfig));
        when(workflowUserRepository.findById(101L)).thenReturn(Optional.of(testUser));
        when(workflowInstanceRepository.save(any(WorkflowInstance.class))).thenReturn(testWorkflowInstance);
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(new WorkflowInstanceTask());

        // When
        WorkflowInstanceDto result = workflowExecutionService.startWorkflowWithCalendar(createDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getWorkflowId());
        assertEquals("IN_PROGRESS", result.getStatus());
        
        // Verify that all tasks are created
        verify(workflowInstanceTaskRepository, times(5)).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testStartWorkflowInstance_ParallelExecutionWorkflow() {
        // Given
        WorkflowConfig parallelConfig = createParallelExecutionWorkflowConfig();
        CreateWorkflowInstanceDto createDto = new CreateWorkflowInstanceDto(1L, 101L, 1L);
        when(workflowConfigRepository.findById(1L)).thenReturn(Optional.of(parallelConfig));
        when(workflowUserRepository.findById(101L)).thenReturn(Optional.of(testUser));
        when(workflowInstanceRepository.save(any(WorkflowInstance.class))).thenReturn(testWorkflowInstance);
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(new WorkflowInstanceTask());

        // When
        WorkflowInstanceDto result = workflowExecutionService.startWorkflowWithCalendar(createDto);

        // Then
        assertNotNull(result);
        
        // Verify parallel tasks are created
        verify(workflowInstanceTaskRepository, times(4)).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testExecuteTask_FileUploadTask() {
        // Given
        Long instanceTaskId = 1L;
        WorkflowInstanceTask uploadTask = createFileUploadTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(uploadTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(uploadTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.executeTask(instanceTaskId, "UPLOAD", "Uploading document");

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testExecuteTask_FileUpdateTask() {
        // Given
        Long instanceTaskId = 2L;
        WorkflowInstanceTask updateTask = createFileUpdateTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(updateTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(updateTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.executeTask(instanceTaskId, "UPDATE", "Updating document");

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testExecuteTask_ConsolidationTask() {
        // Given
        Long instanceTaskId = 3L;
        WorkflowInstanceTask consolidationTask = createConsolidationTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(consolidationTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(consolidationTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.executeTask(instanceTaskId, "CONSOLIDATE", "Consolidating files");

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testExecuteTask_DecisionTask() {
        // Given
        Long instanceTaskId = 4L;
        WorkflowInstanceTask decisionTask = createDecisionTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(decisionTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(decisionTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.executeTask(instanceTaskId, "APPROVE", "Approving workflow");

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testCompleteTask_FileUploadTask() {
        // Given
        Long instanceTaskId = 1L;
        WorkflowInstanceTask uploadTask = createFileUploadTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(uploadTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(uploadTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.completeTask(instanceTaskId, "Document uploaded successfully");

        // Then
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testCompleteTask_WithFileUploads() {
        // Given
        Long instanceTaskId = 1L;
        WorkflowInstanceTask uploadTask = createFileUploadTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(uploadTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(uploadTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.completeTaskWithFiles(instanceTaskId, 
            Arrays.asList("file1.pdf", "file2.docx"), "Files uploaded successfully");

        // Then
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testRejectTask() {
        // Given
        Long instanceTaskId = 1L;
        WorkflowInstanceTask uploadTask = createFileUploadTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(uploadTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(uploadTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.rejectTask(instanceTaskId, "Invalid file format");

        // Then
        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        assertEquals("Invalid file format", result.getRejectionReason());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testEscalateTask() {
        // Given
        Long instanceTaskId = 1L;
        WorkflowInstanceTask uploadTask = createFileUploadTask();
        when(workflowInstanceTaskRepository.findById(instanceTaskId)).thenReturn(Optional.of(uploadTask));
        when(workflowInstanceTaskRepository.save(any(WorkflowInstanceTask.class))).thenReturn(uploadTask);

        // When
        WorkflowInstanceTaskDto result = workflowExecutionService.escalateTask(instanceTaskId, "Task requires manager approval");

        // Then
        assertNotNull(result);
        assertEquals("ESCALATED", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testGetWorkflowInstance_WithTasks() {
        // Given
        Long instanceId = 1L;
        when(workflowInstanceRepository.findById(instanceId)).thenReturn(Optional.of(testWorkflowInstance));
        when(workflowInstanceTaskRepository.findByInstanceInstanceId(instanceId)).thenReturn(Arrays.asList(new WorkflowInstanceTask()));

        // When
        WorkflowInstanceDto result = workflowExecutionService.getWorkflowInstance(instanceId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getInstanceId());
        verify(workflowInstanceRepository).findById(instanceId);
    }

    @Test
    void testGetWorkflowInstances_WithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<WorkflowInstance> instances = Arrays.asList(testWorkflowInstance);
        Page<WorkflowInstance> page = new PageImpl<>(instances, pageable, 1);
        when(workflowInstanceRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<WorkflowInstanceDto> result = workflowExecutionService.getWorkflowInstances(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(workflowInstanceRepository).findAll(pageable);
    }

    @Test
    void testGetWorkflowInstances_ByStatus() {
        // Given
        String status = "IN_PROGRESS";
        List<WorkflowInstance> instances = Arrays.asList(testWorkflowInstance);
        when(workflowInstanceRepository.findByStatus(status)).thenReturn(instances);

        // When
        List<WorkflowInstanceDto> result = workflowExecutionService.getWorkflowInstancesByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
        verify(workflowInstanceRepository).findByStatus(status);
    }

    @Test
    void testGetWorkflowInstances_ByUser() {
        // Given
        Long userId = 101L;
        List<WorkflowInstance> instances = Arrays.asList(testWorkflowInstance);
        when(workflowInstanceRepository.findByAssignedToUserId(userId)).thenReturn(instances);

        // When
        List<WorkflowInstanceDto> result = workflowExecutionService.getWorkflowInstancesByUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(workflowInstanceRepository).findByAssignedToUserId(userId);
    }

    @Test
    void testCancelWorkflowInstance() {
        // Given
        Long instanceId = 1L;
        when(workflowInstanceRepository.findById(instanceId)).thenReturn(Optional.of(testWorkflowInstance));
        when(workflowInstanceRepository.save(any(WorkflowInstance.class))).thenReturn(testWorkflowInstance);

        // When
        workflowExecutionService.cancelWorkflowInstance(instanceId, "Workflow cancelled by user");

        // Then
        verify(workflowInstanceRepository).save(any(WorkflowInstance.class));
    }

    // Helper methods to create test data
    private WorkflowConfig createTestWorkflowConfig() {
        WorkflowConfig config = new WorkflowConfig();
        config.setWorkflowId(1L);
        config.setName("Test Workflow");
        config.setIsActive("Y");
        config.setCalendarId(1L);
        return config;
    }

    private WorkflowConfig createComplexMultiStepWorkflowConfig() {
        WorkflowConfig config = new WorkflowConfig();
        config.setWorkflowId(1L);
        config.setName("Complex Multi-Step Workflow");
        config.setIsActive("Y");
        config.setCalendarId(1L);
        
        // Add tasks
        List<WorkflowConfigTask> tasks = Arrays.asList(
            createConfigTask("Upload", "FILE_UPLOAD", 1, 1L),
            createConfigTask("Review", "FILE_UPDATE", 2, 2L),
            createConfigTask("Process", "FILE_UPDATE", 2, 2L),
            createConfigTask("Consolidate", "CONSOLIDATE_FILE", 3, 3L),
            createConfigTask("Approve", "DECISION", 4, 4L)
        );
        
        config.setTasks(tasks);
        return config;
    }

    private WorkflowConfig createParallelExecutionWorkflowConfig() {
        WorkflowConfig config = new WorkflowConfig();
        config.setWorkflowId(1L);
        config.setName("Parallel Execution Workflow");
        config.setIsActive("Y");
        config.setCalendarId(1L);
        
        // Add tasks with parallel execution
        List<WorkflowConfigTask> tasks = Arrays.asList(
            createConfigTask("Upload", "FILE_UPLOAD", 1, 1L),
            createParallelConfigTask("Process A", "FILE_UPDATE", 2, 2L),
            createParallelConfigTask("Process B", "FILE_UPDATE", 2, 3L),
            createConfigTask("Consolidate", "CONSOLIDATE_FILE", 3, 4L)
        );
        
        config.setTasks(tasks);
        return config;
    }

    private WorkflowInstance createTestWorkflowInstance() {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setInstanceId(1L);
        instance.setWorkflow(testWorkflowConfig);
        instance.setStatus(WorkflowInstance.InstanceStatus.IN_PROGRESS);
        instance.setStartedOn(LocalDateTime.now());
        return instance;
    }

    private WorkflowUser createTestUser() {
        WorkflowUser user = new WorkflowUser();
        user.setUserId(101L);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    private WorkflowConfigTask createConfigTask(String name, String taskType, int sequenceOrder, Long roleId) {
        WorkflowConfigTask task = new WorkflowConfigTask();
        task.setTaskId(sequenceOrder.longValue());
        task.setName(name);
        task.setTaskType(WorkflowConfigTask.TaskType.valueOf(taskType));
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        return task;
    }

    private WorkflowConfigTask createParallelConfigTask(String name, String taskType, int sequenceOrder, Long roleId) {
        WorkflowConfigTask task = createConfigTask(name, taskType, sequenceOrder, roleId);
        task.setCanRunInParallel("Y");
        task.setParallelTaskIds("2,3");
        return task;
    }

    private WorkflowInstanceTask createFileUploadTask() {
        WorkflowInstanceTask task = new WorkflowInstanceTask();
        task.setInstanceTaskId(1L);
        task.setInstance(testWorkflowInstance);
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        
        WorkflowConfigTask configTask = new WorkflowConfigTask();
        configTask.setTaskId(1L);
        configTask.setTaskType(WorkflowConfigTask.TaskType.FILE_UPLOAD);
        configTask.setName("Upload Document");
        task.setTask(configTask);
        
        return task;
    }

    private WorkflowInstanceTask createFileUpdateTask() {
        WorkflowInstanceTask task = new WorkflowInstanceTask();
        task.setInstanceTaskId(2L);
        task.setInstance(testWorkflowInstance);
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        
        WorkflowConfigTask configTask = new WorkflowConfigTask();
        configTask.setTaskId(2L);
        configTask.setTaskType(WorkflowConfigTask.TaskType.FILE_UPDATE);
        configTask.setName("Update Document");
        task.setTask(configTask);
        
        return task;
    }

    private WorkflowInstanceTask createConsolidationTask() {
        WorkflowInstanceTask task = new WorkflowInstanceTask();
        task.setInstanceTaskId(3L);
        task.setInstance(testWorkflowInstance);
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        
        WorkflowConfigTask configTask = new WorkflowConfigTask();
        configTask.setTaskId(3L);
        configTask.setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);
        configTask.setName("Consolidate Files");
        task.setTask(configTask);
        
        return task;
    }

    private WorkflowInstanceTask createDecisionTask() {
        WorkflowInstanceTask task = new WorkflowInstanceTask();
        task.setInstanceTaskId(4L);
        task.setInstance(testWorkflowInstance);
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        
        WorkflowConfigTask configTask = new WorkflowConfigTask();
        configTask.setTaskId(4L);
        configTask.setTaskType(WorkflowConfigTask.TaskType.DECISION);
        configTask.setName("Approve Workflow");
        configTask.setIsDecisionTask("Y");
        task.setTask(configTask);
        
        return task;
    }
}
