package com.docwf.repository;

import com.docwf.entity.WorkflowConfigTaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowConfigTaskFileRepository extends JpaRepository<WorkflowConfigTaskFile, Long> {
    
    /**
     * Find all files for a specific task
     */
    List<WorkflowConfigTaskFile> findByTaskTaskId(Long taskId);
    
    /**
     * Find files by task ID and action type
     */
    List<WorkflowConfigTaskFile> findByTaskTaskIdAndActionType(Long taskId, WorkflowConfigTaskFile.ActionType actionType);
    
    /**
     * Find files by task ID and file status
     */
    List<WorkflowConfigTaskFile> findByTaskTaskIdAndFileStatus(Long taskId, String fileStatus);
    
    /**
     * Find files by task ID and required flag
     */
    List<WorkflowConfigTaskFile> findByTaskTaskIdAndIsRequired(Long taskId, String isRequired);
    
    /**
     * Find all files for a specific workflow
     */
    @Query("SELECT f FROM WorkflowConfigTaskFile f WHERE f.task.workflow.workflowId = :workflowId")
    List<WorkflowConfigTaskFile> findByWorkflowId(@Param("workflowId") Long workflowId);
    
    /**
     * Find files by file name pattern
     */
    List<WorkflowConfigTaskFile> findByFileNameContainingIgnoreCase(String fileName);
    
    /**
     * Find files by file type regex
     */
    @Query("SELECT f FROM WorkflowConfigTaskFile f WHERE f.fileTypeRegex = :fileTypeRegex")
    List<WorkflowConfigTaskFile> findByFileTypeRegex(@Param("fileTypeRegex") String fileTypeRegex);
    
    /**
     * Check if file exists by task ID and file name
     */
    boolean existsByTaskTaskIdAndFileName(Long taskId, String fileName);
    
    /**
     * Count files by task ID
     */
    long countByTaskTaskId(Long taskId);
    
    /**
     * Count required files by task ID
     */
    long countByTaskTaskIdAndIsRequired(Long taskId, String isRequired);
}
