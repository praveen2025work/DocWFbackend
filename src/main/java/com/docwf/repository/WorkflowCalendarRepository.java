package com.docwf.repository;

import com.docwf.entity.WorkflowCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowCalendarRepository extends JpaRepository<WorkflowCalendar, Long> {
    
    Optional<WorkflowCalendar> findByCalendarName(String calendarName);
    
    List<WorkflowCalendar> findByIsActive(String isActive);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.startDate <= :date AND c.endDate >= :date")
    List<WorkflowCalendar> findCalendarsForDate(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.recurrence = :recurrence")
    List<WorkflowCalendar> findByRecurrence(@Param("recurrence") String recurrence);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.createdBy = :createdBy")
    List<WorkflowCalendar> findByCreatedBy(@Param("createdBy") String createdBy);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.startDate >= :startDate AND c.endDate <= :endDate")
    List<WorkflowCalendar> findCalendarsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
