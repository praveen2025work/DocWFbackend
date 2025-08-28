package com.docwf.service;

import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceTaskDto;
import com.docwf.dto.TaskInstanceDecisionOutcomeDto;

import java.util.List;

/**
 * Service for handling complex workflow execution with file dependencies,
 * consolidation, and decision-based routing with revisitation capabilities
 */
public interface ComplexWorkflowExecutionService {
    
    /**
     * Execute a workflow step with file dependencies
     */
    WorkflowInstanceTaskDto executeTaskStep(Long instanceTaskId, String action, String comments);
    
    /**
     * Get available files for a task based on source task dependencies
     */
    List<Object> getAvailableFilesForTask(Long instanceTaskId);
    
    /**
     * Consolidate files based on consolidation rules
     */
    WorkflowInstanceTaskDto consolidateFiles(Long instanceTaskId, List<Long> selectedFileIds, String consolidationNotes);
    
    /**
     * Make a decision and route to appropriate tasks
     */
    WorkflowInstanceTaskDto makeDecision(Long instanceTaskId, String decision, String comments, String nextAction);
    
    /**
     * Route workflow based on decision outcome
     */
    void routeWorkflowBasedOnDecision(Long instanceId, Long decisionTaskId, String decision, String nextAction);
    
    /**
     * Open revision tasks based on decision outcome
     */
    void openRevisionTasks(Long instanceId, List<Long> taskIds, String revisionStrategy);
    
    /**
     * Get file dependency tree for a workflow instance
     */
    Object getFileDependencyTree(Long instanceId);
    
    /**
     * Validate file dependencies before task execution
     */
    boolean validateFileDependencies(Long instanceTaskId);
    
    /**
     * Get consolidation preview for a task
     */
    Object getConsolidationPreview(Long instanceTaskId);
    
    /**
     * Execute auto-consolidation based on rules
     */
    WorkflowInstanceTaskDto executeAutoConsolidation(Long instanceTaskId);
    
    /**
     * Get decision routing options for a task
     */
    List<TaskInstanceDecisionOutcomeDto> getDecisionRoutingOptions(Long instanceTaskId);
    
    /**
     * Check if a task can be revised
     */
    boolean canTaskBeRevised(Long instanceTaskId);
    
    /**
     * Get revision history for a workflow instance
     */
    List<Object> getRevisionHistory(Long instanceId);
}
