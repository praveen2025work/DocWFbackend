package com.docwf.repository;

import com.docwf.entity.WorkflowConfigTaskFileDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowConfigTaskFileDependencyRepository extends JpaRepository<WorkflowConfigTaskFileDependency, Long> {
    
    /**
     * Find all dependencies for a specific file
     */
    List<WorkflowConfigTaskFileDependency> findByFileId(Long fileId);
    
    /**
     * Find all files that depend on a specific parent file
     */
    List<WorkflowConfigTaskFileDependency> findByParentFileFileId(Long parentFileId);
    
    /**
     * Find all dependencies for files in a specific task
     */
    @Query("SELECT d FROM WorkflowConfigTaskFileDependency d JOIN WorkflowConfigTaskFile f ON d.fileId = f.fileId WHERE f.task.taskId = :taskId")
    List<WorkflowConfigTaskFileDependency> findByTaskId(@Param("taskId") Long taskId);
    
    /**
     * Find all dependencies for files in a specific workflow
     */
    @Query("SELECT d FROM WorkflowConfigTaskFileDependency d JOIN WorkflowConfigTaskFile f ON d.fileId = f.fileId JOIN WorkflowConfigTask t ON f.task.taskId = t.taskId WHERE t.workflow.workflowId = :workflowId")
    List<WorkflowConfigTaskFileDependency> findByWorkflowId(@Param("workflowId") Long workflowId);
    
    /**
     * Check if a file has any dependencies
     */
    boolean existsByFileId(Long fileId);
    
    /**
     * Check if a file is a parent of any other files
     */
    boolean existsByParentFileFileId(Long parentFileId);
    
    /**
     * Delete all dependencies for a specific file
     */
    void deleteByFileId(Long fileId);
    
    /**
     * Delete all dependencies where a file is a parent
     */
    void deleteByParentFileFileId(Long parentFileId);
}
