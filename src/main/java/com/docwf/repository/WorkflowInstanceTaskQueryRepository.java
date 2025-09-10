package com.docwf.repository;

import com.docwf.entity.WorkflowInstanceTaskQuery;
import com.docwf.entity.WorkflowInstanceTaskQuery.QueryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowInstanceTaskQueryRepository extends JpaRepository<WorkflowInstanceTaskQuery, Long> {
    
    /**
     * Find all queries for a specific task instance
     */
    List<WorkflowInstanceTaskQuery> findByInstanceTaskInstanceTaskIdOrderByCreatedAtDesc(Long instanceTaskId);
    
    /**
     * Find all queries for a specific task instance with pagination
     */
    Page<WorkflowInstanceTaskQuery> findByInstanceTaskInstanceTaskIdOrderByCreatedAtDesc(Long instanceTaskId, Pageable pageable);
    
    /**
     * Find all queries raised by a specific user
     */
    List<WorkflowInstanceTaskQuery> findByRaisedByUserIdOrderByCreatedAtDesc(Long raisedByUserId);
    
    /**
     * Find all queries assigned to a specific user
     */
    List<WorkflowInstanceTaskQuery> findByAssignedToUserIdOrderByCreatedAtDesc(Long assignedToUserId);
    
    /**
     * Find all queries by status
     */
    List<WorkflowInstanceTaskQuery> findByQueryStatusOrderByCreatedAtDesc(QueryStatus queryStatus);
    
    /**
     * Find all queries by priority
     */
    List<WorkflowInstanceTaskQuery> findByPriorityOrderByCreatedAtDesc(String priority);
    
    /**
     * Find all queries for a specific workflow instance
     */
    @Query("SELECT q FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.instanceTask.instanceTaskId IN " +
           "(SELECT t.instanceTaskId FROM WorkflowInstanceTask t WHERE t.workflowInstance.instanceId = :instanceId) " +
           "ORDER BY q.createdAt DESC")
    List<WorkflowInstanceTaskQuery> findByWorkflowInstanceId(@Param("instanceId") Long instanceId);
    
    /**
     * Find all queries for a specific workflow instance with pagination
     */
    @Query("SELECT q FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.instanceTask.instanceTaskId IN " +
           "(SELECT t.instanceTaskId FROM WorkflowInstanceTask t WHERE t.workflowInstance.instanceId = :instanceId) " +
           "ORDER BY q.createdAt DESC")
    Page<WorkflowInstanceTaskQuery> findByWorkflowInstanceId(@Param("instanceId") Long instanceId, Pageable pageable);
    
    /**
     * Find all open queries assigned to a user
     */
    List<WorkflowInstanceTaskQuery> findByAssignedToUserIdAndQueryStatusOrderByCreatedAtDesc(Long assignedToUserId, QueryStatus queryStatus);
    
    /**
     * Find all high priority queries
     */
    @Query("SELECT q FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.priority IN ('HIGH', 'URGENT') " +
           "ORDER BY q.createdAt DESC")
    List<WorkflowInstanceTaskQuery> findHighPriorityQueries();
    
    /**
     * Count queries by status for a specific user
     */
    @Query("SELECT COUNT(q) FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.assignedToUserId = :userId AND q.queryStatus = :status")
    Long countByAssignedToUserIdAndStatus(@Param("userId") Long userId, @Param("status") QueryStatus status);
    
    /**
     * Count open queries for a specific user
     */
    @Query("SELECT COUNT(q) FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.assignedToUserId = :userId AND q.queryStatus IN ('OPEN', 'IN_PROGRESS')")
    Long countOpenQueriesForUser(@Param("userId") Long userId);
    
    /**
     * Find queries that need attention (open or in progress for more than specified hours)
     */
    @Query("SELECT q FROM WorkflowInstanceTaskQuery q " +
           "WHERE q.queryStatus IN ('OPEN', 'IN_PROGRESS') " +
           "AND q.createdAt < :thresholdDate " +
           "ORDER BY q.priority DESC, q.createdAt ASC")
    List<WorkflowInstanceTaskQuery> findQueriesNeedingAttention(@Param("thresholdDate") java.time.LocalDateTime thresholdDate);
}
