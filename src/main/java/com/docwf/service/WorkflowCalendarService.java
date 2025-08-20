package com.docwf.service;

import com.docwf.dto.WorkflowCalendarDto;
import com.docwf.dto.WorkflowCalendarDayDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkflowCalendarService {
    
    WorkflowCalendarDto createCalendar(WorkflowCalendarDto calendarDto);
    
    Optional<WorkflowCalendarDto> getCalendarById(Long calendarId);
    
    Optional<WorkflowCalendarDto> getCalendarByName(String calendarName);
    
    List<WorkflowCalendarDto> getAllCalendars();
    
    List<WorkflowCalendarDto> getCalendarsByRecurrence(String recurrence);
    
    List<WorkflowCalendarDto> getCalendarsByDateRange(LocalDate startDate, LocalDate endDate);
    
    WorkflowCalendarDto updateCalendar(Long calendarId, WorkflowCalendarDto calendarDto);
    
    void deleteCalendar(Long calendarId);
    
    // Calendar Days Management
    WorkflowCalendarDayDto addCalendarDay(Long calendarId, WorkflowCalendarDayDto dayDto);
    
    List<WorkflowCalendarDayDto> addCalendarDays(Long calendarId, List<WorkflowCalendarDayDto> daysDto);
    
    List<WorkflowCalendarDayDto> getCalendarDays(Long calendarId);
    
    List<WorkflowCalendarDayDto> getCalendarDaysByType(Long calendarId, String dayType);
    
    WorkflowCalendarDayDto updateCalendarDay(Long dayId, WorkflowCalendarDayDto dayDto);
    
    void deleteCalendarDay(Long dayId);
    
    // Calendar Validation
    boolean isDateValid(Long calendarId, LocalDate date);
    
    List<LocalDate> getValidDates(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    List<LocalDate> getHolidays(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    List<LocalDate> getRunDays(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    // Workflow Integration
    boolean canExecuteWorkflow(Long calendarId, LocalDate date);
    
    LocalDate getNextValidDate(Long calendarId, LocalDate fromDate);
    
    LocalDate getPreviousValidDate(Long calendarId, LocalDate fromDate);
}
