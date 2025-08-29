package com.docwf.service;

import com.docwf.dto.QuartzJobStatusDto;
import com.docwf.entity.WorkflowCalendar;

import java.util.List;
import java.util.Map;

/**
 * Service for managing calendar-specific Quartz jobs
 * Provides methods to add, remove, and update calendar jobs dynamically
 */
public interface CalendarJobManagementService {
    
    /**
     * Add a new calendar job when a calendar is created
     */
    void addCalendarJob(WorkflowCalendar calendar);
    
    /**
     * Remove a calendar job when a calendar is deleted
     */
    void removeCalendarJob(Long calendarId);
    
    /**
     * Update a calendar job when calendar configuration changes
     */
    void updateCalendarJob(WorkflowCalendar calendar);
    
    /**
     * Initialize all calendar jobs on application startup
     */
    void initializeAllCalendarJobs();
    
    /**
     * Get the status of a specific calendar job
     */
    List<QuartzJobStatusDto> getCalendarJobStatus(Long calendarId);
    
    /**
     * Pause a calendar job
     */
    void pauseCalendarJob(Long calendarId);
    
    /**
     * Resume a paused calendar job
     */
    void resumeCalendarJob(Long calendarId);
    
    /**
     * Get all active calendar job names
     */
    List<String> getActiveCalendarJobNames();
    
    // ===== QUARTZ JOB MANAGEMENT METHODS =====
    
    /**
     * Get all Quartz job statuses
     */
    List<QuartzJobStatusDto> getAllJobStatuses();
    
    /**
     * Get status of a specific job by name
     */
    QuartzJobStatusDto getJobStatusByName(String jobName);
    
    /**
     * Get status of jobs by group
     */
    List<QuartzJobStatusDto> getJobStatusesByGroup(String groupName);
    
    /**
     * Get stuck jobs (jobs that haven't completed in expected time)
     */
    List<QuartzJobStatusDto> getStuckJobs();
    
    /**
     * Get failed jobs
     */
    List<QuartzJobStatusDto> getFailedJobs();
    
    /**
     * Retrigger a stuck or failed job
     */
    QuartzJobStatusDto retriggerJob(String jobName, String reason);
    
    /**
     * Retrigger multiple jobs
     */
    List<QuartzJobStatusDto> retriggerMultipleJobs(List<String> jobNames, String reason);
    
    /**
     * Pause a job
     */
    QuartzJobStatusDto pauseJob(String jobName);
    
    /**
     * Resume a paused job
     */
    QuartzJobStatusDto resumeJob(String jobName);
    
    /**
     * Stop a running job
     */
    QuartzJobStatusDto stopJob(String jobName);
    
    /**
     * Get scheduler status
     */
    Map<String, Object> getSchedulerStatus();
    
    /**
     * Pause all jobs
     */
    Map<String, Object> pauseAllJobs();
    
    /**
     * Resume all jobs
     */
    Map<String, Object> resumeAllJobs();
    
    /**
     * Shutdown scheduler gracefully
     */
    Map<String, Object> shutdownScheduler();
    
    /**
     * Start scheduler
     */
    Map<String, Object> startScheduler();
    
    /**
     * Get job execution history
     */
    List<Map<String, Object>> getJobExecutionHistory(String jobName, Integer limit);
    
    /**
     * Get job execution statistics
     */
    Map<String, Object> getJobStatistics(String jobName, String period);
    
    /**
     * Get workflow execution job status
     */
    List<QuartzJobStatusDto> getWorkflowExecutionJobStatus();
    
    /**
     * Retrigger workflow execution for a specific calendar
     */
    Map<String, Object> retriggerWorkflowExecution(Long calendarId, Long workflowId);
    

    
    /**
     * Update job schedule
     */
    QuartzJobStatusDto updateJobSchedule(String jobName, String cronExpression);
    
    /**
     * Get job configuration
     */
    Map<String, Object> getJobConfiguration(String jobName);
    
    /**
     * Get job health status
     */
    Map<String, Object> getJobHealthStatus();
}
