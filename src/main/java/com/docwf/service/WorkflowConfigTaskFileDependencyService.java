package com.docwf.service;

import com.docwf.dto.WorkflowConfigTaskFileDependencyDto;
import com.docwf.entity.WorkflowConfigTaskFileDependency;

import java.util.List;

public interface WorkflowConfigTaskFileDependencyService {
    
    /**
     * Create a new file dependency
     */
    WorkflowConfigTaskFileDependencyDto createDependency(WorkflowConfigTaskFileDependencyDto dependencyDto);
    
    /**
     * Create multiple dependencies using sequence-based mapping
     */
    List<WorkflowConfigTaskFileDependencyDto> createDependenciesWithSequenceMapping(
            List<WorkflowConfigTaskFileDependencyDto> dependencyDtos, 
            List<Long> fileIds);
    
    /**
     * Get all dependencies for a file
     */
    List<WorkflowConfigTaskFileDependencyDto> getDependenciesByFileId(Long fileId);
    
    /**
     * Get all files that depend on a parent file
     */
    List<WorkflowConfigTaskFileDependencyDto> getDependenciesByParentFileId(Long parentFileId);
    
    /**
     * Get all dependencies for a task
     */
    List<WorkflowConfigTaskFileDependencyDto> getDependenciesByTaskId(Long taskId);
    
    /**
     * Get all dependencies for a workflow
     */
    List<WorkflowConfigTaskFileDependencyDto> getDependenciesByWorkflowId(Long workflowId);
    
    /**
     * Update a dependency
     */
    WorkflowConfigTaskFileDependencyDto updateDependency(Long dependencyId, WorkflowConfigTaskFileDependencyDto dependencyDto);
    
    /**
     * Delete a dependency
     */
    void deleteDependency(Long dependencyId);
    
    /**
     * Delete all dependencies for a file
     */
    void deleteDependenciesByFileId(Long fileId);
    
    /**
     * Delete all dependencies where a file is a parent
     */
    void deleteDependenciesByParentFileId(Long parentFileId);
    
    /**
     * Validate dependency relationships to prevent circular dependencies
     */
    boolean validateDependencyChain(Long fileId, Long parentFileId);
    
    /**
     * Get dependency chain for a file (all files it depends on)
     */
    List<Long> getDependencyChain(Long fileId);
}
