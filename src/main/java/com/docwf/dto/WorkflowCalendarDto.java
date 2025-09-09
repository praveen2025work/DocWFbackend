package com.docwf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowCalendarDto {
    
    private Long calendarId;
    
    @NotBlank(message = "Calendar name is required")
    private String calendarName;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String recurrence; // NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    
    private String cronExpression; // Quartz cron expression for scheduling
    
    private String timezone; // Timezone for cron execution (e.g., "America/New_York")
    
    private String region; // Geographic region (e.g., "US", "EU", "APAC")
    
    private Integer offsetDays; // Days to offset from the base date (can be negative)
    
    private String isActive = "Y"; // Y/N - whether calendar is active for scheduling
    
    @NotBlank(message = "Created by is required")
    private String createdBy;
    
    private LocalDateTime createdAt;
    
    private String updatedBy;
    
    private LocalDateTime updatedAt;
    
    private List<WorkflowCalendarDayDto> calendarDays;
    
    // Constructors
    public WorkflowCalendarDto() {}
    
    public WorkflowCalendarDto(String calendarName, String description, LocalDate startDate, LocalDate endDate, String recurrence, String createdBy) {
        this.calendarName = calendarName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrence = recurrence;
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
    
    public List<WorkflowCalendarDayDto> getCalendarDays() {
        return calendarDays;
    }
    
    public void setCalendarDays(List<WorkflowCalendarDayDto> calendarDays) {
        this.calendarDays = calendarDays;
    }
}
