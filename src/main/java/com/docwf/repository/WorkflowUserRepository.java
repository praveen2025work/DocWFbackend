package com.docwf.repository;

import com.docwf.entity.WorkflowUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowUserRepository extends JpaRepository<WorkflowUser, Long> {
    
    /**
     * Find user by username
     */
    Optional<WorkflowUser> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<WorkflowUser> findByEmail(String email);
    
    /**
     * Find all active users
     */
    List<WorkflowUser> findByIsActive(String isActive);
    
    /**
     * Find users by escalation hierarchy
     */
    List<WorkflowUser> findByEscalationTo(WorkflowUser escalationTo);
    
    /**
     * Find users who can escalate to a specific user
     */
    @Query("SELECT u FROM WorkflowUser u WHERE u.escalationTo = :escalationTo AND u.isActive = 'Y'")
    List<WorkflowUser> findUsersWhoEscalateTo(@Param("escalationTo") WorkflowUser escalationTo);
    
    /**
     * Find users by role (through workflow config roles)
     */
    @Query("SELECT DISTINCT wcr.user FROM WorkflowConfigRole wcr WHERE wcr.role.roleName = :roleName AND wcr.isActive = 'Y'")
    List<WorkflowUser> findUsersByRoleName(@Param("roleName") String roleName);
    
    /**
     * Find users assigned to a specific workflow
     */
    @Query("SELECT DISTINCT wcr.user FROM WorkflowConfigRole wcr WHERE wcr.workflow.workflowId = :workflowId AND wcr.isActive = 'Y'")
    List<WorkflowUser> findUsersByWorkflowId(@Param("workflowId") Long workflowId);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by active status with pagination
     */
    Page<WorkflowUser> findByIsActive(String isActive, Pageable pageable);
}
