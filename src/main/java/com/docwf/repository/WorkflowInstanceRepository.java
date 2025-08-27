package com.docwf.repository;

import com.docwf.entity.WorkflowInstance;
import com.docwf.entity.WorkflowInstance.InstanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {
    
    /**
     * Find instances by workflow ID
     */
    List<WorkflowInstance> findByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find instances by status
     */
    List<WorkflowInstance> findByStatus(InstanceStatus status);
    
    /**
     * Find instances by workflow ID and status
     */
    List<WorkflowInstance> findByWorkflowWorkflowIdAndStatus(Long workflowId, InstanceStatus status);
    
    /**
     * Find instances by workflow ID and multiple statuses
     */
    List<WorkflowInstance> findByWorkflowWorkflowIdAndStatusIn(Long workflowId, List<InstanceStatus> statuses);
    
    /**
     * Dynamic search for workflow instances with multiple criteria
     */
    @Query("SELECT wi FROM WorkflowInstance wi WHERE " +
           "(:workflowId IS NULL OR wi.workflow.workflowId = :workflowId) AND " +
           "(:status IS NULL OR wi.status = :status) AND " +
           "(:startedBy IS NULL OR wi.startedBy.userId = :startedBy) AND " +
           "(:startedAfter IS NULL OR wi.startedOn >= :startedAfter) AND " +
           "(:startedBefore IS NULL OR wi.startedOn <= :startedBefore) AND " +
           "(:completedAfter IS NULL OR wi.completedOn >= :completedAfter) AND " +
           "(:completedBefore IS NULL OR wi.completedOn <= :completedBefore)")
    Page<WorkflowInstance> searchInstances(@Param("workflowId") Long workflowId,
                                          @Param("status") InstanceStatus status,
                                          @Param("startedBy") Long startedBy,
                                          @Param("startedAfter") java.time.LocalDateTime startedAfter,
                                          @Param("startedBefore") java.time.LocalDateTime startedBefore,
                                          @Param("completedAfter") java.time.LocalDateTime completedAfter,
                                          @Param("completedBefore") java.time.LocalDateTime completedBefore,
                                          Pageable pageable);
    
    /**
     * Find instances started by a specific user
     */
    List<WorkflowInstance> findByStartedByUserId(Long userId);
    
    /**
     * Find instances escalated to a specific user
     */
    List<WorkflowInstance> findByEscalatedToUserId(Long userId);
    
    /**
     * Find instances started within a date range
     */
    List<WorkflowInstance> findByStartedOnBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find instances completed within a date range
     */
    List<WorkflowInstance> findByCompletedOnBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find pending instances that need to be started
     */
    @Query("SELECT wi FROM WorkflowInstance wi WHERE wi.status = 'PENDING' AND wi.startedOn IS NULL")
    List<WorkflowInstance> findPendingInstancesToStart();
    
    /**
     * Find in-progress instances that may need escalation
     */
    @Query("SELECT wi FROM WorkflowInstance wi WHERE wi.status = 'IN_PROGRESS' AND wi.escalatedTo IS NULL")
    List<WorkflowInstance> findInProgressInstancesForEscalation();
    
    /**
     * Find instances that need reminders (based on workflow config)
     */
    @Query("SELECT wi FROM WorkflowInstance wi JOIN wi.workflow w WHERE wi.status = 'IN_PROGRESS' AND w.reminderBeforeDueMins IS NOT NULL")
    List<WorkflowInstance> findInstancesNeedingReminders();
    
    /**
     * Find instances that need escalation (based on workflow config)
     */
    @Query("SELECT wi FROM WorkflowInstance wi JOIN wi.workflow w WHERE wi.status = 'IN_PROGRESS' AND w.escalationAfterMins IS NOT NULL")
    List<WorkflowInstance> findInstancesNeedingEscalation();
    
    /**
     * Count instances by status
     */
    long countByStatus(InstanceStatus status);
    
    /**
     * Count instances by workflow ID
     */
    long countByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find latest instance for a workflow
     */
    @Query("SELECT wi FROM WorkflowInstance wi WHERE wi.workflow.workflowId = :workflowId ORDER BY wi.startedOn DESC")
    Optional<WorkflowInstance> findLatestInstanceByWorkflowId(@Param("workflowId") Long workflowId);
    
    // Process Owner specific queries
    /**
     * Find workflow instances where the user has a process owner role
     */
    @Query("SELECT DISTINCT wi FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND (:status IS NULL OR wi.status = :status)")
    List<WorkflowInstance> findByProcessOwnerRole(@Param("processOwnerId") Long processOwnerId, @Param("status") InstanceStatus status);
    
    /**
     * Find workflow instances that need process owner attention (escalated, overdue, or stuck)
     */
    @Query("SELECT DISTINCT wi FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND (wi.status = 'IN_PROGRESS' OR wi.escalatedTo IS NOT NULL)")
    List<WorkflowInstance> findInstancesNeedingProcessOwnerAttention(@Param("processOwnerId") Long processOwnerId);
    
    /**
     * Find overdue workflow instances for a process owner
     */
    @Query("SELECT DISTINCT wi FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wi.status = 'IN_PROGRESS' " +
           "AND wi.startedOn < :overdueThreshold")
    List<WorkflowInstance> findOverdueInstancesForProcessOwner(@Param("processOwnerId") Long processOwnerId, @Param("overdueThreshold") LocalDateTime overdueThreshold);
    
    // Dashboard specific queries
    /**
     * Count total workflows for a process owner
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y'")
    long countTotalWorkflowsByProcessOwner(@Param("processOwnerId") Long processOwnerId);
    
    /**
     * Count active workflows for a process owner
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wi.status IN ('PENDING', 'IN_PROGRESS')")
    long countActiveWorkflowsByProcessOwner(@Param("processOwnerId") Long processOwnerId);
    
    /**
     * Count completed workflows today for a process owner
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wi.status = 'COMPLETED' " +
           "AND DATE(wi.completedOn) = CURRENT_DATE")
    long countCompletedWorkflowsTodayByProcessOwner(@Param("processOwnerId") Long processOwnerId);
    
    /**
     * Count escalated workflows for a process owner
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "JOIN wir.role wr " +
           "WHERE wir.user.userId = :processOwnerId " +
           "AND wr.roleName = 'PROCESS_OWNER' " +
           "AND wir.isActive = 'Y' " +
           "AND wi.escalatedTo IS NOT NULL")
    long countEscalatedWorkflowsByProcessOwner(@Param("processOwnerId") Long processOwnerId);
    
    // User Dashboard specific queries
    /**
     * Count total workflows for a user
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "WHERE wi.startedBy.userId = :userId")
    long countTotalWorkflowsByUser(@Param("userId") Long userId);
    
    /**
     * Count active workflows for a user
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "WHERE wi.startedBy.userId = :userId " +
           "AND wi.status IN ('PENDING', 'IN_PROGRESS')")
    long countActiveWorkflowsByUser(@Param("userId") Long userId);
    
    /**
     * Count completed workflows today for a user
     */
    @Query("SELECT COUNT(DISTINCT wi) FROM WorkflowInstance wi " +
           "WHERE wi.startedBy.userId = :userId " +
           "AND wi.status = 'COMPLETED' " +
           "AND DATE(wi.completedOn) = CURRENT_DATE")
    long countCompletedWorkflowsTodayByUser(@Param("userId") Long userId);
    
    /**
     * Find workflows where user is participating (started by or assigned to)
     */
    @Query("SELECT DISTINCT wi FROM WorkflowInstance wi " +
           "JOIN wi.instanceRoles wir " +
           "WHERE wir.user.userId = :userId " +
           "AND wir.isActive = 'Y' " +
           "AND (:status IS NULL OR wi.status = :status)")
    List<WorkflowInstance> findWorkflowsByUserParticipation(@Param("userId") Long userId, @Param("status") InstanceStatus status);
}
