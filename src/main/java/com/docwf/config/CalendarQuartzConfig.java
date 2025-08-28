package com.docwf.config;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.job.CalendarWorkflowExecutionJob;
import com.docwf.repository.WorkflowCalendarRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.util.List;

/**
 * Calendar-Specific Quartz Configuration
 * Dynamically creates Quartz jobs and triggers for each calendar
 * Each calendar gets its own job execution schedule
 */
@Configuration
public class CalendarQuartzConfig implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarQuartzConfig.class);
    
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    
    @Autowired
    private WorkflowCalendarRepository calendarRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize calendar-specific jobs when application starts
        initializeCalendarJobs();
    }
    
    /**
     * Initialize Quartz jobs for all active calendars
     */
    public void initializeCalendarJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            
            // Get all active calendars (using a simple query for now)
            List<WorkflowCalendar> activeCalendars = calendarRepository.findAll();
            
            for (WorkflowCalendar calendar : activeCalendars) {
                createCalendarJob(scheduler, calendar);
            }
            
            logger.info("Initialized {} calendar-specific Quartz jobs", activeCalendars.size());
            
        } catch (Exception e) {
            logger.error("Failed to initialize calendar jobs", e);
        }
    }
    
    /**
     * Create a Quartz job for a specific calendar
     */
    private void createCalendarJob(Scheduler scheduler, WorkflowCalendar calendar) throws SchedulerException {
        String jobName = "calendarWorkflowJob_" + calendar.getCalendarId();
        String triggerName = "calendarWorkflowTrigger_" + calendar.getCalendarId();
        String groupName = "calendarWorkflows";
        
        // Create job detail with calendar ID in data map
        JobDetail jobDetail = JobBuilder.newJob(CalendarWorkflowExecutionJob.class)
                .withIdentity(jobName, groupName)
                .withDescription("Workflow execution job for calendar: " + calendar.getCalendarName())
                .usingJobData("calendarId", calendar.getCalendarId())
                .storeDurably()
                .build();
        
        // Create trigger based on calendar schedule
        Trigger trigger = createCalendarTrigger(triggerName, groupName, calendar);
        
        // Schedule the job
        scheduler.scheduleJob(jobDetail, trigger);
        
        logger.info("Created Quartz job for calendar: {} (ID: {})", calendar.getCalendarName(), calendar.getCalendarId());
    }
    
    /**
     * Create a trigger based on calendar configuration
     */
    private Trigger createCalendarTrigger(String triggerName, String groupName, WorkflowCalendar calendar) {
        String cronExpression = buildCronExpression(calendar);
        
        try {
            return TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, groupName)
                    .withDescription("Trigger for calendar: " + calendar.getCalendarName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .usingJobData("calendarId", calendar.getCalendarId())
                    .build();
                    
        } catch (ParseException e) {
            logger.error("Invalid cron expression for calendar {}: {}", calendar.getCalendarName(), cronExpression, e);
            // Fallback to daily at 9 AM
            try {
                return TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, groupName)
                        .withDescription("Fallback trigger for calendar: " + calendar.getCalendarName())
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ?"))
                        .usingJobData("calendarId", calendar.getCalendarId())
                        .build();
            } catch (ParseException fallbackException) {
                throw new RuntimeException("Failed to create fallback trigger", fallbackException);
            }
        }
    }
    
    /**
     * Build cron expression based on calendar configuration
     */
    private String buildCronExpression(WorkflowCalendar calendar) {
        // Default to daily at 9 AM
        String defaultCron = "0 0 9 * * ?";
        
        if (calendar == null) {
            return defaultCron;
        }
        
        // Build cron based on calendar properties
        // For now, use a simple daily schedule based on calendar start date
        // This can be enhanced based on calendar recurrence patterns
        
        if ("DAILY".equals(calendar.getRecurrence())) {
            // Daily at 9 AM
            return "0 0 9 * * ?";
        } else if ("WEEKLY".equals(calendar.getRecurrence())) {
            // Weekly on Monday at 9 AM
            return "0 0 9 ? * MON";
        } else if ("MONTHLY".equals(calendar.getRecurrence())) {
            // Monthly on the 1st at 9 AM
            return "0 0 9 1 * ?";
        } else if ("YEARLY".equals(calendar.getRecurrence())) {
            // Yearly on January 1st at 9 AM
            return "0 0 9 1 1 ?";
        }
        
        // Default to daily at 9 AM
        return defaultCron;
    }
    
    /**
     * Add a new calendar job dynamically
     */
    public void addCalendarJob(WorkflowCalendar calendar) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            createCalendarJob(scheduler, calendar);
            logger.info("Added new calendar job for: {}", calendar.getCalendarName());
        } catch (Exception e) {
            logger.error("Failed to add calendar job for: {}", calendar.getCalendarName(), e);
        }
    }
    
    /**
     * Remove a calendar job dynamically
     */
    public void removeCalendarJob(Long calendarId) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            String jobName = "calendarWorkflowJob_" + calendarId;
            String triggerName = "calendarWorkflowTrigger_" + calendarId;
            String groupName = "calendarWorkflows";
            
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, groupName));
            scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
            
            logger.info("Removed calendar job for calendar ID: {}", calendarId);
        } catch (Exception e) {
            logger.error("Failed to remove calendar job for calendar ID: {}", calendarId, e);
        }
    }
    
    /**
     * Update an existing calendar job
     */
    public void updateCalendarJob(WorkflowCalendar calendar) {
        try {
            // Remove old job and create new one
            removeCalendarJob(calendar.getCalendarId());
            addCalendarJob(calendar);
            logger.info("Updated calendar job for: {}", calendar.getCalendarName());
        } catch (Exception e) {
            logger.error("Failed to update calendar job for: {}", calendar.getCalendarName(), e);
        }
    }
}
