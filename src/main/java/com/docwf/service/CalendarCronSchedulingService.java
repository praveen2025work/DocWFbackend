package com.docwf.service;

import com.docwf.entity.WorkflowCalendar;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Service for managing cron-based calendar scheduling
 */
public interface CalendarCronSchedulingService {
    
    /**
     * Schedule a calendar for cron-based execution
     * @param calendarId The calendar ID to schedule
     * @throws SchedulerException if scheduling fails
     */
    void scheduleCalendar(Long calendarId) throws SchedulerException;
    
    /**
     * Unschedule a calendar from cron execution
     * @param calendarId The calendar ID to unschedule
     * @throws SchedulerException if unscheduling fails
     */
    void unscheduleCalendar(Long calendarId) throws SchedulerException;
    
    /**
     * Reschedule a calendar with updated cron expression
     * @param calendarId The calendar ID to reschedule
     * @throws SchedulerException if rescheduling fails
     */
    void rescheduleCalendar(Long calendarId) throws SchedulerException;
    
    /**
     * Schedule all active calendars with cron expressions
     * @throws SchedulerException if scheduling fails
     */
    void scheduleAllActiveCalendars() throws SchedulerException;
    
    /**
     * Get all scheduled calendar IDs
     * @return List of scheduled calendar IDs
     * @throws SchedulerException if query fails
     */
    List<Long> getScheduledCalendarIds() throws SchedulerException;
    
    /**
     * Check if a calendar is scheduled
     * @param calendarId The calendar ID to check
     * @return true if scheduled, false otherwise
     * @throws SchedulerException if check fails
     */
    boolean isCalendarScheduled(Long calendarId) throws SchedulerException;
    
    /**
     * Get next execution time for a calendar
     * @param calendarId The calendar ID
     * @return Next execution time as string, null if not scheduled
     * @throws SchedulerException if query fails
     */
    String getNextExecutionTime(Long calendarId) throws SchedulerException;
    
    /**
     * Pause calendar execution
     * @param calendarId The calendar ID to pause
     * @throws SchedulerException if pause fails
     */
    void pauseCalendar(Long calendarId) throws SchedulerException;
    
    /**
     * Resume calendar execution
     * @param calendarId The calendar ID to resume
     * @throws SchedulerException if resume fails
     */
    void resumeCalendar(Long calendarId) throws SchedulerException;
    
    /**
     * Get calendar execution status
     * @param calendarId The calendar ID
     * @return Execution status (SCHEDULED, PAUSED, NOT_SCHEDULED, ERROR)
     * @throws SchedulerException if query fails
     */
    String getCalendarExecutionStatus(Long calendarId) throws SchedulerException;
}
