package com.docwf.job;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.service.WorkflowCalendarService;
import com.docwf.service.WorkflowExecutionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Quartz job for executing workflows based on calendar cron expressions
 * This job runs workflows that are scheduled using cron expressions in calendars
 */
@Component
public class CalendarCronExecutionJob implements Job {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarCronExecutionJob.class);
    
    @Autowired
    private WorkflowCalendarService calendarService;
    
    @Autowired
    private WorkflowExecutionService executionService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Starting CalendarCronExecutionJob execution");
        
        try {
            // Get calendar ID from job data
            Long calendarId = context.getJobDetail().getJobDataMap().getLong("calendarId");
            String region = context.getJobDetail().getJobDataMap().getString("region");
            
            logger.info("Executing calendar cron job for calendarId: {}, region: {}", calendarId, region);
            
            // Get the calendar
            WorkflowCalendar calendar = calendarService.getCalendarEntityById(calendarId);
            if (calendar == null) {
                logger.error("Calendar not found for ID: {}", calendarId);
                return;
            }
            
            // Check if calendar is active
            if (!calendar.isActive()) {
                logger.info("Calendar {} is inactive, skipping execution", calendarId);
                return;
            }
            
            // Calculate effective date considering offset
            LocalDate baseDate = LocalDate.now();
            LocalDate effectiveDate = calendar.calculateEffectiveDate(baseDate);
            
            logger.info("Base date: {}, Effective date after offset: {}", baseDate, effectiveDate);
            
            // Check if the effective date is valid for execution
            if (!calendar.canExecuteWorkflow(effectiveDate)) {
                logger.info("Effective date {} is not valid for calendar {} execution", effectiveDate, calendarId);
                return;
            }
            
            // Check region-specific execution
            if (calendar.isRegionSpecific() && region != null && !region.equals(calendar.getRegion())) {
                logger.info("Calendar {} is region-specific ({}) but job is running for region {}, skipping", 
                    calendarId, calendar.getRegion(), region);
                return;
            }
            
            // Execute workflows associated with this calendar
            executeWorkflowsForCalendar(calendar, effectiveDate);
            
            logger.info("CalendarCronExecutionJob completed successfully for calendarId: {}", calendarId);
            
        } catch (Exception e) {
            logger.error("Error executing CalendarCronExecutionJob", e);
            throw new JobExecutionException("Failed to execute calendar cron job", e);
        }
    }
    
    /**
     * Execute workflows associated with the calendar
     */
    private void executeWorkflowsForCalendar(WorkflowCalendar calendar, LocalDate executionDate) {
        try {
            logger.info("Executing workflows for calendar {} on date {}", calendar.getCalendarId(), executionDate);
            
            // Get workflows that use this calendar
            List<Long> workflowIds = calendarService.getWorkflowIdsForCalendar(calendar.getCalendarId());
            
            if (workflowIds.isEmpty()) {
                logger.info("No workflows found for calendar {}", calendar.getCalendarId());
                return;
            }
            
            // Execute each workflow
            for (Long workflowId : workflowIds) {
                try {
                    logger.info("Executing workflow {} for calendar {}", workflowId, calendar.getCalendarId());
                    
                    // Start workflow with calendar validation
                    // Convert LocalDate to Long (assuming we want to use a system user ID)
                    Long systemUserId = 1L; // System user ID for automated execution
                    executionService.startWorkflowWithCalendar(workflowId, systemUserId, calendar.getCalendarId());
                    
                    logger.info("Successfully executed workflow {} for calendar {}", workflowId, calendar.getCalendarId());
                    
                } catch (Exception e) {
                    logger.error("Failed to execute workflow {} for calendar {}", workflowId, calendar.getCalendarId(), e);
                    // Continue with other workflows even if one fails
                }
            }
            
        } catch (Exception e) {
            logger.error("Error executing workflows for calendar {}", calendar.getCalendarId(), e);
        }
    }
    
    /**
     * Get the timezone for the calendar
     */
    private ZoneId getCalendarTimezone(WorkflowCalendar calendar) {
        try {
            String timezone = calendar.getEffectiveTimezone();
            return ZoneId.of(timezone);
        } catch (Exception e) {
            logger.warn("Invalid timezone {} for calendar {}, using UTC", calendar.getTimezone(), calendar.getCalendarId());
            return ZoneId.of("UTC");
        }
    }
}
