package com.docwf.repository;

import com.docwf.entity.TaskDecisionOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDecisionOutcomeRepository extends JpaRepository<TaskDecisionOutcome, Long> {
    
    /**
     * Find all decision outcomes for a specific task
     */
    List<TaskDecisionOutcome> findByTaskTaskId(Long taskId);
    
    /**
     * Find decision outcomes by task ID and outcome name
     */
    List<TaskDecisionOutcome> findByTaskTaskIdAndOutcomeName(Long taskId, String outcomeName);
    
    /**
     * Find decision outcomes that target a specific task
     */
    List<TaskDecisionOutcome> findByTargetTaskId(Long targetTaskId);
    
    /**
     * Find decision outcomes by revision type
     */
    List<TaskDecisionOutcome> findByRevisionType(String revisionType);
    
    /**
     * Find decision outcomes that require escalation
     */
    List<TaskDecisionOutcome> findByAutoEscalate(String autoEscalate);
    
    /**
     * Find decision outcomes by escalation role
     */
    List<TaskDecisionOutcome> findByEscalationRoleId(Long escalationRoleId);
    
    /**
     * Delete all decision outcomes for a specific task
     */
    void deleteByTaskTaskId(Long taskId);
    
    /**
     * Find decision outcomes with specific revision task IDs
     */
    @Query("SELECT o FROM TaskDecisionOutcome o WHERE o.revisionTaskIds LIKE %:taskId%")
    List<TaskDecisionOutcome> findByRevisionTaskIdsContaining(@Param("taskId") String taskId);
}
