package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKFLOW_CALENDAR")
@Audited
public class WorkflowCalendar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CALENDAR")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CALENDAR", sequenceName = "SEQ_WORKFLOW_CALENDAR", allocationSize = 1)
    @Column(name = "CALENDAR_ID")
    private Long calendarId;
    
    @NotBlank
    @Column(name = "CALENDAR_NAME", length = 100, nullable = false)
    private String calendarName;
    
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    
    @NotNull
    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;
    
    @NotNull
    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "RECURRENCE", length = 50)
    private String recurrence; // NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    
    @Column(name = "CRON_EXPRESSION", length = 100)
    private String cronExpression; // Quartz cron expression for scheduling
    
    @Column(name = "TIMEZONE", length = 50)
    private String timezone; // Timezone for cron execution (e.g., "America/New_York")
    
    @Column(name = "REGION", length = 50)
    private String region; // Geographic region (e.g., "US", "EU", "APAC")
    
    @Column(name = "OFFSET_DAYS")
    private Integer offsetDays; // Days to offset from the base date (can be negative)
    
    @Column(name = "IS_ACTIVE", length = 1)
    private String isActive = "Y"; // Y/N - whether calendar is active for scheduling
    
    @Column(name = "WORKFLOW_ID")
    private Long workflowId; // Associated workflow configuration ID
    
    @NotBlank
    @Column(name = "CREATED_BY", length = 100, nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowCalendarDay> calendarDays = new ArrayList<>();
    
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<WorkflowInstance> workflowInstances = new ArrayList<>();
    
    // Enums
    public enum RecurrenceType {
        NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    }
    
    public enum Region {
        US, EU, APAC, GLOBAL
    }
    
    public enum CalendarStatus {
        ACTIVE("Y"), INACTIVE("N");
        
        private final String value;
        
        CalendarStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // Constructors
    public WorkflowCalendar() {}
    
    public WorkflowCalendar(String calendarName, String description, LocalDate startDate, LocalDate endDate, String recurrence, String createdBy) {
        this.calendarName = calendarName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrence = recurrence;
        this.createdBy = createdBy;
    }
    
    public WorkflowCalendar(String calendarName, String description, LocalDate startDate, LocalDate endDate, String recurrence, 
                           String cronExpression, String timezone, String region, Integer offsetDays, String createdBy) {
        this.calendarName = calendarName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrence = recurrence;
        this.cronExpression = cronExpression;
        this.timezone = timezone;
        this.region = region;
        this.offsetDays = offsetDays;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getCalendarId() {
        return calendarId;
    }
    
    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }
    
    public String getCalendarName() {
        return calendarName;
    }
    
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public String getRecurrence() {
        return recurrence;
    }
    
    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<WorkflowCalendarDay> getCalendarDays() {
        return calendarDays;
    }
    
    public void setCalendarDays(List<WorkflowCalendarDay> calendarDays) {
        this.calendarDays = calendarDays;
    }
    
    public List<WorkflowInstance> getWorkflowInstances() {
        return workflowInstances;
    }
    
    public void setWorkflowInstances(List<WorkflowInstance> workflowInstances) {
        this.workflowInstances = workflowInstances;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public Integer getOffsetDays() {
        return offsetDays;
    }
    
    public void setOffsetDays(Integer offsetDays) {
        this.offsetDays = offsetDays;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    public Long getWorkflowId() {
        return workflowId;
    }
    
    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
    
    // Helper methods
    public void addCalendarDay(WorkflowCalendarDay calendarDay) {
        calendarDays.add(calendarDay);
        calendarDay.setCalendar(this);
    }
    
    public void removeCalendarDay(WorkflowCalendarDay calendarDay) {
        calendarDays.remove(calendarDay);
        calendarDay.setCalendar(null);
    }
    
    public boolean isDateValid(LocalDate date) {
        // Check if date is within calendar range
        if (date.isBefore(startDate) || date.isAfter(endDate)) {
            return false;
        }
        
        // Check if it's a holiday - holidays are always invalid
        boolean isHoliday = calendarDays.stream()
            .anyMatch(day -> day.getDayDate().equals(date) && "HOLIDAY".equals(day.getDayType()));
        
        if (isHoliday) {
            return false;
        }
        
        // Check if it's a specific run day - run days are always valid
        boolean isRunDay = calendarDays.stream()
            .anyMatch(day -> day.getDayDate().equals(date) && "RUNDAY".equals(day.getDayType()));
        
        if (isRunDay) {
            return true;
        }
        
        // If no specific run days defined, apply default rules based on recurrence
        if (recurrence == null || "NONE".equals(recurrence)) {
            // No recurrence: skip weekends
            return !isWeekend(date);
        } else if ("DAILY".equals(recurrence)) {
            // Daily: all days except holidays
            return true;
        } else if ("WEEKLY".equals(recurrence)) {
            // Weekly: skip weekends
            return !isWeekend(date);
        } else if ("MONTHLY".equals(recurrence)) {
            // Monthly: skip weekends
            return !isWeekend(date);
        } else if ("YEARLY".equals(recurrence)) {
            // Yearly: skip weekends
            return !isWeekend(date);
        }
        
        // Default: skip weekends
        return !isWeekend(date);
    }
    
    /**
     * Enhanced method to check if workflow can execute on a specific date
     * This implements the business logic: 
     * - If RUNDAY entries exist → workflow only runs on those dates
     * - If no RUNDAY entries → workflow runs all days except holidays and weekends
     */
    public boolean canExecuteWorkflow(LocalDate date) {
        // Check if date is within calendar range
        if (date.isBefore(startDate) || date.isAfter(endDate)) {
            return false;
        }
        
        // Check if it's a holiday - holidays are always invalid
        boolean isHoliday = calendarDays.stream()
            .anyMatch(day -> day.getDayDate().equals(date) && "HOLIDAY".equals(day.getDayType()));
        
        if (isHoliday) {
            return false;
        }
        
        // Check if specific run days are defined
        boolean hasRunDays = calendarDays.stream()
            .anyMatch(day -> "RUNDAY".equals(day.getDayType()));
        
        if (hasRunDays) {
            // If run days are defined, only run on those specific dates
            return calendarDays.stream()
                .anyMatch(day -> day.getDayDate().equals(date) && "RUNDAY".equals(day.getDayType()));
        } else {
            // If no run days defined, run on all days except holidays and weekends
            return !isWeekend(date);
        }
    }
    
    private boolean isWeekend(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7; // Saturday = 6, Sunday = 7
    }
    
    /**
     * Calculate the effective date considering offset days
     * @param baseDate The base date to calculate from
     * @return The effective date after applying offset
     */
    public LocalDate calculateEffectiveDate(LocalDate baseDate) {
        if (offsetDays == null || offsetDays == 0) {
            return baseDate;
        }
        return baseDate.plusDays(offsetDays);
    }
    
    /**
     * Check if calendar is active for scheduling
     * @return true if calendar is active
     */
    public boolean isActive() {
        return "Y".equals(isActive);
    }
    
    /**
     * Check if calendar has cron-based scheduling
     * @return true if cron expression is set
     */
    public boolean hasCronScheduling() {
        return cronExpression != null && !cronExpression.trim().isEmpty();
    }
    
    /**
     * Get the timezone for cron execution, default to UTC if not set
     * @return timezone string
     */
    public String getEffectiveTimezone() {
        return timezone != null ? timezone : "UTC";
    }
    
    /**
     * Check if calendar is region-specific
     * @return true if region is set
     */
    public boolean isRegionSpecific() {
        return region != null && !region.trim().isEmpty();
    }
}
