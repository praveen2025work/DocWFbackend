package com.docwf.repository;

import com.docwf.entity.WorkflowCalendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.startDate <= :date AND c.endDate >= :date")
    List<WorkflowCalendar> findCalendarsForDate(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.recurrence = :recurrence")
    List<WorkflowCalendar> findByRecurrence(@Param("recurrence") String recurrence);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.createdBy = :createdBy")
    List<WorkflowCalendar> findByCreatedBy(@Param("createdBy") String createdBy);
    
    @Query("SELECT c FROM WorkflowCalendar c WHERE c.startDate >= :startDate AND c.endDate <= :endDate")
    List<WorkflowCalendar> findCalendarsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Find calendars by recurrence with pagination
     */
    Page<WorkflowCalendar> findByRecurrence(String recurrence, Pageable pageable);
    
    /**
     * Dynamic search for calendars with multiple criteria
     */
    @Query("SELECT c FROM WorkflowCalendar c WHERE " +
           "(:calendarName IS NULL OR LOWER(c.calendarName) LIKE LOWER(CONCAT('%', :calendarName, '%'))) AND " +
           "(:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:recurrence IS NULL OR c.recurrence = :recurrence) AND " +
           "(:createdBy IS NULL OR LOWER(c.createdBy) LIKE LOWER(CONCAT('%', :createdBy, '%'))) AND " +
           "(:startDate IS NULL OR c.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.endDate <= :endDate) AND " +
           "(:createdAfter IS NULL OR c.createdAt >= :createdAfter) AND " +
           "(:createdBefore IS NULL OR c.createdAt <= :createdBefore)")
    Page<WorkflowCalendar> searchCalendars(@Param("calendarName") String calendarName,
                                          @Param("description") String description,
                                          @Param("recurrence") String recurrence,
                                          @Param("createdBy") String createdBy,
                                          @Param("startDate") java.time.LocalDate startDate,
                                          @Param("endDate") java.time.LocalDate endDate,
                                          @Param("createdAfter") java.time.LocalDateTime createdAfter,
                                          @Param("createdBefore") java.time.LocalDateTime createdBefore,
                                          Pageable pageable);
}
