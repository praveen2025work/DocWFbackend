package com.docwf.repository;

import com.docwf.entity.TaskInstanceDecisionOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskInstanceDecisionOutcomeRepository extends JpaRepository<TaskInstanceDecisionOutcome, Long> {
    
    /**
     * Find all decision outcomes for a specific instance task
     */
    List<TaskInstanceDecisionOutcome> findByInstanceTaskInstanceTaskId(Long instanceTaskId);
    
    /**
     * Find decision outcomes by outcome name
     */
    List<TaskInstanceDecisionOutcome> findByOutcomeName(String outcomeName);
    
    /**
     * Find decision outcomes created by a specific user
     */
    List<TaskInstanceDecisionOutcome> findByCreatedBy(String createdBy);
    
    /**
     * Find decision outcomes for tasks in a specific workflow instance
     */
    @Query("SELECT tido FROM TaskInstanceDecisionOutcome tido WHERE tido.instanceTask.workflowInstance.instanceId = :instanceId")
    List<TaskInstanceDecisionOutcome> findByWorkflowInstanceId(@Param("instanceId") Long instanceId);
    
    /**
     * Find decision outcomes by date range
     */
    @Query("SELECT tido FROM TaskInstanceDecisionOutcome tido WHERE tido.createdAt BETWEEN :startDate AND :endDate")
    List<TaskInstanceDecisionOutcome> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                                           @Param("endDate") java.time.LocalDateTime endDate);
}
