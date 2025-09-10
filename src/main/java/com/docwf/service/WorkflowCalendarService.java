package com.docwf.service;

import com.docwf.dto.WorkflowCalendarDto;
import com.docwf.dto.WorkflowCalendarDayDto;
import com.docwf.dto.CreateCalendarWithDaysDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkflowCalendarService {
    
    WorkflowCalendarDto createCalendar(WorkflowCalendarDto calendarDto);
    
    WorkflowCalendarDto createCalendarWithDays(CreateCalendarWithDaysDto calendarWithDaysDto);
    
    Optional<WorkflowCalendarDto> getCalendarById(Long calendarId);
    
    Optional<WorkflowCalendarDto> getCalendarByName(String calendarName);
    
    List<WorkflowCalendarDto> getAllCalendars();
    
    // Add pagination support
    Page<WorkflowCalendarDto> getAllCalendars(String recurrence, Pageable pageable);
    
    // Add search method with pagination
    Page<WorkflowCalendarDto> searchCalendars(String calendarName, String description, String recurrence, 
                                            String createdBy, String startDate, String endDate, 
                                            String createdAfter, String createdBefore, Pageable pageable);
    
    List<WorkflowCalendarDto> getCalendarsByRecurrence(String recurrence);
    
    List<WorkflowCalendarDto> getCalendarsByDateRange(LocalDate startDate, LocalDate endDate);
    
    WorkflowCalendarDto updateCalendar(Long calendarId, WorkflowCalendarDto calendarDto);
    
    void deleteCalendar(Long calendarId);
    
    // Calendar Days Management
    WorkflowCalendarDayDto addCalendarDay(Long calendarId, WorkflowCalendarDayDto dayDto);
    
    List<WorkflowCalendarDayDto> addCalendarDays(Long calendarId, List<WorkflowCalendarDayDto> daysDto);
    
    // Add batch method
    List<WorkflowCalendarDayDto> addCalendarDaysBatch(Long calendarId, List<WorkflowCalendarDayDto> daysDto);
    
    List<WorkflowCalendarDayDto> getCalendarDays(Long calendarId);
    
    List<WorkflowCalendarDayDto> getCalendarDaysByType(Long calendarId, String dayType);
    
    WorkflowCalendarDayDto updateCalendarDay(Long dayId, WorkflowCalendarDayDto dayDto);
    
    void deleteCalendarDay(Long dayId);
    
    // Calendar Validation
    boolean isDateValid(Long calendarId, LocalDate date);
    
    List<LocalDate> getValidDates(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    // Add range methods
    List<LocalDate> getValidDatesInRange(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    List<LocalDate> getHolidays(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    // Add range methods
    List<WorkflowCalendarDayDto> getHolidaysInRange(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    List<LocalDate> getRunDays(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    // Add range methods
    List<WorkflowCalendarDayDto> getRunDaysInRange(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    // Workflow Integration
    boolean canExecuteWorkflow(Long calendarId, LocalDate date);
    
    LocalDate getNextValidDate(Long calendarId, LocalDate fromDate);
    
    LocalDate getPreviousValidDate(Long calendarId, LocalDate fromDate);
    
    // Cron and Region Management
    List<WorkflowCalendarDto> getCalendarsByRegion(String region);
    
    List<WorkflowCalendarDto> getActiveCalendarsWithCron();
    
    List<WorkflowCalendarDto> getCalendarsByTimezone(String timezone);
    
    List<WorkflowCalendarDto> getCalendarsWithOffset(Integer offsetDays);
    
    // Calendar Entity Access (for internal use)
    com.docwf.entity.WorkflowCalendar getCalendarEntityById(Long calendarId);
    
    List<Long> getWorkflowIdsForCalendar(Long calendarId);
    
    // Calendar Status Management
    void activateCalendar(Long calendarId);
    
    void deactivateCalendar(Long calendarId);
    
    List<WorkflowCalendarDto> getActiveCalendars();
    
    List<WorkflowCalendarDto> getInactiveCalendars();
    
    // Cron Expression Validation
    boolean isValidCronExpression(String cronExpression);
    
    String getCronDescription(String cronExpression);
    
    // Scheduler integration methods
    boolean isDateValidForExecution(Long calendarId, LocalDate date);
    
    void scheduleCalendarWorkflow(com.docwf.entity.WorkflowCalendar calendar);
    
    void unscheduleCalendarWorkflow(Long calendarId);
    
    void updateCalendarSchedule(com.docwf.entity.WorkflowCalendar calendar);
    
    /**
     * Check if a calendar is currently scheduled
     * @param calendarId The calendar ID to check
     * @return true if the calendar is scheduled
     */
    boolean isCalendarScheduled(Long calendarId);
    
    /**
     * Get the next execution time for a calendar
     * @param calendarId The calendar ID
     * @return The next execution time as a string, or null if not scheduled
     */
    String getNextExecutionTime(Long calendarId);
    
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
