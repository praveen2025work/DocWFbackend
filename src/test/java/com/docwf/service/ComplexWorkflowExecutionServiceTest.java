package com.docwf.service;

import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;
import com.docwf.entity.WorkflowConfig;
import com.docwf.entity.WorkflowConfigTask;
import com.docwf.entity.WorkflowInstance;
import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowInstanceTaskFile;
import com.docwf.repository.WorkflowConfigRepository;
import com.docwf.repository.WorkflowInstanceRepository;
import com.docwf.repository.WorkflowInstanceTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for ComplexWorkflowExecutionService
 * Covers all workflow scenarios: file dependencies, consolidation, decision routing
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ComplexWorkflowExecutionServiceTest {

    @Mock
    private WorkflowConfigRepository workflowConfigRepository;

    @Mock
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Mock
    private WorkflowInstanceTaskRepository workflowInstanceTaskRepository;

    @InjectMocks
    private ComplexWorkflowExecutionService complexWorkflowExecutionService;

    private WorkflowConfig testWorkflowConfig;
    private WorkflowInstance testWorkflowInstance;
    private WorkflowInstanceTask testTaskInstance;
    private WorkflowInstanceTaskFile testFileInstance;

    @BeforeEach
    void setUp() {
        // Setup test data
        testWorkflowConfig = createTestWorkflowConfig();
        testWorkflowInstance = createTestWorkflowInstance();
        testTaskInstance = createTestTaskInstance();
        testFileInstance = createTestFileInstance();
    }

    @Test
    void testExecuteTaskStep_FileUpload() {
        // Given
        Long instanceTaskId = 1L;
        String action = "UPLOAD";
        String comments = "Uploading initial documents";

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(testTaskInstance));

        // When
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.executeTaskStep(
            instanceTaskId, action, comments);

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testExecuteTaskStep_FileUpdate() {
        // Given
        Long instanceTaskId = 2L;
        String action = "UPDATE";
        String comments = "Updating document based on feedback";

        WorkflowInstanceTask updateTask = createTestTaskInstance();
        updateTask.getTask().setTaskType(WorkflowConfigTask.TaskType.FILE_UPDATE);

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(updateTask));

        // When
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.executeTaskStep(
            instanceTaskId, action, comments);

        // Then
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testGetAvailableFilesForTask_WithDependencies() {
        // Given
        Long instanceTaskId = 4L; // Consolidation task
        WorkflowInstanceTask consolidationTask = createTestTaskInstance();
        consolidationTask.getTask().setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);
        consolidationTask.getTask().setFileSourceTaskIds("1,2,3");

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(consolidationTask));

        // When
        List<Object> availableFiles = complexWorkflowExecutionService.getAvailableFilesForTask(instanceTaskId);

        // Then
        assertNotNull(availableFiles);
        // Note: This test will need actual implementation to work properly
    }

    @Test
    void testConsolidateFiles_ManualMode() {
        // Given
        Long instanceTaskId = 4L;
        List<Long> selectedFileIds = Arrays.asList(1L, 2L, 3L);
        String consolidationNotes = "Consolidating selected files";

        WorkflowInstanceTask consolidationTask = createTestTaskInstance();
        consolidationTask.getTask().setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);
        consolidationTask.getTask().setConsolidationMode("MANUAL");
        consolidationTask.getTask().setMinFileSelections(1);
        consolidationTask.getTask().setMaxFileSelections(null); // No upper limit

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(consolidationTask));

        // When
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.consolidateFiles(
            instanceTaskId, selectedFileIds, consolidationNotes);

        // Then
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testConsolidateFiles_WithMinMaxConstraints() {
        // Given
        Long instanceTaskId = 4L;
        List<Long> selectedFileIds = Arrays.asList(1L); // Only 1 file
        String consolidationNotes = "Testing minimum file constraint";

        WorkflowInstanceTask consolidationTask = createTestTaskInstance();
        consolidationTask.getTask().setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);
        consolidationTask.getTask().setMinFileSelections(2); // Requires at least 2 files
        consolidationTask.getTask().setMaxFileSelections(5);

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(consolidationTask));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            complexWorkflowExecutionService.consolidateFiles(
                instanceTaskId, selectedFileIds, consolidationNotes);
        });
    }

    @Test
    void testMakeDecision_WithRouting() {
        // Given
        Long instanceTaskId = 5L;
        String decision = "NEEDS_REVISION";
        String comments = "Documents need revision";
        String nextAction = "REVISE";

        WorkflowInstanceTask decisionTask = createTestTaskInstance();
        decisionTask.getTask().setTaskType(WorkflowConfigTask.TaskType.DECISION);
        decisionTask.getTask().setRevisionStrategy("SELECTIVE");
        decisionTask.getTask().setRevisionTaskMapping("{\"NEEDS_REVISION\": \"2,3\"}");

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(decisionTask));

        // When
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.makeDecision(
            instanceTaskId, decision, comments, nextAction);

        // Then
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(workflowInstanceTaskRepository).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testRouteWorkflowBasedOnDecision_SelectiveRevision() {
        // Given
        Long instanceId = 1L;
        Long decisionTaskId = 5L;
        String decision = "REJECTED";
        String nextAction = "REVISE";

        // When
        complexWorkflowExecutionService.routeWorkflowBasedOnDecision(
            instanceId, decisionTaskId, decision, nextAction);

        // Then
        // Verify that revision tasks are opened
        verify(workflowInstanceTaskRepository, times(2)).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testRouteWorkflowBasedOnDecision_CascadeRevision() {
        // Given
        Long instanceId = 1L;
        Long decisionTaskId = 5L;
        String decision = "REJECTED";
        String nextAction = "REVISE";

        WorkflowInstanceTask decisionTask = createTestTaskInstance();
        decisionTask.getTask().setRevisionStrategy("CASCADE");

        when(workflowInstanceTaskRepository.findById(decisionTaskId))
            .thenReturn(Optional.of(decisionTask));

        // When
        complexWorkflowExecutionService.routeWorkflowBasedOnDecision(
            instanceId, decisionTaskId, decision, nextAction);

        // Then
        // Verify cascade revision is handled
        verify(workflowInstanceTaskRepository, atLeastOnce()).save(any(WorkflowInstanceTask.class));
    }

    @Test
    void testValidateFileDependencies_Valid() {
        // Given
        Long instanceTaskId = 2L; // Update task
        WorkflowInstanceTask updateTask = createTestTaskInstance();
        updateTask.getTask().setFileSourceTaskIds("1"); // Depends on task 1

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(updateTask));

        // When
        boolean isValid = complexWorkflowExecutionService.validateFileDependencies(instanceTaskId);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateFileDependencies_Invalid() {
        // Given
        Long instanceTaskId = 2L; // Update task
        WorkflowInstanceTask updateTask = createTestTaskInstance();
        updateTask.getTask().setFileSourceTaskIds("1"); // Depends on task 1

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(updateTask));

        // When
        boolean isValid = complexWorkflowExecutionService.validateFileDependencies(instanceTaskId);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testGetConsolidationPreview() {
        // Given
        Long instanceTaskId = 4L;
        WorkflowInstanceTask consolidationTask = createTestTaskInstance();
        consolidationTask.getTask().setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(consolidationTask));

        // When
        Object preview = complexWorkflowExecutionService.getConsolidationPreview(instanceTaskId);

        // Then
        assertNotNull(preview);
    }

    @Test
    void testExecuteAutoConsolidation() {
        // Given
        Long instanceTaskId = 4L;
        WorkflowInstanceTask consolidationTask = createTestTaskInstance();
        consolidationTask.getTask().setTaskType(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE);
        consolidationTask.getTask().setConsolidationMode("AUTO");

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(consolidationTask));

        // When
        WorkflowInstanceTaskDto result = complexWorkflowExecutionService.executeAutoConsolidation(instanceTaskId);

        // Then
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void testGetDecisionRoutingOptions() {
        // Given
        Long instanceTaskId = 5L;
        WorkflowInstanceTask decisionTask = createTestTaskInstance();
        decisionTask.getTask().setTaskType(WorkflowConfigTask.TaskType.DECISION);

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(decisionTask));

        // When
        List<TaskInstanceDecisionOutcomeDto> options = complexWorkflowExecutionService.getDecisionRoutingOptions(instanceTaskId);

        // Then
        assertNotNull(options);
        assertFalse(options.isEmpty());
    }

    @Test
    void testCanTaskBeRevised() {
        // Given
        Long instanceTaskId = 2L;
        WorkflowInstanceTask task = createTestTaskInstance();
        task.getTask().setCanBeRevisited("Y");
        task.getTask().setMaxRevisits(5);

        when(workflowInstanceTaskRepository.findById(instanceTaskId))
            .thenReturn(Optional.of(task));

        // When
        boolean canRevise = complexWorkflowExecutionService.canTaskBeRevised(instanceTaskId);

        // Then
        assertTrue(canRevise);
    }

    @Test
    void testGetRevisionHistory() {
        // Given
        Long instanceId = 1L;

        // When
        List<Object> history = complexWorkflowExecutionService.getRevisionHistory(instanceId);

        // Then
        assertNotNull(history);
    }

    // Helper methods to create test data
    private WorkflowConfig createTestWorkflowConfig() {
        WorkflowConfig config = new WorkflowConfig();
        config.setWorkflowId(1L);
        config.setName("Test Workflow");
        config.setIsActive("Y");
        return config;
    }

    private WorkflowInstance createTestWorkflowInstance() {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setInstanceId(1L);
        instance.setWorkflow(testWorkflowConfig);
        instance.setStatus(WorkflowInstance.InstanceStatus.IN_PROGRESS);
        return instance;
    }

    private WorkflowInstanceTask createTestTaskInstance() {
        WorkflowInstanceTask task = new WorkflowInstanceTask();
        task.setInstanceTaskId(1L);
        task.setWorkflowInstance(testWorkflowInstance);
        task.setStatus(WorkflowInstanceTask.TaskInstanceStatus.PENDING);
        
        WorkflowConfigTask configTask = new WorkflowConfigTask();
        configTask.setTaskId(1L);
        configTask.setTaskType(WorkflowConfigTask.TaskType.FILE_UPLOAD);
        configTask.setName("Test Task");
        task.setTask(configTask);
        
        return task;
    }

    private WorkflowInstanceTaskFile createTestFileInstance() {
        // Return null for now - this will be implemented when the service is fully implemented
        return null;
    }
}
