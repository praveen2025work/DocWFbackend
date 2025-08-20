package com.docwf.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for User Availability
 */
public class UserAvailabilityDto {
    
    private Long userId;
    private String username;
    private LocalDate startDate;
    private LocalDate endDate;
    private int availableHours;
    private int scheduledHours;
    private double availablePercentage;
    private List<TimeConflictDto> conflicts;
    private List<AvailabilitySlotDto> availabilitySlots;

    // Default constructor
    public UserAvailabilityDto() {}

    // Constructor with required fields
    public UserAvailabilityDto(Long userId, String username, LocalDate startDate, LocalDate endDate) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(int availableHours) {
        this.availableHours = availableHours;
    }

    public int getScheduledHours() {
        return scheduledHours;
    }

    public void setScheduledHours(int scheduledHours) {
        this.scheduledHours = scheduledHours;
    }

    public double getAvailablePercentage() {
        return availablePercentage;
    }

    public void setAvailablePercentage(double availablePercentage) {
        this.availablePercentage = availablePercentage;
    }

    public List<TimeConflictDto> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<TimeConflictDto> conflicts) {
        this.conflicts = conflicts;
    }

    public List<AvailabilitySlotDto> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlotDto> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

    /**
     * Calculate available percentage
     */
    public void calculateAvailablePercentage() {
        if (availableHours > 0) {
            this.availablePercentage = (double) (availableHours - scheduledHours) / availableHours * 100;
        } else {
            this.availablePercentage = 0.0;
        }
    }

    /**
     * Check if user has conflicts
     */
    public boolean hasConflicts() {
        return conflicts != null && !conflicts.isEmpty();
    }

    /**
     * Get conflict count
     */
    public int getConflictCount() {
        return conflicts != null ? conflicts.size() : 0;
    }

    /**
     * Check if user is available for given hours
     */
    public boolean isAvailableForHours(int requiredHours) {
        return (availableHours - scheduledHours) >= requiredHours;
    }

    /**
     * Inner class for time conflicts
     */
    public static class TimeConflictDto {
        private String conflictType; // OVERLAP, OVERTIME, UNAVAILABLE
        private String startTime;
        private String endTime;
        private String description;
        private String entityType; // WORKFLOW, TASK, MEETING
        private Long entityId;

        // Default constructor
        public TimeConflictDto() {}

        // Getters and Setters
        public String getConflictType() {
            return conflictType;
        }

        public void setConflictType(String conflictType) {
            this.conflictType = conflictType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }
    }

    /**
     * Inner class for availability slots
     */
    public static class AvailabilitySlotDto {
        private String dayOfWeek;
        private String startTime;
        private String endTime;
        private boolean isAvailable;
        private String notes;

        // Default constructor
        public AvailabilitySlotDto() {}

        // Getters and Setters
        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
