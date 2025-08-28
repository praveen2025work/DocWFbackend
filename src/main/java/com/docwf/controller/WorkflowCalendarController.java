package com.docwf.controller;

import com.docwf.dto.WorkflowCalendarDto;
import com.docwf.dto.WorkflowCalendarDayDto;
import com.docwf.dto.CreateCalendarWithDaysDto;
import com.docwf.service.WorkflowCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendar")
@Tag(name = "Calendar Management", description = "APIs for managing workflow calendars with predicate-based search")
public class WorkflowCalendarController {
    
    @Autowired
    private WorkflowCalendarService calendarService;
    
    // ===== CALENDAR CRUD OPERATIONS =====
    
    @PostMapping
    @Operation(summary = "Create calendar", description = "Creates a new workflow calendar")
    public ResponseEntity<WorkflowCalendarDto> createCalendar(
            @Valid @RequestBody WorkflowCalendarDto calendarDto) {
        WorkflowCalendarDto createdCalendar = calendarService.createCalendar(calendarDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
    }
    
    @PostMapping("/with-days")
    @Operation(summary = "Create calendar with days", description = "Creates a new workflow calendar with calendar days in the same call")
    public ResponseEntity<WorkflowCalendarDto> createCalendarWithDays(
            @Valid @RequestBody CreateCalendarWithDaysDto calendarWithDaysDto) {
        WorkflowCalendarDto createdCalendar = calendarService.createCalendarWithDays(calendarWithDaysDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
    }
    
    @GetMapping("/{calendarId}")
    @Operation(summary = "Get calendar by ID", description = "Retrieves a calendar by its unique identifier")
    public ResponseEntity<WorkflowCalendarDto> getCalendarById(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        Optional<WorkflowCalendarDto> calendar = calendarService.getCalendarById(calendarId);
        return calendar.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{calendarId}")
    @Operation(summary = "Update calendar", description = "Updates an existing calendar")
    public ResponseEntity<WorkflowCalendarDto> updateCalendar(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Valid @RequestBody WorkflowCalendarDto calendarDto) {
        WorkflowCalendarDto updatedCalendar = calendarService.updateCalendar(calendarId, calendarDto);
        return ResponseEntity.ok(updatedCalendar);
    }
    
    @DeleteMapping("/{calendarId}")
    @Operation(summary = "Delete calendar", description = "Deletes a calendar from the system")
    public ResponseEntity<Void> deleteCalendar(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        calendarService.deleteCalendar(calendarId);
        return ResponseEntity.noContent().build();
    }
    
    // ===== PREDICATE-BASED SEARCH =====
    
    @GetMapping("/search")
    @Operation(summary = "Search calendars with predicates", description = "Search calendars using multiple criteria with pagination")
    public ResponseEntity<Page<WorkflowCalendarDto>> searchCalendars(
            @Parameter(description = "Calendar name (partial match)") @RequestParam(required = false) String calendarName,
            @Parameter(description = "Description (partial match)") @RequestParam(required = false) String description,
            @Parameter(description = "Recurrence type") @RequestParam(required = false) String recurrence,
            @Parameter(description = "Created by user") @RequestParam(required = false) String createdBy,
            @Parameter(description = "Start date (ISO format)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam(required = false) String endDate,
            @Parameter(description = "Created after date (ISO format)") @RequestParam(required = false) String createdAfter,
            @Parameter(description = "Created before date (ISO format)") @RequestParam(required = false) String createdBefore,
            Pageable pageable) {
        
        Page<WorkflowCalendarDto> calendars = calendarService.searchCalendars(
                calendarName, description, recurrence, createdBy, startDate, endDate, createdAfter, createdBefore, pageable);
        return ResponseEntity.ok(calendars);
    }
    
    @GetMapping
    @Operation(summary = "Get all calendars", description = "Retrieves all calendars with optional filtering and pagination")
    public ResponseEntity<Page<WorkflowCalendarDto>> getAllCalendars(
            @Parameter(description = "Filter by recurrence type") @RequestParam(required = false) String recurrence,
            Pageable pageable) {
        Page<WorkflowCalendarDto> calendars = calendarService.getAllCalendars(recurrence, pageable);
        return ResponseEntity.ok(calendars);
    }
    
    // ===== CALENDAR DAYS MANAGEMENT =====
    
    @PostMapping("/{calendarId}/days")
    @Operation(summary = "Add calendar day", description = "Adds a single day to a calendar")
    public ResponseEntity<WorkflowCalendarDayDto> addCalendarDay(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Valid @RequestBody WorkflowCalendarDayDto dayDto) {
        // Set the calendarId from the path variable
        dayDto.setCalendarId(calendarId);
        WorkflowCalendarDayDto addedDay = calendarService.addCalendarDay(calendarId, dayDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDay);
    }
    
    @PostMapping("/{calendarId}/days/batch")
    @Operation(summary = "Add multiple calendar days", description = "Adds multiple days to a calendar in batch")
    public ResponseEntity<List<WorkflowCalendarDayDto>> addCalendarDaysBatch(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Valid @RequestBody List<WorkflowCalendarDayDto> daysDto) {
        // Set the calendarId for all days in the batch
        daysDto.forEach(dayDto -> dayDto.setCalendarId(calendarId));
        List<WorkflowCalendarDayDto> addedDays = calendarService.addCalendarDaysBatch(calendarId, daysDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDays);
    }
    
    @GetMapping("/{calendarId}/days")
    @Operation(summary = "Get calendar days", description = "Retrieves all days of a calendar")
    public ResponseEntity<List<WorkflowCalendarDayDto>> getCalendarDays(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        List<WorkflowCalendarDayDto> days = calendarService.getCalendarDays(calendarId);
        return ResponseEntity.ok(days);
    }
    
    @PutMapping("/days/{dayId}")
    @Operation(summary = "Update calendar day", description = "Updates a calendar day")
    public ResponseEntity<WorkflowCalendarDayDto> updateCalendarDay(
            @Parameter(description = "Day ID") @PathVariable Long dayId,
            @Valid @RequestBody WorkflowCalendarDayDto dayDto) {
        WorkflowCalendarDayDto updatedDay = calendarService.updateCalendarDay(dayId, dayDto);
        return ResponseEntity.ok(updatedDay);
    }
    
    @DeleteMapping("/days/{dayId}")
    @Operation(summary = "Delete calendar day", description = "Deletes a calendar day")
    public ResponseEntity<Void> deleteCalendarDay(
            @Parameter(description = "Day ID") @PathVariable Long dayId) {
        calendarService.deleteCalendarDay(dayId);
        return ResponseEntity.noContent().build();
    }
    
    // ===== CALENDAR VALIDATION & WORKFLOW INTEGRATION =====
    
    @GetMapping("/{calendarId}/validate-date")
    @Operation(summary = "Validate date", description = "Checks if a specific date is valid for workflow execution")
    public ResponseEntity<Boolean> isDateValid(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to validate") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isValid = calendarService.isDateValid(calendarId, date);
        return ResponseEntity.ok(isValid);
    }
    
    @GetMapping("/{calendarId}/can-execute")
    @Operation(summary = "Check workflow execution", description = "Checks if a workflow can execute on a specific date")
    public ResponseEntity<Boolean> canExecuteWorkflow(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to check") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean canExecute = calendarService.canExecuteWorkflow(calendarId, date);
        return ResponseEntity.ok(canExecute);
    }
    
    @GetMapping("/{calendarId}/next-valid-date")
    @Operation(summary = "Get next valid date", description = "Finds the next valid date for workflow execution")
    public ResponseEntity<LocalDate> getNextValidDate(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to start from") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        LocalDate nextValidDate = calendarService.getNextValidDate(calendarId, fromDate);
        return ResponseEntity.ok(nextValidDate);
    }
    
    @GetMapping("/{calendarId}/previous-valid-date")
    @Operation(summary = "Get previous valid date", description = "Finds the previous valid date for workflow execution")
    public ResponseEntity<LocalDate> getPreviousValidDate(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to start from") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        LocalDate previousValidDate = calendarService.getPreviousValidDate(calendarId, fromDate);
        return ResponseEntity.ok(previousValidDate);
    }
    
    // ===== UTILITY OPERATIONS =====
    
    @GetMapping("/{calendarId}/days/type/{dayType}")
    @Operation(summary = "Get days by type", description = "Retrieves calendar days of a specific type")
    public ResponseEntity<List<WorkflowCalendarDayDto>> getCalendarDaysByType(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Day type (HOLIDAY, RUNDAY, WEEKEND)") @PathVariable String dayType) {
        List<WorkflowCalendarDayDto> days = calendarService.getCalendarDaysByType(calendarId, dayType);
        return ResponseEntity.ok(days);
    }
    
    @GetMapping("/{calendarId}/valid-dates")
    @Operation(summary = "Get valid dates in range", description = "Retrieves all valid dates within a date range")
    public ResponseEntity<List<LocalDate>> getValidDatesInRange(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LocalDate> validDates = calendarService.getValidDatesInRange(calendarId, startDate, endDate);
        return ResponseEntity.ok(validDates);
    }
    
    @GetMapping("/{calendarId}/holidays")
    @Operation(summary = "Get holidays in range", description = "Retrieves all holidays within a date range")
    public ResponseEntity<List<WorkflowCalendarDayDto>> getHolidaysInRange(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WorkflowCalendarDayDto> holidays = calendarService.getHolidaysInRange(calendarId, startDate, endDate);
        return ResponseEntity.ok(holidays);
    }
    
    @GetMapping("/{calendarId}/run-days")
    @Operation(summary = "Get run days in range", description = "Retrieves all run days within a date range")
    public ResponseEntity<List<WorkflowCalendarDayDto>> getRunDaysInRange(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WorkflowCalendarDayDto> runDays = calendarService.getRunDaysInRange(calendarId, startDate, endDate);
        return ResponseEntity.ok(runDays);
    }
    
    // ===== ADDITIONAL ENDPOINTS TO MATCH SAMPLE_REQUESTS.JSON =====
    
    @GetMapping("/calendars")
    @Operation(summary = "Get all calendars", description = "Retrieves all calendars with optional filtering and pagination")
    public ResponseEntity<Page<WorkflowCalendarDto>> getAllCalendarsPlural(
            @Parameter(description = "Filter by recurrence type") @RequestParam(required = false) String recurrence,
            Pageable pageable) {
        Page<WorkflowCalendarDto> calendars = calendarService.getAllCalendars(recurrence, pageable);
        return ResponseEntity.ok(calendars);
    }
    
    @PostMapping("/calendars")
    @Operation(summary = "Create new calendar", description = "Creates a new workflow calendar")
    public ResponseEntity<WorkflowCalendarDto> createCalendarPlural(
            @Valid @RequestBody WorkflowCalendarDto calendarDto) {
        WorkflowCalendarDto createdCalendar = calendarService.createCalendar(calendarDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
    }
    
    @GetMapping("/calendars/{calendarId}/days")
    @Operation(summary = "Get all days for a calendar", description = "Retrieves all calendar days for a specific calendar")
    public ResponseEntity<List<WorkflowCalendarDayDto>> getCalendarDaysPlural(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        List<WorkflowCalendarDayDto> days = calendarService.getCalendarDays(calendarId);
        return ResponseEntity.ok(days);
    }
    
    @PostMapping("/calendars/{calendarId}/days")
    @Operation(summary = "Add a day to calendar", description = "Adds a single day to a calendar")
    public ResponseEntity<WorkflowCalendarDayDto> addCalendarDayPlural(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Valid @RequestBody WorkflowCalendarDayDto dayDto) {
        WorkflowCalendarDayDto createdDay = calendarService.addCalendarDay(calendarId, dayDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDay);
    }
    
    @PostMapping("/calendars/{calendarId}/days/batch")
    @Operation(summary = "Add multiple days to calendar in batch", description = "Adds multiple calendar days in a single operation")
    public ResponseEntity<List<WorkflowCalendarDayDto>> addCalendarDaysBatchPlural(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Valid @RequestBody List<WorkflowCalendarDayDto> daysDto) {
        List<WorkflowCalendarDayDto> createdDays = calendarService.addCalendarDaysBatch(calendarId, daysDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDays);
    }
    
    @GetMapping("/calendars/{calendarId}/validate-date")
    @Operation(summary = "Check if a specific date is valid for workflow execution", description = "Validates a date against calendar rules")
    public ResponseEntity<Boolean> validateDatePlural(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to validate") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isValid = calendarService.isDateValid(calendarId, date);
        return ResponseEntity.ok(isValid);
    }
    
    @GetMapping("/calendars/{calendarId}/can-execute")
    @Operation(summary = "Check if a workflow can execute on a specific date", description = "Determines if workflow execution is allowed on a given date")
    public ResponseEntity<Boolean> canExecuteWorkflowPlural(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId,
            @Parameter(description = "Date to check") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean canExecute = calendarService.canExecuteWorkflow(calendarId, date);
        return ResponseEntity.ok(canExecute);
    }
}
