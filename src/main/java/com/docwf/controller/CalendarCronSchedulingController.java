package com.docwf.controller;

import com.docwf.service.CalendarCronSchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing cron-based calendar scheduling
 */
@RestController
@RequestMapping("/api/calendar/cron")
@Tag(name = "Calendar Cron Scheduling", description = "APIs for managing cron-based calendar scheduling")
public class CalendarCronSchedulingController {
    
    @Autowired
    private CalendarCronSchedulingService cronSchedulingService;
    
    @PostMapping("/schedule/{calendarId}")
    @Operation(summary = "Schedule calendar for cron execution", description = "Schedules a calendar for cron-based execution")
    public ResponseEntity<String> scheduleCalendar(
            @Parameter(description = "Calendar ID to schedule") @PathVariable Long calendarId) {
        try {
            cronSchedulingService.scheduleCalendar(calendarId);
            return ResponseEntity.ok("Calendar " + calendarId + " scheduled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to schedule calendar: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/unschedule/{calendarId}")
    @Operation(summary = "Unschedule calendar from cron execution", description = "Removes a calendar from cron-based execution")
    public ResponseEntity<String> unscheduleCalendar(
            @Parameter(description = "Calendar ID to unschedule") @PathVariable Long calendarId) {
        try {
            cronSchedulingService.unscheduleCalendar(calendarId);
            return ResponseEntity.ok("Calendar " + calendarId + " unscheduled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to unschedule calendar: " + e.getMessage());
        }
    }
    
    @PutMapping("/reschedule/{calendarId}")
    @Operation(summary = "Reschedule calendar with updated cron expression", description = "Reschedules a calendar with updated cron configuration")
    public ResponseEntity<String> rescheduleCalendar(
            @Parameter(description = "Calendar ID to reschedule") @PathVariable Long calendarId) {
        try {
            cronSchedulingService.rescheduleCalendar(calendarId);
            return ResponseEntity.ok("Calendar " + calendarId + " rescheduled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to reschedule calendar: " + e.getMessage());
        }
    }
    
    @PostMapping("/schedule-all")
    @Operation(summary = "Schedule all active calendars with cron expressions", description = "Schedules all active calendars that have cron expressions")
    public ResponseEntity<String> scheduleAllActiveCalendars() {
        try {
            cronSchedulingService.scheduleAllActiveCalendars();
            return ResponseEntity.ok("All active calendars scheduled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to schedule all calendars: " + e.getMessage());
        }
    }
    
    @GetMapping("/scheduled")
    @Operation(summary = "Get all scheduled calendar IDs", description = "Retrieves list of all scheduled calendar IDs")
    public ResponseEntity<List<Long>> getScheduledCalendarIds() {
        try {
            List<Long> scheduledIds = cronSchedulingService.getScheduledCalendarIds();
            return ResponseEntity.ok(scheduledIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/status/{calendarId}")
    @Operation(summary = "Check if calendar is scheduled", description = "Checks if a calendar is currently scheduled")
    public ResponseEntity<Boolean> isCalendarScheduled(
            @Parameter(description = "Calendar ID to check") @PathVariable Long calendarId) {
        try {
            boolean isScheduled = cronSchedulingService.isCalendarScheduled(calendarId);
            return ResponseEntity.ok(isScheduled);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/next-execution/{calendarId}")
    @Operation(summary = "Get next execution time for calendar", description = "Gets the next scheduled execution time for a calendar")
    public ResponseEntity<String> getNextExecutionTime(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            String nextExecution = cronSchedulingService.getNextExecutionTime(calendarId);
            return ResponseEntity.ok(nextExecution != null ? nextExecution : "Not scheduled");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting next execution time: " + e.getMessage());
        }
    }
    
    @PostMapping("/pause/{calendarId}")
    @Operation(summary = "Pause calendar execution", description = "Pauses execution of a scheduled calendar")
    public ResponseEntity<String> pauseCalendar(
            @Parameter(description = "Calendar ID to pause") @PathVariable Long calendarId) {
        try {
            cronSchedulingService.pauseCalendar(calendarId);
            return ResponseEntity.ok("Calendar " + calendarId + " paused successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to pause calendar: " + e.getMessage());
        }
    }
    
    @PostMapping("/resume/{calendarId}")
    @Operation(summary = "Resume calendar execution", description = "Resumes execution of a paused calendar")
    public ResponseEntity<String> resumeCalendar(
            @Parameter(description = "Calendar ID to resume") @PathVariable Long calendarId) {
        try {
            cronSchedulingService.resumeCalendar(calendarId);
            return ResponseEntity.ok("Calendar " + calendarId + " resumed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resume calendar: " + e.getMessage());
        }
    }
    
    @GetMapping("/execution-status/{calendarId}")
    @Operation(summary = "Get calendar execution status", description = "Gets the current execution status of a calendar")
    public ResponseEntity<String> getCalendarExecutionStatus(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            String status = cronSchedulingService.getCalendarExecutionStatus(calendarId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting execution status: " + e.getMessage());
        }
    }
}
