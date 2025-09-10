package com.docwf.service;

import com.docwf.dto.WorkflowConfigDto;
import com.docwf.dto.WorkflowConfigTaskDto;
import com.docwf.entity.WorkflowConfigTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test scenarios for different workflow configuration types
 */
@SpringBootTest
class WorkflowConfigScenariosTest {

    @Test
    void testSimpleUploadWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createSimpleUploadWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("Simple Document Upload", dto.getName());
        assertEquals(1, dto.getTasks().size());
        
        WorkflowConfigTaskDto task = dto.getTasks().get(0);
        assertEquals("Upload Document", task.getName());
        assertEquals(WorkflowConfigTask.TaskType.FILE_UPLOAD, task.getTaskType());
        assertEquals(1, task.getSequenceOrder());
        assertEquals(1L, task.getRoleId());
    }

    @Test
    void testComplexMultiStepWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createComplexMultiStepWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("Complex Multi-Step Workflow", dto.getName());
        assertEquals(5, dto.getTasks().size());
        
        // Verify task sequence and types
        List<WorkflowConfigTaskDto> tasks = dto.getTasks();
        assertEquals(WorkflowConfigTask.TaskType.FILE_UPLOAD, tasks.get(0).getTaskType());
        assertEquals(WorkflowConfigTask.TaskType.FILE_UPDATE, tasks.get(1).getTaskType());
        assertEquals(WorkflowConfigTask.TaskType.FILE_UPDATE, tasks.get(2).getTaskType());
        assertEquals(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE, tasks.get(3).getTaskType());
        assertEquals(WorkflowConfigTask.TaskType.DECISION, tasks.get(4).getTaskType());
    }

    @Test
    void testParallelExecutionWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createParallelExecutionWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("Parallel Execution Workflow", dto.getName());
        
        // Verify parallel execution configuration
        WorkflowConfigTaskDto parallelTask1 = dto.getTasks().get(1);
        WorkflowConfigTaskDto parallelTask2 = dto.getTasks().get(2);
        
        assertEquals("Y", parallelTask1.getCanRunInParallel());
        assertEquals("Y", parallelTask2.getCanRunInParallel());
        assertEquals("2,3", parallelTask1.getParallelTaskIds());
        assertEquals("2,3", parallelTask2.getParallelTaskIds());
    }

    @Test
    void testDecisionBasedRoutingWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createDecisionBasedRoutingWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("Decision-Based Routing Workflow", dto.getName());
        
        // Verify decision task configuration
        WorkflowConfigTaskDto decisionTask = dto.getTasks().get(2);
        assertEquals(WorkflowConfigTask.TaskType.DECISION, decisionTask.getTaskType());
        assertEquals("Y", decisionTask.getIsDecisionTask());
        assertEquals("APPROVAL", decisionTask.getDecisionType());
    }

    @Test
    void testConsolidationWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createConsolidationWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("File Consolidation Workflow", dto.getName());
        
        // Verify consolidation task configuration
        WorkflowConfigTaskDto consolidationTask = dto.getTasks().get(2);
        assertEquals(WorkflowConfigTask.TaskType.CONSOLIDATE_FILE, consolidationTask.getTaskType());
        // Note: Consolidation-specific fields will be tested when DTO is enhanced
    }

    @Test
    void testRevisitationWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createRevisitationWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("Revisitation Workflow", dto.getName());
        
        // Verify revisitation configuration
        WorkflowConfigTaskDto revisitableTask = dto.getTasks().get(1);
        assertEquals(WorkflowConfigTask.TaskType.FILE_UPDATE, revisitableTask.getTaskType());
        assertEquals("Y", revisitableTask.getCanBeRevisited());
        assertEquals(3, revisitableTask.getMaxRevisits());
    }

    @Test
    void testFileDependencyWorkflowConfiguration() {
        // Given
        WorkflowConfigDto dto = createFileDependencyWorkflowDto();

        // Then
        assertNotNull(dto);
        assertEquals("File Dependency Workflow", dto.getName());
        
        // Verify file dependency configuration
        WorkflowConfigTaskDto updateTask = dto.getTasks().get(1);
        WorkflowConfigTaskDto consolidationTask = dto.getTasks().get(2);
        
        assertEquals("1", updateTask.getFileSourceTaskIds()); // Depends on task 1
        assertEquals("1,2", consolidationTask.getFileSourceTaskIds()); // Depends on tasks 1 and 2
        assertEquals("N", updateTask.getAllowNewFiles()); // Cannot add new files
        assertEquals("N", consolidationTask.getAllowNewFiles()); // Cannot add new files
    }

    // Helper methods to create test data
    private WorkflowConfigDto createSimpleUploadWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("Simple Document Upload");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName("Upload Document");
        task.setTaskType(WorkflowConfigTask.TaskType.FILE_UPLOAD);
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
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createTaskDto("Review", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 2L),
            createTaskDto("Process", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 2L),
            createTaskDto("Consolidate", WorkflowConfigTask.TaskType.CONSOLIDATE_FILE, 3, 3L),
            createTaskDto("Approve", WorkflowConfigTask.TaskType.DECISION, 4, 4L)
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
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createParallelTaskDto("Process A", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 2L),
            createParallelTaskDto("Process B", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 3L),
            createTaskDto("Consolidate", WorkflowConfigTask.TaskType.CONSOLIDATE_FILE, 3, 4L)
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
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createTaskDto("Review", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 2L),
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
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createTaskDto("Update", WorkflowConfigTask.TaskType.FILE_UPDATE, 2, 2L),
            createTaskDto("Consolidate", WorkflowConfigTask.TaskType.CONSOLIDATE_FILE, 3, 3L)
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
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createRevisitableTaskDto("Review", 2, 2L),
            createTaskDto("Approve", WorkflowConfigTask.TaskType.DECISION, 3, 3L)
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigDto createFileDependencyWorkflowDto() {
        WorkflowConfigDto dto = new WorkflowConfigDto();
        dto.setName("File Dependency Workflow");
        dto.setIsActive("Y");
        dto.setCalendarId(1L);
        
        List<WorkflowConfigTaskDto> tasks = Arrays.asList(
            createTaskDto("Upload", WorkflowConfigTask.TaskType.FILE_UPLOAD, 1, 1L),
            createFileDependentTaskDto("Update", 2, 2L, "1"),
            createFileDependentTaskDto("Consolidate", 3, 3L, "1,2")
        );
        
        dto.setTasks(tasks);
        return dto;
    }

    private WorkflowConfigTaskDto createTaskDto(String name, WorkflowConfigTask.TaskType taskType, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
        task.setName(name);
        task.setTaskType(taskType);
        task.setSequenceOrder(sequenceOrder);
        task.setRoleId(roleId);
        return task;
    }

    private WorkflowConfigTaskDto createParallelTaskDto(String name, WorkflowConfigTask.TaskType taskType, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = createTaskDto(name, taskType, sequenceOrder, roleId);
        task.setCanRunInParallel("Y");
        task.setParallelTaskIds("2,3");
        return task;
    }

    private WorkflowConfigTaskDto createDecisionTaskDto(String name, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = createTaskDto(name, WorkflowConfigTask.TaskType.DECISION, sequenceOrder, roleId);
        task.setIsDecisionTask("Y");
        task.setDecisionType("APPROVAL");
        return task;
    }

    private WorkflowConfigTaskDto createRevisitableTaskDto(String name, int sequenceOrder, Long roleId) {
        WorkflowConfigTaskDto task = createTaskDto(name, WorkflowConfigTask.TaskType.FILE_UPDATE, sequenceOrder, roleId);
        task.setCanBeRevisited("Y");
        task.setMaxRevisits(3);
        return task;
    }

    private WorkflowConfigTaskDto createFileDependentTaskDto(String name, int sequenceOrder, Long roleId, String fileSourceTaskIds) {
        WorkflowConfigTaskDto task = createTaskDto(name, WorkflowConfigTask.TaskType.FILE_UPDATE, sequenceOrder, roleId);
        task.setFileSourceTaskIds(fileSourceTaskIds);
        task.setAllowNewFiles("N");
        return task;
    }
}
