package com.docwf.service;

import com.docwf.dto.CreateTaskQueryDto;
import com.docwf.dto.UpdateQueryStatusDto;
import com.docwf.dto.WorkflowInstanceTaskQueryDto;
import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowInstanceTaskQuery;
import com.docwf.entity.WorkflowInstanceTaskQuery.QueryStatus;
import com.docwf.repository.WorkflowInstanceTaskQueryRepository;
import com.docwf.repository.WorkflowInstanceTaskRepository;
import com.docwf.repository.WorkflowUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkflowInstanceTaskQueryService {
    
    @Autowired
    private WorkflowInstanceTaskQueryRepository queryRepository;
    
    @Autowired
    private WorkflowInstanceTaskRepository taskRepository;
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    /**
     * Create a new query for a task
     */
    public WorkflowInstanceTaskQueryDto createQuery(CreateTaskQueryDto createDto) {
        // Validate that the task exists
        WorkflowInstanceTask task = taskRepository.findById(createDto.getInstanceTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + createDto.getInstanceTaskId()));
        
        // Validate that users exist
        userRepository.findById(createDto.getRaisedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + createDto.getRaisedByUserId()));
        
        userRepository.findById(createDto.getAssignedToUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + createDto.getAssignedToUserId()));
        
        // Create the query entity
        WorkflowInstanceTaskQuery query = new WorkflowInstanceTaskQuery();
        query.setInstanceTask(task);
        query.setQueryTitle(createDto.getQueryTitle());
        query.setQueryDescription(createDto.getQueryDescription());
        query.setRaisedByUserId(createDto.getRaisedByUserId());
        query.setAssignedToUserId(createDto.getAssignedToUserId());
        query.setPriority(createDto.getPriority());
        query.setCreatedBy(createDto.getCreatedBy());
        query.setQueryStatus(QueryStatus.OPEN);
        
        WorkflowInstanceTaskQuery savedQuery = queryRepository.save(query);
        return convertToDto(savedQuery);
    }
    
    /**
     * Get all queries for a specific task
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getQueriesForTask(Long instanceTaskId) {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findByInstanceTaskInstanceTaskIdOrderByCreatedAtDesc(instanceTaskId);
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all queries for a specific task with pagination
     */
    @Transactional(readOnly = true)
    public Page<WorkflowInstanceTaskQueryDto> getQueriesForTask(Long instanceTaskId, Pageable pageable) {
        Page<WorkflowInstanceTaskQuery> queries = queryRepository.findByInstanceTaskInstanceTaskIdOrderByCreatedAtDesc(instanceTaskId, pageable);
        return queries.map(this::convertToDto);
    }
    
    /**
     * Get all queries for a workflow instance
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getQueriesForWorkflowInstance(Long instanceId) {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findByWorkflowInstanceId(instanceId);
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all queries for a workflow instance with pagination
     */
    @Transactional(readOnly = true)
    public Page<WorkflowInstanceTaskQueryDto> getQueriesForWorkflowInstance(Long instanceId, Pageable pageable) {
        Page<WorkflowInstanceTaskQuery> queries = queryRepository.findByWorkflowInstanceId(instanceId, pageable);
        return queries.map(this::convertToDto);
    }
    
    /**
     * Get all queries assigned to a user
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getQueriesAssignedToUser(Long userId) {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findByAssignedToUserIdOrderByCreatedAtDesc(userId);
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all queries raised by a user
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getQueriesRaisedByUser(Long userId) {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findByRaisedByUserIdOrderByCreatedAtDesc(userId);
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all open queries assigned to a user
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getOpenQueriesForUser(Long userId) {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findByAssignedToUserIdAndQueryStatusOrderByCreatedAtDesc(userId, QueryStatus.OPEN);
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all high priority queries
     */
    @Transactional(readOnly = true)
    public List<WorkflowInstanceTaskQueryDto> getHighPriorityQueries() {
        List<WorkflowInstanceTaskQuery> queries = queryRepository.findHighPriorityQueries();
        return queries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update query status
     */
    public WorkflowInstanceTaskQueryDto updateQueryStatus(Long queryId, UpdateQueryStatusDto updateDto) {
        WorkflowInstanceTaskQuery query = queryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with ID: " + queryId));
        
        query.setQueryStatus(updateDto.getQueryStatus());
        query.setUpdatedBy(updateDto.getUpdatedBy());
        
        // If resolving the query, set resolution details
        if (QueryStatus.RESOLVED.equals(updateDto.getQueryStatus()) || QueryStatus.CLOSED.equals(updateDto.getQueryStatus())) {
            query.setResolutionNotes(updateDto.getResolutionNotes());
            query.setResolvedByUserId(updateDto.getUpdatedByUserId());
            query.setResolvedAt(LocalDateTime.now());
        }
        
        WorkflowInstanceTaskQuery savedQuery = queryRepository.save(query);
        return convertToDto(savedQuery);
    }
    
    /**
     * Get query by ID
     */
    @Transactional(readOnly = true)
    public WorkflowInstanceTaskQueryDto getQueryById(Long queryId) {
        WorkflowInstanceTaskQuery query = queryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with ID: " + queryId));
        return convertToDto(query);
    }
    
    /**
     * Delete a query
     */
    public void deleteQuery(Long queryId) {
        if (!queryRepository.existsById(queryId)) {
            throw new RuntimeException("Query not found with ID: " + queryId);
        }
        queryRepository.deleteById(queryId);
    }
    
    /**
     * Get query statistics for a user
     */
    @Transactional(readOnly = true)
    public QueryStatisticsDto getQueryStatisticsForUser(Long userId) {
        Long openQueries = queryRepository.countOpenQueriesForUser(userId);
        Long resolvedQueries = queryRepository.countByAssignedToUserIdAndStatus(userId, QueryStatus.RESOLVED);
        Long closedQueries = queryRepository.countByAssignedToUserIdAndStatus(userId, QueryStatus.CLOSED);
        
        return new QueryStatisticsDto(userId, openQueries, resolvedQueries, closedQueries);
    }
    
    /**
     * Convert entity to DTO
     */
    private WorkflowInstanceTaskQueryDto convertToDto(WorkflowInstanceTaskQuery query) {
        WorkflowInstanceTaskQueryDto dto = new WorkflowInstanceTaskQueryDto();
        dto.setQueryId(query.getQueryId());
        dto.setInstanceTaskId(query.getInstanceTask().getInstanceTaskId());
        dto.setQueryTitle(query.getQueryTitle());
        dto.setQueryDescription(query.getQueryDescription());
        dto.setRaisedByUserId(query.getRaisedByUserId());
        dto.setAssignedToUserId(query.getAssignedToUserId());
        dto.setQueryStatus(query.getQueryStatus());
        dto.setPriority(query.getPriority());
        dto.setResolutionNotes(query.getResolutionNotes());
        dto.setResolvedByUserId(query.getResolvedByUserId());
        dto.setResolvedAt(query.getResolvedAt());
        dto.setCreatedBy(query.getCreatedBy());
        dto.setCreatedAt(query.getCreatedAt());
        dto.setUpdatedBy(query.getUpdatedBy());
        dto.setUpdatedAt(query.getUpdatedAt());
        
        // Set related data
        dto.setTaskName(query.getInstanceTask().getTask().getName());
        dto.setWorkflowName(query.getInstanceTask().getWorkflowInstance().getWorkflow().getName());
        dto.setInstanceId(query.getInstanceTask().getWorkflowInstance().getInstanceId());
        
        // Set usernames (you might want to fetch these from user service)
        // For now, we'll set them as null and let the controller handle it
        dto.setRaisedByUsername(null);
        dto.setAssignedToUsername(null);
        dto.setResolvedByUsername(null);
        
        return dto;
    }
    
    /**
     * Inner class for query statistics
     */
    public static class QueryStatisticsDto {
        private Long userId;
        private Long openQueries;
        private Long resolvedQueries;
        private Long closedQueries;
        
        public QueryStatisticsDto(Long userId, Long openQueries, Long resolvedQueries, Long closedQueries) {
            this.userId = userId;
            this.openQueries = openQueries;
            this.resolvedQueries = resolvedQueries;
            this.closedQueries = closedQueries;
        }
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getOpenQueries() { return openQueries; }
        public void setOpenQueries(Long openQueries) { this.openQueries = openQueries; }
        public Long getResolvedQueries() { return resolvedQueries; }
        public void setResolvedQueries(Long resolvedQueries) { this.resolvedQueries = resolvedQueries; }
        public Long getClosedQueries() { return closedQueries; }
        public void setClosedQueries(Long closedQueries) { this.closedQueries = closedQueries; }
    }
}
