package com.docwf.repository;

import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowInstanceTask.TaskInstanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowInstanceTaskRepository extends JpaRepository<WorkflowInstanceTask, Long> {
    
    /**
     * Find instance tasks by instance ID
     */
    List<WorkflowInstanceTask> findByWorkflowInstanceInstanceId(Long instanceId);
    
    /**
     * Find instance tasks by status
     */
    List<WorkflowInstanceTask> findByStatus(TaskInstanceStatus status);
    
    /**
     * Find instance tasks by instance ID and status
     */
    List<WorkflowInstanceTask> findByWorkflowInstanceInstanceIdAndStatus(Long instanceId, TaskInstanceStatus status);
    
    /**
     * Find instance tasks assigned to a specific user
     */
    List<WorkflowInstanceTask> findByAssignedToUserId(Long userId);
    
    /**
     * Find instance tasks by task ID (across all instances)
     */
    List<WorkflowInstanceTask> findByTaskTaskId(Long taskId);
    
    /**
     * Find instance tasks by instance ID and task ID
     */
    Optional<WorkflowInstanceTask> findByWorkflowInstanceInstanceIdAndTaskTaskId(Long instanceId, Long taskId);
    
    /**
     * Find pending tasks that need to be assigned
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.status = 'PENDING' AND wit.assignedTo IS NULL")
    List<WorkflowInstanceTask> findPendingTasksToAssign();
    
    /**
     * Find in-progress tasks that may need escalation
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.status = 'IN_PROGRESS' AND wit.assignedTo IS NOT NULL")
    List<WorkflowInstanceTask> findInProgressTasksForEscalation();
    
    /**
     * Find tasks started within a date range
     */
    List<WorkflowInstanceTask> findByStartedOnBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find tasks completed within a date range
     */
    List<WorkflowInstanceTask> findByCompletedOnBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find tasks that have exceeded expected completion time
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.status = 'IN_PROGRESS' AND t.expectedCompletion IS NOT NULL")
    List<WorkflowInstanceTask> findTasksExceedingExpectedCompletion();
    
    /**
     * Find next task in sequence for an instance
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.workflowInstance.instanceId = :instanceId AND t.sequenceOrder > :currentSequence ORDER BY t.sequenceOrder ASC")
    List<WorkflowInstanceTask> findNextTasksInSequence(@Param("instanceId") Long instanceId, @Param("currentSequence") Integer currentSequence);
    
    /**
     * Find previous task in sequence for an instance
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.workflowInstance.instanceId = :instanceId AND t.sequenceOrder < :currentSequence ORDER BY t.sequenceOrder DESC")
    List<WorkflowInstanceTask> findPreviousTasksInSequence(@Param("instanceId") Long instanceId, @Param("currentSequence") Integer currentSequence);
    
    /**
     * Count tasks by status for an instance
     */
    long countByWorkflowInstanceInstanceIdAndStatus(Long instanceId, TaskInstanceStatus status);
    
    /**
     * Count completed tasks for an instance
     */
    @Query("SELECT COUNT(wit) FROM WorkflowInstanceTask wit WHERE wit.workflowInstance.instanceId = :instanceId AND wit.status = 'COMPLETED'")
    long countCompletedTasksByInstanceId(@Param("instanceId") Long instanceId);
    
    /**
     * Find first pending task for an instance
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.workflowInstance.instanceId = :instanceId AND wit.status = 'PENDING' ORDER BY t.sequenceOrder ASC")
    Optional<WorkflowInstanceTask> findFirstPendingTask(@Param("instanceId") Long instanceId);
    
    /**
     * Find pending tasks by assigned user ID
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.assignedTo.userId = :userId AND wit.status = 'PENDING'")
    List<WorkflowInstanceTask> findPendingTasksByAssignedToUserId(@Param("userId") Long userId);
    
    /**
     * Find overdue tasks
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.status = 'IN_PROGRESS' AND wit.startedOn < :cutoffTime")
    List<WorkflowInstanceTask> findOverdueTasks(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
    
    /**
     * Find tasks needing attention
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.status IN ('PENDING', 'IN_PROGRESS') AND wit.startedOn < :attentionTime")
    List<WorkflowInstanceTask> findTasksNeedingAttention(@Param("attentionTime") java.time.LocalDateTime attentionTime);
}
