package com.docwf.config;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.service.WorkflowCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to initialize scheduler with existing calendars on application startup
 */
@Service
public class SchedulerInitializationService implements ApplicationRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(SchedulerInitializationService.class);
    
    @Autowired
    private WorkflowCalendarService workflowCalendarService;
    
    @Autowired
    private CalendarSchedulerConfig calendarSchedulerConfig;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Initializing scheduler with existing calendars...");
        
        try {
            // Get all active calendars with cron expressions
            List<WorkflowCalendar> activeCalendars = workflowCalendarService.getActiveCalendars()
                .stream()
                .map(calendarDto -> workflowCalendarService.getCalendarEntityById(calendarDto.getCalendarId()))
                .filter(calendar -> calendar != null && 
                        calendar.getCronExpression() != null && 
                        !calendar.getCronExpression().trim().isEmpty())
                .toList();
            
            logger.info("Found {} active calendars with cron expressions", activeCalendars.size());
            
            // Schedule each calendar
            for (WorkflowCalendar calendar : activeCalendars) {
                try {
                    calendarSchedulerConfig.scheduleWorkflow(calendar);
                    logger.info("Scheduled calendar: {} (ID: {})", 
                               calendar.getCalendarName(), calendar.getCalendarId());
                } catch (Exception e) {
                    logger.error("Failed to schedule calendar: {} (ID: {})", 
                                calendar.getCalendarName(), calendar.getCalendarId(), e);
                }
            }
            
            logger.info("Scheduler initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during scheduler initialization", e);
        }
    }
}
