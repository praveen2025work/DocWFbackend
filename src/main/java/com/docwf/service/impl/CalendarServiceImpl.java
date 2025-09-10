package com.docwf.service.impl;

import com.docwf.dto.UserCalendarDto;
import com.docwf.dto.UserAvailabilityDto;
import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowUser;
import com.docwf.repository.WorkflowInstanceTaskRepository;
import com.docwf.repository.WorkflowUserRepository;
import com.docwf.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {
    
    @Autowired
    private WorkflowUserRepository userRepository;
    
    @Autowired
    private WorkflowInstanceTaskRepository taskRepository;
    
    @Override
    public UserCalendarDto getUserCalendar(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement actual calendar retrieval logic
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        UserCalendarDto calendar = new UserCalendarDto(userId, user.getUsername(), startDate, endDate);
        
        // Get user's tasks for the date range
        List<WorkflowInstanceTask> userTasks = taskRepository.findByAssignedToUserIdAndDateRange(userId, startDate, endDate);
        List<UserCalendarDto.CalendarTaskDto> taskDtos = userTasks.stream()
                .map(this::convertTaskToCalendarTask)
                .collect(Collectors.toList());
        
        calendar.setTasks(taskDtos);
        calendar.setEvents(new ArrayList<>()); // Events would come from a separate events table
        calendar.setReminders(new ArrayList<>()); // Reminders would come from a separate reminders table
        
        // Get user availability
        UserAvailabilityDto availability = getUserAvailability(userId);
        calendar.setAvailability(availability);
        
        return calendar;
    }
    
    @Override
    public UserCalendarDto.CalendarEventDto createEvent(UserCalendarDto.CalendarEventDto eventDto) {
        // Implement actual event creation logic
        // This would typically involve saving to an events table
        // For now, we'll generate a proper ID and return the event
        eventDto.setEventId(System.currentTimeMillis());
        // TODO: Save event to database when events table is implemented
        return eventDto;
    }
    
    @Override
    public Optional<UserCalendarDto.CalendarEventDto> getEventById(Long eventId) {
        // Implement actual event retrieval logic
        // This would typically involve querying an events table
        // For now, returning empty as events table is not implemented
        return Optional.empty();
    }
    
    @Override
    public UserCalendarDto.CalendarEventDto updateEvent(Long eventId, UserCalendarDto.CalendarEventDto eventDto) {
        // Implement actual event update logic
        // This would typically involve updating an events table
        eventDto.setEventId(eventId);
        // TODO: Update event in database when events table is implemented
        return eventDto;
    }
    
    @Override
    public void deleteEvent(Long eventId) {
        // Implement actual event deletion logic
        // This would typically involve deleting from an events table
        // TODO: Delete event from database when events table is implemented
    }
    
    @Override
    public List<UserCalendarDto.CalendarEventDto> getUserEvents(Long userId, String eventType, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement actual event filtering logic
        // This would typically involve querying an events table with filters
        // For now, returning empty list as events table is not implemented
        return new ArrayList<>();
    }
    
    @Override
    public List<UserCalendarDto.CalendarTaskDto> getUserTasks(Long userId, String status, String priority) {
        // Implement actual task retrieval logic
        List<WorkflowInstanceTask> tasks;
        
        if (status != null && priority != null) {
            tasks = taskRepository.findByAssignedToUserIdAndStatusAndPriority(userId, 
                WorkflowInstanceTask.TaskInstanceStatus.valueOf(status), priority);
        } else if (status != null) {
            tasks = taskRepository.findByAssignedToUserIdAndStatus(userId, 
                WorkflowInstanceTask.TaskInstanceStatus.valueOf(status));
        } else if (priority != null) {
            tasks = taskRepository.findByAssignedToUserIdAndPriority(userId, priority);
        } else {
            tasks = taskRepository.findByAssignedToUserId(userId);
        }
        
        return tasks.stream()
                .map(this::convertTaskToCalendarTask)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserCalendarDto.CalendarReminderDto> getUserReminders(Long userId, String type) {
        // Implement actual reminder retrieval logic
        // This would typically involve querying a reminders table
        // For now, returning empty list as reminders table is not implemented
        return new ArrayList<>();
    }
    
    @Override
    public UserCalendarDto.CalendarReminderDto createReminder(UserCalendarDto.CalendarReminderDto reminderDto) {
        // Implement actual reminder creation logic
        // This would typically involve saving to a reminders table
        reminderDto.setReminderId(System.currentTimeMillis());
        // TODO: Save reminder to database when reminders table is implemented
        return reminderDto;
    }
    
    @Override
    public UserCalendarDto.CalendarReminderDto updateReminder(Long reminderId, UserCalendarDto.CalendarReminderDto reminderDto) {
        // Implement actual reminder update logic
        // This would typically involve updating a reminders table
        reminderDto.setReminderId(reminderId);
        // TODO: Update reminder in database when reminders table is implemented
        return reminderDto;
    }
    
    @Override
    public void deleteReminder(Long reminderId) {
        // Implement actual reminder deletion logic
        // This would typically involve deleting from a reminders table
        // TODO: Delete reminder from database when reminders table is implemented
    }
    
    @Override
    public UserAvailabilityDto getUserAvailability(Long userId) {
        // Implement actual availability retrieval logic
        WorkflowUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        UserAvailabilityDto availability = new UserAvailabilityDto();
        availability.setUserId(userId);
        availability.setUsername(user.getUsername());
        availability.setStartDate(LocalDateTime.now().toLocalDate());
        availability.setEndDate(LocalDateTime.now().plusDays(7).toLocalDate());
        
        // Calculate actual scheduled hours from user's tasks
        List<WorkflowInstanceTask> userTasks = taskRepository.findByAssignedToUserId(userId);
        long scheduledMinutes = userTasks.stream()
                .filter(task -> task.getStartedOn() != null && task.getCompletedOn() != null)
                .mapToLong(task -> 
                    java.time.Duration.between(task.getStartedOn(), task.getCompletedOn()).toMinutes())
                .sum();
        
        availability.setScheduledHours((int) (scheduledMinutes / 60));
        availability.setAvailableHours(40); // Default 40-hour work week
        availability.calculateAvailablePercentage();
        
        return availability;
    }
    
    @Override
    public UserAvailabilityDto updateUserAvailability(Long userId, UserAvailabilityDto availabilityDto) {
        // Implement actual availability update logic
        // This would typically involve updating a user_availability table
        availabilityDto.setUserId(userId);
        // TODO: Update availability in database when availability table is implemented
        return availabilityDto;
    }
    
    @Override
    public List<UserCalendarDto.CalendarEventDto> checkConflicts(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        // Implement actual conflict checking logic
        List<WorkflowInstanceTask> conflictingTasks = taskRepository.findConflictingTasks(userId, startTime, endTime);
        
        return conflictingTasks.stream()
                .map(this::convertTaskToCalendarEvent)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserCalendarDto.CalendarEventDto> getFreeTimeSlots(Long userId, LocalDateTime date, Integer durationMinutes) {
        // Implement actual free time slot calculation logic
        List<UserCalendarDto.CalendarEventDto> freeSlots = new ArrayList<>();
        
        // Get user's scheduled tasks for the given date
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        
        List<WorkflowInstanceTask> scheduledTasks = taskRepository.findByAssignedToUserIdAndDateRange(userId, startOfDay, endOfDay);
        
        // Define working hours (9 AM to 5 PM)
        LocalTime workStart = LocalTime.of(9, 0);
        LocalTime workEnd = LocalTime.of(17, 0);
        
        LocalDateTime currentSlot = date.with(workStart);
        LocalDateTime endOfWorkDay = date.with(workEnd);
        
        while (currentSlot.plusMinutes(durationMinutes).isBefore(endOfWorkDay)) {
            final LocalDateTime slotStart = currentSlot;
            final LocalDateTime slotEnd = currentSlot.plusMinutes(durationMinutes);
            
            // Check if this time slot conflicts with any scheduled tasks
            boolean hasConflict = scheduledTasks.stream()
                    .anyMatch(task -> {
                        if (task.getStartedOn() == null || task.getCompletedOn() == null) return false;
                        return !(slotEnd.isBefore(task.getStartedOn()) ||
                                slotStart.isAfter(task.getCompletedOn()));
                    });
            
            if (!hasConflict) {
                UserCalendarDto.CalendarEventDto slot = new UserCalendarDto.CalendarEventDto();
                slot.setEventId(System.currentTimeMillis() + freeSlots.size());
                slot.setEventType("FREE_SLOT");
                slot.setTitle("Available Time");
                slot.setStartTime(slotStart);
                slot.setEndTime(slotEnd);
                slot.setPriority("LOW");
                slot.setAllDay(false);
                
                freeSlots.add(slot);
            }
            
            currentSlot = currentSlot.plusMinutes(durationMinutes);
        }
        
        return freeSlots;
    }
    
    private UserCalendarDto.CalendarTaskDto convertTaskToCalendarTask(WorkflowInstanceTask task) {
        UserCalendarDto.CalendarTaskDto taskDto = new UserCalendarDto.CalendarTaskDto();
        taskDto.setTaskId(task.getInstanceTaskId());
        taskDto.setTaskName(task.getTask().getName());
        taskDto.setStatus(task.getStatus().toString());
        taskDto.setPriority("MEDIUM"); // Default priority
        taskDto.setDueDate(task.getCompletedOn() != null ? task.getCompletedOn() : task.getStartedOn());
        taskDto.setWorkflowName(task.getWorkflowInstance().getWorkflow().getName());
        taskDto.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null);
        return taskDto;
    }
    
    private UserCalendarDto.CalendarEventDto convertTaskToCalendarEvent(WorkflowInstanceTask task) {
        UserCalendarDto.CalendarEventDto eventDto = new UserCalendarDto.CalendarEventDto();
        eventDto.setEventId(task.getInstanceTaskId());
        eventDto.setEventType("TASK");
        eventDto.setTitle(task.getTask().getName());
        eventDto.setStartTime(task.getStartedOn());
        eventDto.setEndTime(task.getCompletedOn());
        eventDto.setPriority("MEDIUM");
        eventDto.setAllDay(false);
        return eventDto;
    }
}
