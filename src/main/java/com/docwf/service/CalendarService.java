package com.docwf.service;

import com.docwf.dto.UserCalendarDto;
import com.docwf.dto.UserAvailabilityDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarService {
    
    /**
     * Retrieves a user's calendar for a specific date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return the user's calendar
     */
    UserCalendarDto getUserCalendar(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Creates a new calendar event
     * @param eventDto the event data
     * @return the created event
     */
    UserCalendarDto.CalendarEventDto createEvent(UserCalendarDto.CalendarEventDto eventDto);
    
    /**
     * Retrieves an event by its ID
     * @param eventId the event ID
     * @return the event if found
     */
    Optional<UserCalendarDto.CalendarEventDto> getEventById(Long eventId);
    
    /**
     * Updates an existing event
     * @param eventId the event ID
     * @param eventDto the updated event data
     * @return the updated event
     */
    UserCalendarDto.CalendarEventDto updateEvent(Long eventId, UserCalendarDto.CalendarEventDto eventDto);
    
    /**
     * Deletes an event
     * @param eventId the event ID
     */
    void deleteEvent(Long eventId);
    
    /**
     * Retrieves all events for a specific user with optional filtering
     * @param userId the user ID
     * @param eventType optional event type filter
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @return list of events
     */
    List<UserCalendarDto.CalendarEventDto> getUserEvents(Long userId, String eventType, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Retrieves all workflow tasks for a specific user
     * @param userId the user ID
     * @param status optional status filter
     * @param priority optional priority filter
     * @return list of tasks
     */
    List<UserCalendarDto.CalendarTaskDto> getUserTasks(Long userId, String status, String priority);
    
    /**
     * Retrieves all reminders for a specific user
     * @param userId the user ID
     * @param type optional reminder type filter
     * @return list of reminders
     */
    List<UserCalendarDto.CalendarReminderDto> getUserReminders(Long userId, String type);
    
    /**
     * Creates a new reminder
     * @param reminderDto the reminder data
     * @return the created reminder
     */
    UserCalendarDto.CalendarReminderDto createReminder(UserCalendarDto.CalendarReminderDto reminderDto);
    
    /**
     * Updates an existing reminder
     * @param reminderId the reminder ID
     * @param reminderDto the updated reminder data
     * @return the updated reminder
     */
    UserCalendarDto.CalendarReminderDto updateReminder(Long reminderId, UserCalendarDto.CalendarReminderDto reminderDto);
    
    /**
     * Deletes a reminder
     * @param reminderId the reminder ID
     */
    void deleteReminder(Long reminderId);
    
    /**
     * Retrieves a user's availability schedule
     * @param userId the user ID
     * @return the user's availability
     */
    UserAvailabilityDto getUserAvailability(Long userId);
    
    /**
     * Updates a user's availability schedule
     * @param userId the user ID
     * @param availabilityDto the availability data
     * @return the updated availability
     */
    UserAvailabilityDto updateUserAvailability(Long userId, UserAvailabilityDto availabilityDto);
    
    /**
     * Checks for scheduling conflicts in a user's calendar
     * @param userId the user ID
     * @param startTime the start time to check
     * @param endTime the end time to check
     * @return list of conflicting events
     */
    List<UserCalendarDto.CalendarEventDto> checkConflicts(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Retrieves available time slots for a user
     * @param userId the user ID
     * @param date the date to check
     * @param durationMinutes the duration of the time slot in minutes
     * @return list of free time slots
     */
    List<UserCalendarDto.CalendarEventDto> getFreeTimeSlots(Long userId, LocalDateTime date, Integer durationMinutes);
}
