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
}
