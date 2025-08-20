package com.docwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "WORKFLOW_CALENDAR_DAYS")
@Audited
public class WorkflowCalendarDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORKFLOW_CALENDAR_DAY")
    @SequenceGenerator(name = "SEQ_WORKFLOW_CALENDAR_DAY", sequenceName = "SEQ_WORKFLOW_CALENDAR_DAY", allocationSize = 1)
    @Column(name = "CALENDAR_DAY_ID")
    private Long calendarDayId;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_ID", nullable = false)
    private WorkflowCalendar calendar;
    
    @NotNull
    @Column(name = "DAY_DATE", nullable = false)
    private LocalDate dayDate;
    
    @NotNull
    @Column(name = "DAY_TYPE", length = 20, nullable = false)
    private String dayType; // HOLIDAY, RUNDAY
    
    @Column(name = "NOTE", length = 255)
    private String note;
    
    // Enums
    public enum DayType {
        HOLIDAY, RUNDAY
    }
    
    // Constructors
    public WorkflowCalendarDay() {}
    
    public WorkflowCalendarDay(WorkflowCalendar calendar, LocalDate dayDate, String dayType, String note) {
        this.calendar = calendar;
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
    
    public WorkflowCalendar getCalendar() {
        return calendar;
    }
    
    public void setCalendar(WorkflowCalendar calendar) {
        this.calendar = calendar;
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
