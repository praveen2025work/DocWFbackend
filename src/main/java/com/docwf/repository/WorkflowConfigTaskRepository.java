package com.docwf.repository;

import com.docwf.entity.WorkflowConfigTask;
import com.docwf.entity.WorkflowConfigTask.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowConfigTaskRepository extends JpaRepository<WorkflowConfigTask, Long> {
    
    /**
     * Find tasks by workflow ID
     */
    List<WorkflowConfigTask> findByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find tasks by workflow ID ordered by sequence
     */
    List<WorkflowConfigTask> findByWorkflowWorkflowIdOrderBySequenceOrderAsc(Long workflowId);
    
    /**
     * Find task by workflow ID and sequence order
     */
    Optional<WorkflowConfigTask> findByWorkflowWorkflowIdAndSequenceOrder(Long workflowId, Integer sequenceOrder);
    
    /**
     * Find tasks by task type
     */
    List<WorkflowConfigTask> findByTaskType(TaskType taskType);
    
    /**
     * Find tasks by role ID
     */
    List<WorkflowConfigTask> findByRoleRoleId(Long roleId);
    
    /**
     * Find tasks by workflow ID and role ID
     */
    List<WorkflowConfigTask> findByWorkflowWorkflowIdAndRoleRoleId(Long workflowId, Long roleId);
    
    /**
     * Find next task in sequence
     */
    @Query("SELECT t FROM WorkflowConfigTask t WHERE t.workflow.workflowId = :workflowId AND t.sequenceOrder > :currentSequence ORDER BY t.sequenceOrder ASC")
    List<WorkflowConfigTask> findNextTasks(@Param("workflowId") Long workflowId, @Param("currentSequence") Integer currentSequence);
    
    /**
     * Find previous task in sequence
     */
    @Query("SELECT t FROM WorkflowConfigTask t WHERE t.workflow.workflowId = :workflowId AND t.sequenceOrder < :currentSequence ORDER BY t.sequenceOrder DESC")
    List<WorkflowConfigTask> findPreviousTasks(@Param("workflowId") Long workflowId, @Param("currentSequence") Integer currentSequence);
    
    /**
     * Find first task in workflow
     */
    @Query("SELECT t FROM WorkflowConfigTask t WHERE t.workflow.workflowId = :workflowId ORDER BY t.sequenceOrder ASC")
    Optional<WorkflowConfigTask> findFirstTask(@Param("workflowId") Long workflowId);
    
    /**
     * Find last task in workflow
     */
    @Query("SELECT t FROM WorkflowConfigTask t WHERE t.workflow.workflowId = :workflowId ORDER BY t.sequenceOrder DESC")
    Optional<WorkflowConfigTask> findLastTask(@Param("workflowId") Long workflowId);
    
    /**
     * Find decision tasks in workflow
     */
    List<WorkflowConfigTask> findByWorkflowWorkflowIdAndTaskType(Long workflowId, TaskType taskType);
    
    /**
     * Count tasks in workflow
     */
    long countByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find tasks by workflow ID ordered by sequence order
     */
    List<WorkflowConfigTask> findByWorkflowWorkflowIdOrderBySequenceOrder(Long workflowId);
}
