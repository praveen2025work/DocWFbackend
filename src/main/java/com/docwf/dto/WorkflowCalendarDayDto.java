package com.docwf.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class WorkflowCalendarDayDto {
    
    private Long calendarDayId;
    
    @NotNull(message = "Calendar ID is required")
    private Long calendarId;
    
    @NotNull(message = "Day date is required")
    private LocalDate dayDate;
    
    @NotNull(message = "Day type is required")
    private String dayType; // HOLIDAY, RUNDAY
    
    private String note;
    
    // Constructors
    public WorkflowCalendarDayDto() {}
    
    public WorkflowCalendarDayDto(Long calendarId, LocalDate dayDate, String dayType, String note) {
        this.calendarId = calendarId;
        this.dayDate = dayDate;
        this.dayType = dayType;
        this.note = note;
    }
    
    // Getters and Setters
    public Long getCalendarDayId() {
        return calendarDayId;
    }
    
    public void setCalendarDayId(Long calendarDayId) {
        this.calendarDayId = calendarDayId;
    }
    
    public Long getCalendarId() {
        return calendarId;
    }
    
    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }
    
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
