package com.docwf.repository;

import com.docwf.entity.WorkflowInstanceTaskFile;
import com.docwf.entity.WorkflowInstanceTaskFileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowInstanceTaskFileRepository extends JpaRepository<WorkflowInstanceTaskFile, WorkflowInstanceTaskFileId> {
    
    /**
     * Find all versions of a specific file by instance file ID
     */
    @Query("SELECT f FROM WorkflowInstanceTaskFile f WHERE f.instanceFileId = :instanceFileId ORDER BY f.version DESC")
    List<WorkflowInstanceTaskFile> findAllVersionsByInstanceFileId(@Param("instanceFileId") Long instanceFileId);
    
    /**
     * Find the latest version of a specific file by instance file ID
     */
    @Query("SELECT f FROM WorkflowInstanceTaskFile f WHERE f.instanceFileId = :instanceFileId ORDER BY f.version DESC LIMIT 1")
    Optional<WorkflowInstanceTaskFile> findLatestVersionByInstanceFileId(@Param("instanceFileId") Long instanceFileId);
    
    /**
     * Find a specific version of a file by instance file ID and version
     */
    Optional<WorkflowInstanceTaskFile> findByInstanceFileIdAndVersion(Long instanceFileId, Integer version);
    
    /**
     * Find all files for a specific task instance
     */
    List<WorkflowInstanceTaskFile> findByInstanceTaskInstanceTaskId(Long instanceTaskId);
    
    /**
     * Find the latest version of all files for a specific task instance
     */
    @Query("SELECT f1 FROM WorkflowInstanceTaskFile f1 " +
           "WHERE f1.instanceTask.instanceTaskId = :instanceTaskId " +
           "AND f1.version = (SELECT MAX(f2.version) FROM WorkflowInstanceTaskFile f2 WHERE f2.instanceFileId = f1.instanceFileId) " +
           "ORDER BY f1.instanceFileId")
    List<WorkflowInstanceTaskFile> findLatestVersionsByInstanceTaskId(@Param("instanceTaskId") Long instanceTaskId);
    
    /**
     * Get the maximum version number for a specific instance file ID
     */
    @Query("SELECT MAX(f.version) FROM WorkflowInstanceTaskFile f WHERE f.instanceFileId = :instanceFileId")
    Optional<Integer> findMaxVersionByInstanceFileId(@Param("instanceFileId") Long instanceFileId);
    
    /**
     * Find files by action type
     */
    List<WorkflowInstanceTaskFile> findByActionType(WorkflowInstanceTaskFile.ActionType actionType);
    
    /**
     * Find files by status
     */
    List<WorkflowInstanceTaskFile> findByFileStatus(String fileStatus);
    
    /**
     * Find files created by a specific user
     */
    List<WorkflowInstanceTaskFile> findByCreatedBy(String createdBy);
}
