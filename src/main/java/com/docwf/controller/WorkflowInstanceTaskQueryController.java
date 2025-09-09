package com.docwf.controller;

import com.docwf.dto.CreateTaskQueryDto;
import com.docwf.dto.UpdateQueryStatusDto;
import com.docwf.dto.WorkflowInstanceTaskQueryDto;
import com.docwf.service.WorkflowInstanceTaskQueryService;
import com.docwf.service.WorkflowInstanceTaskQueryService.QueryStatisticsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
@Tag(name = "Workflow Task Queries", description = "APIs for managing task queries and chat functionality")
public class WorkflowInstanceTaskQueryController {
    
    @Autowired
    private WorkflowInstanceTaskQueryService queryService;
    
    @PostMapping
    @Operation(summary = "Create a new task query", description = "Create a new query/chat for a specific task")
    public ResponseEntity<WorkflowInstanceTaskQueryDto> createQuery(@Valid @RequestBody CreateTaskQueryDto createDto) {
        WorkflowInstanceTaskQueryDto query = queryService.createQuery(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(query);
    }
    
    @GetMapping("/{queryId}")
    @Operation(summary = "Get query by ID", description = "Retrieve a specific query by its ID")
    public ResponseEntity<WorkflowInstanceTaskQueryDto> getQueryById(
            @Parameter(description = "Query ID") @PathVariable Long queryId) {
        WorkflowInstanceTaskQueryDto query = queryService.getQueryById(queryId);
        return ResponseEntity.ok(query);
    }
    
    @GetMapping("/task/{instanceTaskId}")
    @Operation(summary = "Get all queries for a task", description = "Retrieve all queries for a specific task instance")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getQueriesForTask(
            @Parameter(description = "Instance Task ID") @PathVariable Long instanceTaskId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (page == 0 && size == Integer.MAX_VALUE) {
            // Return all queries without pagination
            List<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesForTask(instanceTaskId);
            return ResponseEntity.ok(queries);
        } else {
            Page<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesForTask(instanceTaskId, pageable);
            return ResponseEntity.ok(queries.getContent());
        }
    }
    
    @GetMapping("/workflow/{instanceId}")
    @Operation(summary = "Get all queries for a workflow instance", description = "Retrieve all queries for a specific workflow instance")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getQueriesForWorkflowInstance(
            @Parameter(description = "Workflow Instance ID") @PathVariable Long instanceId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (page == 0 && size == Integer.MAX_VALUE) {
            // Return all queries without pagination
            List<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesForWorkflowInstance(instanceId);
            return ResponseEntity.ok(queries);
        } else {
            Page<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesForWorkflowInstance(instanceId, pageable);
            return ResponseEntity.ok(queries.getContent());
        }
    }
    
    @GetMapping("/assigned-to/{userId}")
    @Operation(summary = "Get queries assigned to user", description = "Retrieve all queries assigned to a specific user")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getQueriesAssignedToUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesAssignedToUser(userId);
        return ResponseEntity.ok(queries);
    }
    
    @GetMapping("/raised-by/{userId}")
    @Operation(summary = "Get queries raised by user", description = "Retrieve all queries raised by a specific user")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getQueriesRaisedByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowInstanceTaskQueryDto> queries = queryService.getQueriesRaisedByUser(userId);
        return ResponseEntity.ok(queries);
    }
    
    @GetMapping("/open/{userId}")
    @Operation(summary = "Get open queries for user", description = "Retrieve all open queries assigned to a specific user")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getOpenQueriesForUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowInstanceTaskQueryDto> queries = queryService.getOpenQueriesForUser(userId);
        return ResponseEntity.ok(queries);
    }
    
    @GetMapping("/high-priority")
    @Operation(summary = "Get high priority queries", description = "Retrieve all high priority queries across the system")
    public ResponseEntity<List<WorkflowInstanceTaskQueryDto>> getHighPriorityQueries() {
        List<WorkflowInstanceTaskQueryDto> queries = queryService.getHighPriorityQueries();
        return ResponseEntity.ok(queries);
    }
    
    @PutMapping("/{queryId}/status")
    @Operation(summary = "Update query status", description = "Update the status of a query (OPEN, IN_PROGRESS, RESOLVED, CLOSED)")
    public ResponseEntity<WorkflowInstanceTaskQueryDto> updateQueryStatus(
            @Parameter(description = "Query ID") @PathVariable Long queryId,
            @Valid @RequestBody UpdateQueryStatusDto updateDto) {
        WorkflowInstanceTaskQueryDto query = queryService.updateQueryStatus(queryId, updateDto);
        return ResponseEntity.ok(query);
    }
    
    @DeleteMapping("/{queryId}")
    @Operation(summary = "Delete query", description = "Delete a specific query")
    public ResponseEntity<Void> deleteQuery(
            @Parameter(description = "Query ID") @PathVariable Long queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/statistics/{userId}")
    @Operation(summary = "Get query statistics for user", description = "Get query statistics for a specific user")
    public ResponseEntity<QueryStatisticsDto> getQueryStatisticsForUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        QueryStatisticsDto statistics = queryService.getQueryStatisticsForUser(userId);
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/dashboard/{userId}")
    @Operation(summary = "Get user query dashboard", description = "Get comprehensive query dashboard for a user")
    public ResponseEntity<UserQueryDashboardDto> getUserQueryDashboard(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        // Get various query lists for the user
        List<WorkflowInstanceTaskQueryDto> assignedQueries = queryService.getQueriesAssignedToUser(userId);
        List<WorkflowInstanceTaskQueryDto> raisedQueries = queryService.getQueriesRaisedByUser(userId);
        List<WorkflowInstanceTaskQueryDto> openQueries = queryService.getOpenQueriesForUser(userId);
        QueryStatisticsDto statistics = queryService.getQueryStatisticsForUser(userId);
        
        UserQueryDashboardDto dashboard = new UserQueryDashboardDto();
        dashboard.setUserId(userId);
        dashboard.setAssignedQueries(assignedQueries);
        dashboard.setRaisedQueries(raisedQueries);
        dashboard.setOpenQueries(openQueries);
        dashboard.setStatistics(statistics);
        
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Inner class for user query dashboard
     */
    public static class UserQueryDashboardDto {
        private Long userId;
        private List<WorkflowInstanceTaskQueryDto> assignedQueries;
        private List<WorkflowInstanceTaskQueryDto> raisedQueries;
        private List<WorkflowInstanceTaskQueryDto> openQueries;
        private QueryStatisticsDto statistics;
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public List<WorkflowInstanceTaskQueryDto> getAssignedQueries() { return assignedQueries; }
        public void setAssignedQueries(List<WorkflowInstanceTaskQueryDto> assignedQueries) { this.assignedQueries = assignedQueries; }
        public List<WorkflowInstanceTaskQueryDto> getRaisedQueries() { return raisedQueries; }
        public void setRaisedQueries(List<WorkflowInstanceTaskQueryDto> raisedQueries) { this.raisedQueries = raisedQueries; }
        public List<WorkflowInstanceTaskQueryDto> getOpenQueries() { return openQueries; }
        public void setOpenQueries(List<WorkflowInstanceTaskQueryDto> openQueries) { this.openQueries = openQueries; }
        public QueryStatisticsDto getStatistics() { return statistics; }
        public void setStatistics(QueryStatisticsDto statistics) { this.statistics = statistics; }
    }
}
