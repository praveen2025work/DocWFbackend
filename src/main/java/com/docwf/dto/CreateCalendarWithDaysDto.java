package com.docwf.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CreateCalendarWithDaysDto {
    
    @NotBlank(message = "Calendar name is required")
    private String calendarName;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String recurrence; // NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    
    @NotBlank(message = "Created by is required")
    private String createdBy;
    
    @Valid
    @NotEmpty(message = "At least one calendar day must be provided")
    private List<CalendarDayInputDto> calendarDays;
    
    // Constructors
    public CreateCalendarWithDaysDto() {}
    
    public CreateCalendarWithDaysDto(String calendarName, String description, LocalDate startDate, 
                                   LocalDate endDate, String recurrence, String createdBy, 
                                   List<CalendarDayInputDto> calendarDays) {
        this.calendarName = calendarName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurrence = recurrence;
        this.createdBy = createdBy;
        this.calendarDays = calendarDays;
    }
    
    // Getters and Setters
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
    
    public List<CalendarDayInputDto> getCalendarDays() {
        return calendarDays;
    }
    
    public void setCalendarDays(List<CalendarDayInputDto> calendarDays) {
        this.calendarDays = calendarDays;
    }
    
    // Inner DTO for calendar day input (without calendarId since it's not known yet)
    public static class CalendarDayInputDto {
        
        @NotNull(message = "Day date is required")
        private LocalDate dayDate;
        
        @NotBlank(message = "Day type is required")
        private String dayType; // HOLIDAY, RUNDAY
        
        private String note;
        
        // Constructors
        public CalendarDayInputDto() {}
        
        public CalendarDayInputDto(LocalDate dayDate, String dayType, String note) {
            this.dayDate = dayDate;
            this.dayType = dayType;
            this.note = note;
        }
        
        // Getters and Setters
        public LocalDate getDayDate() {
            return dayDate;
        }
        
        public void setDayDate(LocalDate dayDate) {
            this.dayDate = dayDate;
        }
        
        public String getDayType() {
            return dayType;
        }
        
        public void setDayType(String dayType) {
            this.dayType = dayType;
        }
        
        public String getNote() {
            return note;
        }
        
        public void setNote(String note) {
            this.note = note;
        }
    }
}
