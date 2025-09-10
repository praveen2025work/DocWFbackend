package com.docwf.service;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.dto.WorkflowConfigTaskFileDto;
import com.docwf.dto.WorkflowConfigRoleDto;
import com.docwf.dto.WorkflowConfigParamDto;
import com.docwf.entity.WorkflowConfig;
import com.docwf.entity.WorkflowConfigTask;
import com.docwf.entity.WorkflowConfigTaskFile;
import com.docwf.entity.WorkflowConfigRole;
import com.docwf.entity.WorkflowConfigParam;
import com.docwf.entity.WorkflowRole;
import com.docwf.entity.WorkflowUser;
import com.docwf.repository.WorkflowConfigRepository;
import com.docwf.repository.WorkflowRoleRepository;
import com.docwf.repository.WorkflowUserRepository;
import com.docwf.service.impl.WorkflowConfigServiceImpl;
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
 * Comprehensive test scenarios for WorkflowConfigService
 * Covers all different workflow types and configurations
 */
@ExtendWith(MockitoExtension.class)
public class WorkflowConfigServiceTest {

    @Mock
    private WorkflowConfigRepository workflowConfigRepository;

    @Mock
    private WorkflowRoleRepository workflowRoleRepository;

    @Mock
    private WorkflowUserRepository workflowUserRepository;

    @InjectMocks
    private WorkflowConfigServiceImpl workflowConfigService;

    private WorkflowConfig testWorkflowConfig;
    private WorkflowConfigDto testWorkflowConfigDto;

    @BeforeEach
    void setUp() {
        testWorkflowConfig = createTestWorkflowConfig();
        testWorkflowConfigDto = createTestWorkflowConfigDto();
    }

    @Test
    void testCreateWorkflowConfig_SimpleUploadWorkflow() {
        // Given
        WorkflowConfigDto simpleUploadDto = createSimpleUploadWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(simpleUploadDto);

        // Then
        assertNotNull(result);
        assertEquals("Simple Document Upload", result.getName());
        assertEquals("FILE_UPLOAD", result.getTasks().get(0).getTaskType());
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testCreateWorkflowConfig_ComplexMultiStepWorkflow() {
        // Given
        WorkflowConfigDto complexDto = createComplexMultiStepWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(complexDto);

        // Then
        assertNotNull(result);
        assertEquals("Complex Multi-Step Workflow", result.getName());
        assertEquals(5, result.getTasks().size());
        
        // Verify task types
        assertEquals("FILE_UPLOAD", result.getTasks().get(0).getTaskType());
        assertEquals("FILE_UPDATE", result.getTasks().get(1).getTaskType());
        assertEquals("CONSOLIDATE_FILE", result.getTasks().get(3).getTaskType());
        assertEquals("DECISION", result.getTasks().get(4).getTaskType());
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testCreateWorkflowConfig_ParallelExecutionWorkflow() {
        // Given
        WorkflowConfigDto parallelDto = createParallelExecutionWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(parallelDto);

        // Then
        assertNotNull(result);
        assertEquals("Parallel Execution Workflow", result.getName());
        
        // Verify parallel execution configuration
        WorkflowConfigTaskDto task2 = result.getTasks().get(1);
        WorkflowConfigTaskDto task3 = result.getTasks().get(2);
        
        assertEquals("Y", task2.getCanRunInParallel());
        assertEquals("Y", task3.getCanRunInParallel());
        assertEquals("2,3", task2.getParallelTaskIds());
        assertEquals("2,3", task3.getParallelTaskIds());
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testCreateWorkflowConfig_DecisionBasedRoutingWorkflow() {
        // Given
        WorkflowConfigDto decisionDto = createDecisionBasedRoutingWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(decisionDto);

        // Then
        assertNotNull(result);
        assertEquals("Decision-Based Routing Workflow", result.getName());
        
        // Verify decision task configuration
        WorkflowConfigTaskDto decisionTask = result.getTasks().get(2);
        assertEquals("DECISION", decisionTask.getTaskType());
        assertEquals("Y", decisionTask.getIsDecisionTask());
        assertEquals("APPROVAL", decisionTask.getDecisionType());
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testCreateWorkflowConfig_ConsolidationWorkflow() {
        // Given
        WorkflowConfigDto consolidationDto = createConsolidationWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(consolidationDto);

        // Then
        assertNotNull(result);
        assertEquals("File Consolidation Workflow", result.getName());
        
        // Verify consolidation task configuration
        WorkflowConfigTaskDto consolidationTask = result.getTasks().get(2);
        assertEquals("CONSOLIDATE_FILE", consolidationTask.getTaskType());
        assertEquals("HYBRID", consolidationTask.getConsolidationMode());
        assertEquals("USER_SELECT", consolidationTask.getFileSelectionStrategy());
        assertEquals(1, consolidationTask.getMinFileSelections());
        assertNull(consolidationTask.getMaxFileSelections()); // No upper limit
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testCreateWorkflowConfig_RevisitationWorkflow() {
        // Given
        WorkflowConfigDto revisitationDto = createRevisitationWorkflowDto();
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.createWorkflowConfig(revisitationDto);

        // Then
        assertNotNull(result);
        assertEquals("Revisitation Workflow", result.getName());
        
        // Verify revisitation configuration
        WorkflowConfigTaskDto revisitableTask = result.getTasks().get(1);
        assertEquals("Y", revisitableTask.getCanBeRevisited());
        assertEquals(3, revisitableTask.getMaxRevisits());
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testUpdateWorkflowConfig_AddNewTasks() {
        // Given
        Long workflowId = 1L;
        WorkflowConfigDto updateDto = createUpdateWorkflowDto();
        when(workflowConfigRepository.findById(workflowId)).thenReturn(Optional.of(testWorkflowConfig));
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.updateWorkflowConfig(workflowId, updateDto);

        // Then
        assertNotNull(result);
        assertEquals("Updated Workflow", result.getName());
        assertEquals(4, result.getTasks().size()); // Original 3 + 1 new task
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testUpdateWorkflowConfig_ModifyTaskConfiguration() {
        // Given
        Long workflowId = 1L;
        WorkflowConfigDto updateDto = createTaskModificationDto();
        when(workflowConfigRepository.findById(workflowId)).thenReturn(Optional.of(testWorkflowConfig));
        when(workflowConfigRepository.save(any(WorkflowConfig.class))).thenReturn(testWorkflowConfig);

        // When
        WorkflowConfigDto result = workflowConfigService.updateWorkflowConfig(workflowId, updateDto);

        // Then
        assertNotNull(result);
        
        // Verify task modification
        WorkflowConfigTaskDto modifiedTask = result.getTasks().get(0);
        assertEquals("HIGH", modifiedTask.getTaskPriority());
        assertEquals("Updated task description", modifiedTask.getTaskDescription());
        
        verify(workflowConfigRepository).save(any(WorkflowConfig.class));
    }

    @Test
    void testGetWorkflowConfigs_WithFilters() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<WorkflowConfig> workflows = Arrays.asList(testWorkflowConfig);
        Page<WorkflowConfig> page = new PageImpl<>(workflows, pageable, 1);
        
        when(workflowConfigRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<WorkflowConfigDto> result = workflowConfigService.getWorkflowConfigs(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        
        verify(workflowConfigRepository).findAll(pageable);
    }

    @Test
    void testGetWorkflowConfigs_ByStatus() {
        // Given
        String status = "ACTIVE";
        List<WorkflowConfig> workflows = Arrays.asList(testWorkflowConfig);
        when(workflowConfigRepository.findByIsActive(status)).thenReturn(workflows);

        // When
        List<WorkflowConfigDto> result = workflowConfigService.getWorkflowConfigsByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Y", result.get(0).getIsActive());
        
        verify(workflowConfigRepository).findByIsActive(status);
    }

    @Test
    void testGetWorkflowConfigs_ByCalendarId() {
        // Given
        Long calendarId = 1L;
        List<WorkflowConfig> workflows = Arrays.asList(testWorkflowConfig);
        when(workflowConfigRepository.findByCalendarIdAndIsActive(calendarId, "Y")).thenReturn(workflows);

        // When
        List<WorkflowConfig> result = workflowConfigService.getWorkflowsByCalendarId(calendarId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(calendarId, result.get(0).getCalendarId());
        
        verify(workflowConfigRepository).findByCalendarIdAndIsActive(calendarId, "Y");
    }

    @Test
    void testDeleteWorkflowConfig() {
        // Given
        Long workflowId = 1L;
        when(workflowConfigRepository.findById(workflowId)).thenReturn(Optional.of(testWorkflowConfig));

        // When
        workflowConfigService.deleteWorkflowConfig(workflowId);

        // Then
        verify(workflowConfigRepository).delete(testWorkflowConfig);
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

    private WorkflowConfigDto createTestWorkflowConfigDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Test Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        return dto;
    }

    private WorkflowConfigDto createSimpleUploadWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Simple Document Upload");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName("Upload Document");
        task.setTaskType("FILE_UPLOAD");
        task.setSequenceOrder(1);
        task.setRoleId(1L);
        
        dto.setTasks(Arrays.asList(task));
        return dto;
    }

    private WorkflowConfigDto createComplexMultiStepWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Complex Multi-Step Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createTaskDto("Review", "FILE_UPDATE", 2, 2L),
            createTaskDto("Process", "FILE_UPDATE", 2, 2L),
            createTaskDto("Consolidate", "CONSOLIDATE_FILE", 3, 3L),
            createTaskDto("Approve", "DECISION", 4, 4L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createParallelExecutionWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Parallel Execution Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createTaskDto("Process A", "FILE_UPDATE", 2, 2L, "Y", "2,3"),
            createTaskDto("Process B", "FILE_UPDATE", 2, 3L, "Y", "2,3"),
            createTaskDto("Consolidate", "CONSOLIDATE_FILE", 3, 4L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createDecisionBasedRoutingWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Decision-Based Routing Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createTaskDto("Review", "FILE_UPDATE", 2, 2L),
            createDecisionTaskDto("Approve", 3, 4L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createConsolidationWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("File Consolidation Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createTaskDto("Update", "FILE_UPDATE", 2, 2L),
            createConsolidationTaskDto("Consolidate", 3, 3L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createRevisitationWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Revisitation Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createRevisitableTaskDto("Review", 2, 2L),
            createTaskDto("Approve", "DECISION", 3, 3L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createUpdateWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Updated Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", "FILE_UPLOAD", 1, 1L),
            createTaskDto("Review", "FILE_UPDATE", 2, 2L),
            createTaskDto("Approve", "DECISION", 3, 3L),
            createTaskDto("Archive", "FILE_UPDATE", 4, 4L) // New task
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createTaskModificationDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Updated Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        WorkflowConfigTaskDto task = createTaskDto("Upload", "FILE_UPLOAD", 1, 1L);
        task.setTaskPriority("HIGH");
        task.setTaskDescription("Updated task description");
        
        dto.setTasks(Arrays.asList(task));
        return dto;
    }

    private WorkflowConfigTaskDto createTaskDto(String name, String taskType, int sequenceOrder, Long roleId) {
        return createTaskDto(name, taskType, sequenceOrder, roleId, "N", "");
    }

    private WorkflowConfigTaskDto createTaskDto(String name, String taskType, int sequenceOrder, Long roleId, 
                                               String canRunInParallel, String parallelTaskIds) {
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName(name);
        task.setTaskType(taskType);
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        task.setCanRunInParallel(canRunInParallel);
        task.setParallelTaskIds(parallelTaskIds);
        return task;
    }

    private WorkflowConfigTaskDto createDecisionTaskDto(String name, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName(name);
        task.setTaskType("DECISION");
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        task.setIsDecisionTask("Y");
        task.setDecisionType("APPROVAL");
        return task;
    }

    private WorkflowConfigTaskDto createConsolidationTaskDto(String name, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName(name);
        task.setTaskType("CONSOLIDATE_FILE");
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        task.setConsolidationMode("HYBRID");
        task.setFileSelectionStrategy("USER_SELECT");
        task.setMinFileSelections(1);
        task.setMaxFileSelections(null); // No upper limit
        return task;
    }

    private WorkflowConfigTaskDto createRevisitableTaskDto(String name, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName(name);
        task.setTaskType("FILE_UPDATE");
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        task.setCanBeRevisited("Y");
        task.setMaxRevisits(3);
        return task;
    }
}
