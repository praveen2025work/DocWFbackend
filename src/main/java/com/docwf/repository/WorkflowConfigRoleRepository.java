package com.docwf.repository;

import com.docwf.entity.WorkflowConfigRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowConfigRoleRepository extends JpaRepository<WorkflowConfigRole, Long> {
    
    /**
     * Find all config roles for a specific workflow
     */
    List<WorkflowConfigRole> findByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find all config roles for a specific role
     */
    List<WorkflowConfigRole> findByRoleRoleId(Long roleId);
    
    /**
     * Find all config roles for a specific user
     */
    List<WorkflowConfigRole> findByUserUserId(Long userId);
    
    /**
     * Find active config roles for a workflow
     */
    List<WorkflowConfigRole> findByWorkflowWorkflowIdAndIsActive(Long workflowId, String isActive);
    
    /**
     * Find config role by workflow, role, and user
     */
    WorkflowConfigRole findByWorkflowWorkflowIdAndRoleRoleIdAndUserUserId(Long workflowId, Long roleId, Long userId);
    
    /**
     * Check if a user has a specific role in a workflow
     */
    boolean existsByWorkflowWorkflowIdAndRoleRoleIdAndUserUserIdAndIsActive(Long workflowId, Long roleId, Long userId, String isActive);
    
    /**
     * Find all workflows where a user has a specific role
     */
    @Query("SELECT wcr.workflow.workflowId FROM WorkflowConfigRole wcr WHERE wcr.user.userId = :userId AND wcr.role.roleId = :roleId AND wcr.isActive = 'Y'")
    List<Long> findWorkflowIdsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * Find all users with a specific role in a workflow
     */
    @Query("SELECT wcr.user.userId FROM WorkflowConfigRole wcr WHERE wcr.workflow.workflowId = :workflowId AND wcr.role.roleId = :roleId AND wcr.isActive = 'Y'")
    List<Long> findUserIdsByWorkflowIdAndRoleId(@Param("workflowId") Long workflowId, @Param("roleId") Long roleId);
}
