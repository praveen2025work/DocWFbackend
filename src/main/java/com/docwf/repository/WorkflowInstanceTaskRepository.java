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
    
    // Process Owner specific queries
    /**
     * Find tasks for workflow instances where the user has a process owner role
     */
    @Query("SELECT DISTINCT wit FROM WorkflowInstanceTask wit " +
           "JOIN wit.workflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND (:status IS NULL OR wit.status = :status)")
    List<WorkflowInstanceTask> findTasksByProcessOwnerRole(@Param("processOwnerId") Long processOwnerId, @Param("status") TaskInstanceStatus status);
    
    /**
     * Find overdue tasks for workflow instances where the user has a process owner role
     */
    @Query("SELECT DISTINCT wit FROM WorkflowInstanceTask wit " +
           "JOIN wit.workflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wit.status = 'IN_PROGRESS' " +
           "AND wit.startedOn < :overdueThreshold")
    List<WorkflowInstanceTask> findOverdueTasksForProcessOwner(@Param("processOwnerId") Long processOwnerId, @Param("overdueThreshold") LocalDateTime overdueThreshold);
    
    // Dashboard specific queries
    /**
     * Count pending tasks for a process owner
     */
    @Query("SELECT COUNT(DISTINCT wit) FROM WorkflowInstanceTask wit " +
           "JOIN wit.workflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wit.status = 'PENDING'")
    long countPendingTasksByProcessOwner(@Param("processOwnerId") Long processOwnerId);
    
    /**
     * Find pending tasks for a process owner (limited for dashboard)
     */
    @Query("SELECT DISTINCT wit FROM WorkflowInstanceTask wit " +
           "JOIN wit.workflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wit.status = 'PENDING' " +
           "ORDER BY wit.startedOn ASC")
    List<WorkflowInstanceTask> findPendingTasksByProcessOwnerForDashboard(@Param("processOwnerId") Long processOwnerId);
    
    // User Dashboard specific queries
    /**
     * Count total tasks for a user
     */
    @Query("SELECT COUNT(DISTINCT wit) FROM WorkflowInstanceTask wit " +
           "WHERE wit.assignedTo.userId = :userId")
    long countTotalTasksByUser(@Param("userId") Long userId);
    
    /**
     * Count pending tasks for a user
     */
    @Query("SELECT COUNT(DISTINCT wit) FROM WorkflowInstanceTask wit " +
           "WHERE wit.assignedTo.userId = :userId " +
           "AND wit.status = 'PENDING'")
    long countPendingTasksByUser(@Param("userId") Long userId);
    
    /**
     * Count completed tasks today for a user
     */
    @Query("SELECT COUNT(DISTINCT wit) FROM WorkflowInstanceTask wit " +
           "WHERE wit.assignedTo.userId = :userId " +
           "AND wit.status = 'COMPLETED' " +
           "AND wit.completedOn >= :startOfDay " +
           "AND wit.completedOn < :endOfDay")
    long countCompletedTasksTodayByUser(@Param("userId") Long userId, 
                                       @Param("startOfDay") LocalDateTime startOfDay,
                                       @Param("endOfDay") LocalDateTime endOfDay);
    
    /**
     * Find recent tasks for a user (limited for dashboard)
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit " +
           "WHERE wit.assignedTo.userId = :userId " +
           "ORDER BY wit.startedOn DESC")
    List<WorkflowInstanceTask> findRecentTasksByUserForDashboard(@Param("userId") Long userId);
    
    /**
     * Count tasks by status
     */
    long countByStatus(TaskInstanceStatus status);
    
    /**
     * Find tasks by assigned user ID and date range
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.assignedTo.userId = :userId " +
           "AND ((wit.startedOn BETWEEN :startDate AND :endDate) OR " +
           "(wit.completedOn BETWEEN :startDate AND :endDate) OR " +
           "(:startDate BETWEEN wit.startedOn AND wit.completedOn))")
    List<WorkflowInstanceTask> findByAssignedToUserIdAndDateRange(@Param("userId") Long userId, 
                                                                 @Param("startDate") LocalDateTime startDate, 
                                                                 @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find tasks by assigned user ID and status
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.assignedTo.userId = :userId AND wit.status = :status")
    List<WorkflowInstanceTask> findByAssignedToUserIdAndStatus(@Param("userId") Long userId, @Param("status") TaskInstanceStatus status);
    
    /**
     * Find tasks by assigned user ID and priority
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.assignedTo.userId = :userId AND t.taskPriority = :priority")
    List<WorkflowInstanceTask> findByAssignedToUserIdAndPriority(@Param("userId") Long userId, @Param("priority") String priority);
    
    /**
     * Find tasks by assigned user ID, status and priority
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit JOIN wit.task t WHERE wit.assignedTo.userId = :userId " +
           "AND wit.status = :status AND t.taskPriority = :priority")
    List<WorkflowInstanceTask> findByAssignedToUserIdAndStatusAndPriority(@Param("userId") Long userId, 
                                                                        @Param("status") TaskInstanceStatus status, 
                                                                        @Param("priority") String priority);
    
    /**
     * Find conflicting tasks for a user in a time range
     */
    @Query("SELECT wit FROM WorkflowInstanceTask wit WHERE wit.assignedTo.userId = :userId " +
           "AND wit.status IN ('PENDING', 'IN_PROGRESS') " +
           "AND NOT (wit.completedOn <= :startTime OR wit.startedOn >= :endTime)")
    List<WorkflowInstanceTask> findConflictingTasks(@Param("userId") Long userId, 
                                                   @Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);
}
