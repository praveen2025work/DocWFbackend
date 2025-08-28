package com.docwf.service;

import com.docwf.entity.WorkflowCalendar;

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
    String getCalendarJobStatus(Long calendarId);
    
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
    java.util.List<String> getActiveCalendarJobNames();
}
