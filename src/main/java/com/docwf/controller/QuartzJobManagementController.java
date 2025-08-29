package com.docwf.controller;

import com.docwf.dto.QuartzJobStatusDto;
import com.docwf.dto.JobTriggerRequestDto;
import com.docwf.service.CalendarJobManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing Quartz jobs including monitoring, retriggering, and job control operations.
 * Provides endpoints to check job status, retrigger stuck jobs, and manage job execution.
 */
@RestController
@RequestMapping("/api/quartz-jobs")
@Tag(name = "Quartz Job Management", description = "APIs for monitoring and managing Quartz scheduled jobs")
public class QuartzJobManagementController {

    @Autowired
    private CalendarJobManagementService calendarJobManagementService;

    // ===== JOB STATUS MONITORING =====

    /**
     * Get all Quartz job statuses
     */
    @GetMapping("/status")
    @Operation(summary = "Get All Job Statuses", description = "Retrieves status of all Quartz jobs in the system")
    public ResponseEntity<List<QuartzJobStatusDto>> getAllJobStatuses() {
        List<QuartzJobStatusDto> jobStatuses = calendarJobManagementService.getAllJobStatuses();
        return ResponseEntity.ok(jobStatuses);
    }

    /**
     * Get status of a specific job by name
     */
    @GetMapping("/status/{jobName}")
    @Operation(summary = "Get Job Status by Name", description = "Retrieves status of a specific Quartz job by name")
    public ResponseEntity<QuartzJobStatusDto> getJobStatusByName(
            @Parameter(description = "Job name") @PathVariable String jobName) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.getJobStatusByName(jobName);
        return ResponseEntity.ok(jobStatus);
    }

    /**
     * Get status of jobs by group
     */
    @GetMapping("/status/group/{groupName}")
    @Operation(summary = "Get Job Statuses by Group", description = "Retrieves status of all jobs in a specific group")
    public ResponseEntity<List<QuartzJobStatusDto>> getJobStatusesByGroup(
            @Parameter(description = "Job group name") @PathVariable String groupName) {
        List<QuartzJobStatusDto> jobStatuses = calendarJobManagementService.getJobStatusesByGroup(groupName);
        return ResponseEntity.ok(jobStatuses);
    }

    /**
     * Get stuck jobs (jobs that haven't completed in expected time)
     */
    @GetMapping("/stuck")
    @Operation(summary = "Get Stuck Jobs", description = "Retrieves list of jobs that appear to be stuck or running too long")
    public ResponseEntity<List<QuartzJobStatusDto>> getStuckJobs() {
        List<QuartzJobStatusDto> stuckJobs = calendarJobManagementService.getStuckJobs();
        return ResponseEntity.ok(stuckJobs);
    }

    /**
     * Get failed jobs
     */
    @GetMapping("/failed")
    @Operation(summary = "Get Failed Jobs", description = "Retrieves list of jobs that have failed execution")
    public ResponseEntity<List<QuartzJobStatusDto>> getFailedJobs() {
        List<QuartzJobStatusDto> failedJobs = calendarJobManagementService.getFailedJobs();
        return ResponseEntity.ok(failedJobs);
    }

    // ===== JOB CONTROL OPERATIONS =====

    /**
     * Retrigger a stuck or failed job
     */
    @PostMapping("/retrigger/{jobName}")
    @Operation(summary = "Retrigger Job", description = "Retriggers a specific job that is stuck or failed")
    public ResponseEntity<QuartzJobStatusDto> retriggerJob(
            @Parameter(description = "Job name to retrigger") @PathVariable String jobName,
            @Parameter(description = "Retrigger reason") @RequestParam(required = false) String reason) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.retriggerJob(jobName, reason);
        return ResponseEntity.ok(jobStatus);
    }

    /**
     * Retrigger multiple jobs
     */
    @PostMapping("/retrigger/batch")
    @Operation(summary = "Retrigger Multiple Jobs", description = "Retriggers multiple jobs in batch")
    public ResponseEntity<List<QuartzJobStatusDto>> retriggerMultipleJobs(
            @Parameter(description = "Job names to retrigger") @RequestBody List<String> jobNames,
            @Parameter(description = "Retrigger reason") @RequestParam(required = false) String reason) {
        List<QuartzJobStatusDto> jobStatuses = calendarJobManagementService.retriggerMultipleJobs(jobNames, reason);
        return ResponseEntity.ok(jobStatuses);
    }

    /**
     * Pause a job
     */
    @PostMapping("/pause/{jobName}")
    @Operation(summary = "Pause Job", description = "Pauses a specific job from executing")
    public ResponseEntity<QuartzJobStatusDto> pauseJob(
            @Parameter(description = "Job name to pause") @PathVariable String jobName) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.pauseJob(jobName);
        return ResponseEntity.ok(jobStatus);
    }

    /**
     * Resume a paused job
     */
    @PostMapping("/resume/{jobName}")
    @Operation(summary = "Resume Job", description = "Resumes a previously paused job")
    public ResponseEntity<QuartzJobStatusDto> resumeJob(
            @Parameter(description = "Job name to resume") @PathVariable String jobName) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.resumeJob(jobName);
        return ResponseEntity.ok(jobStatus);
    }

    /**
     * Stop a running job
     */
    @PostMapping("/stop/{jobName}")
    @Operation(summary = "Stop Job", description = "Stops a currently running job")
    public ResponseEntity<QuartzJobStatusDto> stopJob(
            @Parameter(description = "Job name to stop") @PathVariable String jobName) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.stopJob(jobName);
        return ResponseEntity.ok(jobStatus);
    }

    // ===== SCHEDULER MANAGEMENT =====

    /**
     * Get scheduler status
     */
    @GetMapping("/scheduler/status")
    @Operation(summary = "Get Scheduler Status", description = "Retrieves overall Quartz scheduler status")
    public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
        Map<String, Object> schedulerStatus = calendarJobManagementService.getSchedulerStatus();
        return ResponseEntity.ok(schedulerStatus);
    }

    /**
     * Pause all jobs
     */
    @PostMapping("/scheduler/pause")
    @Operation(summary = "Pause All Jobs", description = "Pauses all jobs in the scheduler")
    public ResponseEntity<Map<String, Object>> pauseAllJobs() {
        Map<String, Object> result = calendarJobManagementService.pauseAllJobs();
        return ResponseEntity.ok(result);
    }

    /**
     * Resume all jobs
     */
    @PostMapping("/scheduler/resume")
    @Operation(summary = "Resume All Jobs", description = "Resumes all paused jobs in the scheduler")
    public ResponseEntity<Map<String, Object>> resumeAllJobs() {
        Map<String, Object> result = calendarJobManagementService.resumeAllJobs();
        return ResponseEntity.ok(result);
    }

    /**
     * Shutdown scheduler gracefully
     */
    @PostMapping("/scheduler/shutdown")
    @Operation(summary = "Shutdown Scheduler", description = "Gracefully shuts down the Quartz scheduler")
    public ResponseEntity<Map<String, Object>> shutdownScheduler() {
        Map<String, Object> result = calendarJobManagementService.shutdownScheduler();
        return ResponseEntity.ok(result);
    }

    /**
     * Start scheduler
     */
    @PostMapping("/scheduler/start")
    @Operation(summary = "Start Scheduler", description = "Starts the Quartz scheduler")
    public ResponseEntity<Map<String, Object>> startScheduler() {
        Map<String, Object> result = calendarJobManagementService.startScheduler();
        return ResponseEntity.ok(result);
    }

    // ===== JOB EXECUTION HISTORY =====

    /**
     * Get job execution history
     */
    @GetMapping("/history/{jobName}")
    @Operation(summary = "Get Job Execution History", description = "Retrieves execution history for a specific job")
    public ResponseEntity<List<Map<String, Object>>> getJobExecutionHistory(
            @Parameter(description = "Job name") @PathVariable String jobName,
            @Parameter(description = "Number of executions to retrieve") @RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> history = calendarJobManagementService.getJobExecutionHistory(jobName, limit);
        return ResponseEntity.ok(history);
    }

    /**
     * Get job execution statistics
     */
    @GetMapping("/statistics/{jobName}")
    @Operation(summary = "Get Job Statistics", description = "Retrieves execution statistics for a specific job")
    public ResponseEntity<Map<String, Object>> getJobStatistics(
            @Parameter(description = "Job name") @PathVariable String jobName,
            @Parameter(description = "Time period for statistics (e.g., '1h', '1d', '1w')") @RequestParam(defaultValue = "1d") String period) {
        Map<String, Object> statistics = calendarJobManagementService.getJobStatistics(jobName, period);
        return ResponseEntity.ok(statistics);
    }

    // ===== WORKFLOW-SPECIFIC JOB MANAGEMENT =====

    /**
     * Get workflow execution job status
     */
    @GetMapping("/workflow-execution/status")
    @Operation(summary = "Get Workflow Execution Job Status", description = "Retrieves status of workflow execution jobs")
    public ResponseEntity<List<QuartzJobStatusDto>> getWorkflowExecutionJobStatus() {
        List<QuartzJobStatusDto> jobStatuses = calendarJobManagementService.getWorkflowExecutionJobStatus();
        return ResponseEntity.ok(jobStatuses);
    }

    /**
     * Retrigger workflow execution for a specific calendar
     */
    @PostMapping("/workflow-execution/retrigger")
    @Operation(summary = "Retrigger Workflow Execution", description = "Retriggers workflow execution for a specific calendar")
    public ResponseEntity<Map<String, Object>> retriggerWorkflowExecution(
            @Parameter(description = "Calendar ID") @RequestParam Long calendarId,
            @Parameter(description = "Workflow ID") @RequestParam Long workflowId) {
        Map<String, Object> result = calendarJobManagementService.retriggerWorkflowExecution(calendarId, workflowId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get calendar-specific job status
     */
    @GetMapping("/calendar/{calendarId}/jobs")
    @Operation(summary = "Get Calendar Jobs Status", description = "Retrieves status of jobs related to a specific calendar")
    public ResponseEntity<List<QuartzJobStatusDto>> getCalendarJobStatus(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        List<QuartzJobStatusDto> jobStatuses = calendarJobManagementService.getCalendarJobStatus(calendarId);
        return ResponseEntity.ok(jobStatuses);
    }

    // ===== JOB CONFIGURATION =====

    /**
     * Update job schedule
     */
    @PutMapping("/schedule/{jobName}")
    @Operation(summary = "Update Job Schedule", description = "Updates the schedule for a specific job")
    public ResponseEntity<QuartzJobStatusDto> updateJobSchedule(
            @Parameter(description = "Job name") @PathVariable String jobName,
            @Parameter(description = "New cron expression") @RequestParam String cronExpression) {
        QuartzJobStatusDto jobStatus = calendarJobManagementService.updateJobSchedule(jobName, cronExpression);
        return ResponseEntity.ok(jobStatus);
    }

    /**
     * Get job configuration
     */
    @GetMapping("/config/{jobName}")
    @Operation(summary = "Get Job Configuration", description = "Retrieves configuration details for a specific job")
    public ResponseEntity<Map<String, Object>> getJobConfiguration(
            @Parameter(description = "Job name") @PathVariable String jobName) {
        Map<String, Object> config = calendarJobManagementService.getJobConfiguration(jobName);
        return ResponseEntity.ok(config);
    }

    // ===== HEALTH CHECK =====

    /**
     * Health check for Quartz jobs
     */
    @GetMapping("/health")
    @Operation(summary = "Job Health Check", description = "Performs health check on all Quartz jobs")
    public ResponseEntity<Map<String, Object>> getJobHealthStatus() {
        Map<String, Object> healthStatus = calendarJobManagementService.getJobHealthStatus();
        return ResponseEntity.ok(healthStatus);
    }
}
