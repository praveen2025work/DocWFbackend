package com.docwf.repository;

import com.docwf.entity.WorkflowCalendarDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkflowCalendarDayRepository extends JpaRepository<WorkflowCalendarDay, Long> {
    
    List<WorkflowCalendarDay> findByCalendarCalendarId(Long calendarId);
    
    List<WorkflowCalendarDay> findByCalendarCalendarIdAndDayType(Long calendarId, String dayType);
    
    List<WorkflowCalendarDay> findByDayDate(LocalDate dayDate);
    
    List<WorkflowCalendarDay> findByDayDateAndDayType(LocalDate dayDate, String dayType);
    
    @Query("SELECT c FROM WorkflowCalendarDay c WHERE c.calendar.calendarId = :calendarId AND c.dayDate BETWEEN :startDate AND :endDate")
    List<WorkflowCalendarDay> findByCalendarAndDateRange(@Param("calendarId") Long calendarId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM WorkflowCalendarDay c WHERE c.dayDate BETWEEN :startDate AND :endDate AND c.dayType = :dayType")
    List<WorkflowCalendarDay> findByDateRangeAndType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("dayType") String dayType);
}
