package com.docwf.repository;

import com.docwf.entity.WorkflowConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowConfigRepository extends JpaRepository<WorkflowConfig, Long> {
    
    /**
     * Find workflow by name
     */
    Optional<WorkflowConfig> findByName(String name);
    
    /**
     * Find all active workflows
     */
    List<WorkflowConfig> findByIsActive(String isActive);
    
    /**
     * Find workflows by description containing text
     */
    List<WorkflowConfig> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * Find workflows that need reminders (based on due time)
     */
    @Query("SELECT w FROM WorkflowConfig w WHERE w.reminderBeforeDueMins IS NOT NULL AND w.isActive = 'Y'")
    List<WorkflowConfig> findWorkflowsWithReminders();
    
    /**
     * Find workflows that need escalation (based on escalation time)
     */
    @Query("SELECT w FROM WorkflowConfig w WHERE w.escalationAfterMins IS NOT NULL AND w.isActive = 'Y'")
    List<WorkflowConfig> findWorkflowsWithEscalation();
    
    /**
     * Find workflows by user assignment (through workflow config roles)
     */
    @Query("SELECT DISTINCT wcr.workflow FROM WorkflowConfigRole wcr WHERE wcr.user.userId = :userId AND wcr.isActive = 'Y'")
    List<WorkflowConfig> findWorkflowsByUserId(@Param("userId") Long userId);
    
    /**
     * Find workflows by role assignment
     */
    @Query("SELECT DISTINCT wcr.workflow FROM WorkflowConfigRole wcr WHERE wcr.role.roleId = :roleId AND wcr.isActive = 'Y'")
    List<WorkflowConfig> findWorkflowsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Check if workflow name exists
     */
    boolean existsByName(String name);
    
    /**
     * Find workflows by multiple names
     */
    List<WorkflowConfig> findByNameIn(List<String> names);
    
    /**
     * Find workflows that need reminders
     */
    @Query("SELECT wc FROM WorkflowConfig wc WHERE wc.isActive = 'Y' AND wc.reminderBeforeDueMins IS NOT NULL")
    List<WorkflowConfig> findWorkflowsNeedingReminders();
    
    /**
     * Find workflows that need escalation
     */
    @Query("SELECT wc FROM WorkflowConfig wc WHERE wc.isActive = 'Y' AND wc.escalationAfterMins IS NOT NULL")
    List<WorkflowConfig> findWorkflowsNeedingEscalation();
    
    /**
     * Find active workflows that have calendar assignments for execution
     */
    @Query("SELECT wc FROM WorkflowConfig wc WHERE wc.isActive = 'Y' AND wc.calendarId IS NOT NULL")
    List<WorkflowConfig> findActiveWorkflowsForExecution();
    
    /**
     * Find workflows by specific calendar ID and active status
     */
    @Query("SELECT wc FROM WorkflowConfig wc WHERE wc.calendarId = :calendarId AND wc.isActive = :isActive")
    List<WorkflowConfig> findByCalendarIdAndIsActive(@Param("calendarId") Long calendarId, @Param("isActive") String isActive);
    
    /**
     * Find workflows by active status with pagination
     */
    Page<WorkflowConfig> findByIsActive(String isActive, Pageable pageable);
    
    /**
     * Dynamic search for workflows with multiple criteria
     */
    @Query("SELECT w FROM WorkflowConfig w WHERE " +
           "(:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:description IS NULL OR LOWER(w.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:isActive IS NULL OR w.isActive = :isActive) AND " +
           "(:createdBy IS NULL OR LOWER(w.createdBy) LIKE LOWER(CONCAT('%', :createdBy, '%'))) AND " +
           "(:minDueTime IS NULL OR w.dueInMins >= :minDueTime) AND " +
           "(:maxDueTime IS NULL OR w.dueInMins <= :maxDueTime) AND " +
           "(:createdAfter IS NULL OR w.createdOn >= :createdAfter) AND " +
           "(:createdBefore IS NULL OR w.createdOn <= :createdBefore)")
    Page<WorkflowConfig> searchWorkflows(@Param("name") String name,
                                        @Param("description") String description,
                                        @Param("isActive") String isActive,
                                        @Param("createdBy") String createdBy,
                                        @Param("minDueTime") Integer minDueTime,
                                        @Param("maxDueTime") Integer maxDueTime,
                                        @Param("createdAfter") java.time.LocalDateTime createdAfter,
                                        @Param("createdBefore") java.time.LocalDateTime createdBefore,
                                        Pageable pageable);
}
