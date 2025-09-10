package com.docwf.config;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.job.CalendarWorkflowExecutionJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Calendar-based Scheduler Configuration
 * Manages Quartz triggers based on workflow calendar schedules
 */
@Component
public class CalendarSchedulerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarSchedulerConfig.class);
    
    @Autowired
    private Scheduler scheduler;
    
    /**
     * Schedule a workflow based on calendar configuration
     */
    public void scheduleWorkflow(WorkflowCalendar calendar) {
        try {
            if (calendar.getCronExpression() == null || calendar.getCronExpression().trim().isEmpty()) {
                logger.warn("No cron expression found for calendar: {}", calendar.getCalendarName());
                return;
            }
            
            // Create job detail for this specific calendar
            JobDetail jobDetail = createCalendarJobDetail(calendar);
            
            // Create trigger based on cron expression
            Trigger trigger = createCalendarTrigger(calendar, jobDetail);
            
            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);
            
            logger.info("Scheduled workflow for calendar: {} with cron: {}", 
                       calendar.getCalendarName(), calendar.getCronExpression());
            
        } catch (Exception e) {
            logger.error("Failed to schedule workflow for calendar: {}", calendar.getCalendarName(), e);
        }
    }
    
    /**
     * Update an existing workflow schedule
     */
    public void updateWorkflowSchedule(WorkflowCalendar calendar) {
        try {
            // First unschedule the existing job
            unscheduleWorkflow(calendar.getCalendarId());
            
            // Then schedule the updated version
            scheduleWorkflow(calendar);
            
            logger.info("Updated schedule for calendar: {}", calendar.getCalendarName());
            
        } catch (Exception e) {
            logger.error("Failed to update schedule for calendar: {}", calendar.getCalendarName(), e);
        }
    }
    
    /**
     * Unschedule a workflow
     */
    public void unscheduleWorkflow(Long calendarId) {
        try {
            String jobName = "calendar_" + calendarId;
            String jobGroup = "calendarWorkflows";
            
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                logger.info("Unscheduled workflow for calendar ID: {}", calendarId);
            }
            
        } catch (Exception e) {
            logger.error("Failed to unschedule workflow for calendar ID: {}", calendarId, e);
        }
    }
    
    /**
     * Create job detail for calendar-based workflow execution
     */
    private JobDetail createCalendarJobDetail(WorkflowCalendar calendar) {
        String jobName = "calendar_" + calendar.getCalendarId();
        String jobGroup = "calendarWorkflows";
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("calendarId", calendar.getCalendarId());
        jobDataMap.put("calendarName", calendar.getCalendarName());
        jobDataMap.put("workflowId", calendar.getWorkflowId());
        
        return JobBuilder.newJob(CalendarWorkflowExecutionJob.class)
                .withIdentity(jobName, jobGroup)
                .withDescription("Calendar-based workflow execution for: " + calendar.getCalendarName())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    
    /**
     * Create trigger based on calendar cron expression
     */
    private Trigger createCalendarTrigger(WorkflowCalendar calendar, JobDetail jobDetail) throws ParseException {
        String triggerName = "trigger_" + calendar.getCalendarId();
        String triggerGroup = "calendarTriggers";
        
        // Parse cron expression and create trigger
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(calendar.getCronExpression());
        
        // Set timezone if specified
        if (calendar.getTimezone() != null && !calendar.getTimezone().trim().isEmpty()) {
            cronSchedule.inTimeZone(java.util.TimeZone.getTimeZone(calendar.getTimezone()));
        }
        
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName, triggerGroup)
                .withDescription("Trigger for calendar: " + calendar.getCalendarName())
                .withSchedule(cronSchedule)
                .startNow()
                .build();
    }
    
    /**
     * Check if a calendar is already scheduled
     */
    public boolean isCalendarScheduled(Long calendarId) {
        try {
            String jobName = "calendar_" + calendarId;
            String jobGroup = "calendarWorkflows";
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            return scheduler.checkExists(jobKey);
        } catch (Exception e) {
            logger.error("Failed to check if calendar is scheduled: {}", calendarId, e);
            return false;
        }
    }
    
    /**
     * Get next execution time for a calendar
     */
    public Date getNextExecutionTime(Long calendarId) {
        try {
            String jobName = "calendar_" + calendarId;
            String jobGroup = "calendarWorkflows";
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + calendarId, "calendarTriggers");
                Trigger trigger = scheduler.getTrigger(triggerKey);
                if (trigger != null) {
                    return trigger.getNextFireTime();
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get next execution time for calendar: {}", calendarId, e);
        }
        return null;
    }
    
    /**
     * Get execution status for a calendar
     */
    public String getExecutionStatus(Long calendarId) {
        try {
            String jobName = "calendar_" + calendarId;
            String jobGroup = "calendarWorkflows";
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + calendarId, "calendarTriggers");
                Trigger trigger = scheduler.getTrigger(triggerKey);
                if (trigger != null) {
                    return scheduler.getTriggerState(triggerKey).toString();
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get execution status for calendar: {}", calendarId, e);
        }
        return "UNKNOWN";
    }
}
