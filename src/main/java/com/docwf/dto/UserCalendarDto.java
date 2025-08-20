package com.docwf.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for User Calendar
 */
public class UserCalendarDto {
    
    private Long userId;
    private String username;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<CalendarEventDto> events;
    private List<CalendarTaskDto> tasks;
    private List<CalendarReminderDto> reminders;
    private UserAvailabilityDto availability;

    // Default constructor
    public UserCalendarDto() {}

    // Constructor with required fields
    public UserCalendarDto(Long userId, String username, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<CalendarEventDto> getEvents() {
        return events;
    }

    public void setEvents(List<CalendarEventDto> events) {
        this.events = events;
    }

    public List<CalendarTaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<CalendarTaskDto> tasks) {
        this.tasks = tasks;
    }

    public List<CalendarReminderDto> getReminders() {
        return reminders;
    }

    public void setReminders(List<CalendarReminderDto> reminders) {
        this.reminders = reminders;
    }

    public UserAvailabilityDto getAvailability() {
        return availability;
    }

    public void setAvailability(UserAvailabilityDto availability) {
        this.availability = availability;
    }

    /**
     * Inner class for calendar events
     */
    public static class CalendarEventDto {
        private Long eventId;
        private String eventType; // WORKFLOW_START, TASK_DUE, MEETING, etc.
        private String title;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String location;
        private String priority; // LOW, MEDIUM, HIGH, URGENT
        private boolean isAllDay;

        // Default constructor
        public CalendarEventDto() {}

        // Getters and Setters
        public Long getEventId() {
            return eventId;
        }

        public void setEventId(Long eventId) {
            this.eventId = eventId;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public boolean isAllDay() {
            return isAllDay;
        }

        public void setAllDay(boolean allDay) {
            isAllDay = allDay;
        }
    }

    /**
     * Inner class for calendar tasks
     */
    public static class CalendarTaskDto {
        private Long taskId;
        private String taskName;
        private String workflowName;
        private LocalDateTime dueDate;
        private String priority; // LOW, MEDIUM, HIGH, URGENT
        private String status; // PENDING, IN_PROGRESS, COMPLETED, OVERDUE
        private String assignedTo;

        // Default constructor
        public CalendarTaskDto() {}

        // Getters and Setters
        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getWorkflowName() {
            return workflowName;
        }

        public void setWorkflowName(String workflowName) {
            this.workflowName = workflowName;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAssignedTo() {
            return assignedTo;
        }

        public void setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
        }
    }

    /**
     * Inner class for calendar reminders
     */
    public static class CalendarReminderDto {
        private Long reminderId;
        private String title;
        private String message;
        private LocalDateTime reminderTime;
        private String type; // TASK_DUE, WORKFLOW_OVERDUE, CUSTOM
        private String priority; // LOW, MEDIUM, HIGH, URGENT

        // Default constructor
        public CalendarReminderDto() {}

        // Getters and Setters
        public Long getReminderId() {
            return reminderId;
        }

        public void setReminderId(Long reminderId) {
            this.reminderId = reminderId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getReminderTime() {
            return reminderTime;
        }

        public void setReminderTime(LocalDateTime reminderTime) {
            this.reminderTime = reminderTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }
}
