package com.docwf.service;

import com.docwf.entity.WorkflowCalendar;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Service for managing calendar-based workflow scheduling using Quartz
 */
public interface CalendarSchedulerService {
    
    /**
     * Schedule a workflow execution job for a calendar
     * @param calendar The calendar to schedule
     * @return true if scheduling was successful
     */
    boolean scheduleCalendarWorkflow(WorkflowCalendar calendar);
    
    /**
     * Update an existing calendar workflow schedule
     * @param calendar The updated calendar
     * @return true if update was successful
     */
    boolean updateCalendarWorkflowSchedule(WorkflowCalendar calendar);
    
    /**
     * Unschedule a calendar workflow
     * @param calendarId The calendar ID to unschedule
     * @return true if unscheduling was successful
     */
    boolean unscheduleCalendarWorkflow(Long calendarId);
    
    /**
     * Check if a calendar is currently scheduled
     * @param calendarId The calendar ID to check
     * @return true if the calendar is scheduled
     */
    boolean isCalendarScheduled(Long calendarId);
    
    /**
     * Get the next execution time for a calendar
     * @param calendarId The calendar ID
     * @return The next execution time, or null if not scheduled
     */
    Date getNextExecutionTime(Long calendarId);
    
    /**
     * Pause a calendar schedule
     * @param calendarId The calendar ID to pause
     * @return true if pausing was successful
     */
    boolean pauseCalendarSchedule(Long calendarId);
    
    /**
     * Resume a paused calendar schedule
     * @param calendarId The calendar ID to resume
     * @return true if resuming was successful
     */
    boolean resumeCalendarSchedule(Long calendarId);
}
