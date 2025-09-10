package com.docwf.service.impl;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.job.CalendarWorkflowExecutionJob;
import com.docwf.service.CalendarSchedulerService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Implementation of CalendarSchedulerService for managing calendar-based workflow scheduling
 */
@Service
public class CalendarSchedulerServiceImpl implements CalendarSchedulerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarSchedulerServiceImpl.class);
    
    @Autowired
    private Scheduler scheduler;
    
    private static final String JOB_GROUP = "calendarWorkflows";
    private static final String TRIGGER_GROUP = "calendarTriggers";
    
    @Override
    public boolean scheduleCalendarWorkflow(WorkflowCalendar calendar) {
        try {
            String jobName = "calendarWorkflow_" + calendar.getCalendarId();
            String triggerName = "calendarTrigger_" + calendar.getCalendarId();
            
            // Check if already scheduled
            if (isCalendarScheduled(calendar.getCalendarId())) {
                logger.info("Calendar {} is already scheduled, updating instead", calendar.getCalendarName());
                return updateCalendarWorkflowSchedule(calendar);
            }
            
            // Create job detail
            JobDetail jobDetail = JobBuilder.newJob(CalendarWorkflowExecutionJob.class)
                    .withIdentity(jobName, JOB_GROUP)
                    .withDescription("Workflow execution job for calendar: " + calendar.getCalendarName())
                    .usingJobData("calendarId", calendar.getCalendarId())
                    .usingJobData("calendarName", calendar.getCalendarName())
                    .storeDurably()
                    .build();
            
            // Create trigger based on calendar's cron expression
            Trigger trigger = createTriggerForCalendar(calendar, triggerName, jobDetail);
            
            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);
            
            logger.info("Successfully scheduled calendar workflow for: {} (ID: {})", 
                calendar.getCalendarName(), calendar.getCalendarId());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error scheduling calendar workflow for: {} (ID: {})", 
                calendar.getCalendarName(), calendar.getCalendarId(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateCalendarWorkflowSchedule(WorkflowCalendar calendar) {
        try {
            String jobName = "calendarWorkflow_" + calendar.getCalendarId();
            String triggerName = "calendarTrigger_" + calendar.getCalendarId();
            
            // Check if job exists
            JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP);
            if (!scheduler.checkExists(jobKey)) {
                logger.warn("Job not found for calendar: {} (ID: {}), creating new schedule", 
                    calendar.getCalendarName(), calendar.getCalendarId());
                return scheduleCalendarWorkflow(calendar);
            }
            
            // Delete existing trigger
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
            }
            
            // Create new trigger
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger newTrigger = createTriggerForCalendar(calendar, triggerName, jobDetail);
            
            // Schedule the new trigger
            scheduler.scheduleJob(newTrigger);
            
            logger.info("Successfully updated calendar workflow schedule for: {} (ID: {})", 
                calendar.getCalendarName(), calendar.getCalendarId());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error updating calendar workflow schedule for: {} (ID: {})", 
                calendar.getCalendarName(), calendar.getCalendarId(), e);
            return false;
        }
    }
    
    @Override
    public boolean unscheduleCalendarWorkflow(Long calendarId) {
        try {
            String jobName = "calendarWorkflow_" + calendarId;
            String triggerName = "calendarTrigger_" + calendarId;
            
            // Delete trigger
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
            }
            
            // Delete job
            JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP);
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
            
            logger.info("Successfully unscheduled calendar workflow for ID: {}", calendarId);
            return true;
            
        } catch (Exception e) {
            logger.error("Error unscheduling calendar workflow for ID: {}", calendarId, e);
            return false;
        }
    }
    
    @Override
    public boolean isCalendarScheduled(Long calendarId) {
        try {
            String jobName = "calendarWorkflow_" + calendarId;
            JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP);
            return scheduler.checkExists(jobKey);
        } catch (Exception e) {
            logger.error("Error checking if calendar is scheduled for ID: {}", calendarId, e);
            return false;
        }
    }
    
    @Override
    public Date getNextExecutionTime(Long calendarId) {
        try {
            String triggerName = "calendarTrigger_" + calendarId;
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP);
            
            if (scheduler.checkExists(triggerKey)) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                return trigger.getNextFireTime();
            }
            
            return null;
        } catch (Exception e) {
            logger.error("Error getting next execution time for calendar ID: {}", calendarId, e);
            return null;
        }
    }
    
    @Override
    public boolean pauseCalendarSchedule(Long calendarId) {
        try {
            String triggerName = "calendarTrigger_" + calendarId;
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP);
            
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
                logger.info("Successfully paused calendar schedule for ID: {}", calendarId);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error pausing calendar schedule for ID: {}", calendarId, e);
            return false;
        }
    }
    
    @Override
    public boolean resumeCalendarSchedule(Long calendarId) {
        try {
            String triggerName = "calendarTrigger_" + calendarId;
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP);
            
            if (scheduler.checkExists(triggerKey)) {
                scheduler.resumeTrigger(triggerKey);
                logger.info("Successfully resumed calendar schedule for ID: {}", calendarId);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error resuming calendar schedule for ID: {}", calendarId, e);
            return false;
        }
    }
    
    /**
     * Creates a trigger for a calendar based on its cron expression
     */
    private Trigger createTriggerForCalendar(WorkflowCalendar calendar, String triggerName, JobDetail jobDetail) {
        try {
            String cronExpression = calendar.getCronExpression();
            
            if (cronExpression == null || cronExpression.trim().isEmpty()) {
                // Default to daily at 9 AM if no cron expression is provided
                cronExpression = "0 0 9 * * ?";
                logger.warn("No cron expression provided for calendar: {}, using default: {}", 
                    calendar.getCalendarName(), cronExpression);
            }
            
            return TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, TRIGGER_GROUP)
                    .forJob(jobDetail)
                    .withDescription("Trigger for calendar: " + calendar.getCalendarName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                            .inTimeZone(java.util.TimeZone.getTimeZone(calendar.getTimezone() != null ? calendar.getTimezone() : "UTC")))
                    .startAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
                    
        } catch (Exception e) {
            logger.error("Error creating trigger for calendar: {} (ID: {})", 
                calendar.getCalendarName(), calendar.getCalendarId(), e);
            throw new RuntimeException("Failed to create trigger for calendar", e);
        }
    }
}
