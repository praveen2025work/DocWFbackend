package com.docwf.repository;

import com.docwf.entity.WorkflowConfigParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowConfigParamRepository extends JpaRepository<WorkflowConfigParam, Long> {
    
    /**
     * Find all parameters for a specific workflow
     */
    List<WorkflowConfigParam> findByWorkflowWorkflowId(Long workflowId);
    
    /**
     * Find parameter by workflow and key
     */
    Optional<WorkflowConfigParam> findByWorkflowWorkflowIdAndParamKey(Long workflowId, String paramKey);
    
    /**
     * Find parameters by key across all workflows
     */
    List<WorkflowConfigParam> findByParamKey(String paramKey);
    
    /**
     * Find parameters by value pattern
     */
    List<WorkflowConfigParam> findByParamValueContaining(String valuePattern);
    
    /**
     * Find parameters created by a specific user
     */
    List<WorkflowConfigParam> findByCreatedBy(String createdBy);
    
    /**
     * Find parameters updated by a specific user
     */
    List<WorkflowConfigParam> findByUpdatedBy(String updatedBy);
    
    /**
     * Check if a parameter exists for a workflow
     */
    boolean existsByWorkflowWorkflowIdAndParamKey(Long workflowId, String paramKey);
    
    /**
     * Find parameters by workflow and key pattern
     */
    @Query("SELECT wcp FROM WorkflowConfigParam wcp WHERE wcp.workflow.workflowId = :workflowId AND wcp.paramKey LIKE %:keyPattern%")
    List<WorkflowConfigParam> findByWorkflowIdAndParamKeyPattern(@Param("workflowId") Long workflowId, @Param("keyPattern") String keyPattern);
    
    /**
     * Find all parameter keys for a workflow
     */
    @Query("SELECT wcp.paramKey FROM WorkflowConfigParam wcp WHERE wcp.workflow.workflowId = :workflowId")
    List<String> findParamKeysByWorkflowId(@Param("workflowId") Long workflowId);
    
    /**
     * Delete all parameters for a workflow
     */
    void deleteByWorkflowWorkflowId(Long workflowId);
}
