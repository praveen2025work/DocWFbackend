package com.docwf.repository;

import com.docwf.entity.WorkflowRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRoleRepository extends JpaRepository<WorkflowRole, Long> {
    
    /**
     * Find role by role name
     */
    Optional<WorkflowRole> findByRoleName(String roleName);
    
    /**
     * Find all active roles
     */
    List<WorkflowRole> findByIsActive(String isActive);
    
    /**
     * Find roles by workflow (through workflow config roles)
     */
    @Query("SELECT DISTINCT wcr.role FROM WorkflowConfigRole wcr WHERE wcr.workflow.workflowId = :workflowId AND wcr.isActive = 'Y'")
    List<WorkflowRole> findRolesByWorkflowId(@Param("workflowId") Long workflowId);
    
    /**
     * Find roles assigned to a specific user
     */
    @Query("SELECT DISTINCT wcr.role FROM WorkflowConfigRole wcr WHERE wcr.user.userId = :userId AND wcr.isActive = 'Y'")
    List<WorkflowRole> findRolesByUserId(@Param("userId") Long userId);
    
    /**
     * Check if role name exists
     */
    boolean existsByRoleName(String roleName);
    
    /**
     * Find roles by multiple role names
     */
    List<WorkflowRole> findByRoleNameIn(List<String> roleNames);
}
