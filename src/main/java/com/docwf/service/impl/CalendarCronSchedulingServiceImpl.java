package com.docwf.service.impl;

import com.docwf.dto.WorkflowCalendarDto;
import com.docwf.entity.WorkflowCalendar;
import com.docwf.job.CalendarCronExecutionJob;
import com.docwf.service.CalendarCronSchedulingService;
import com.docwf.service.WorkflowCalendarService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Implementation of CalendarCronSchedulingService
 */
@Service
public class CalendarCronSchedulingServiceImpl implements CalendarCronSchedulingService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarCronSchedulingServiceImpl.class);
    
    @Autowired
    private Scheduler scheduler;
    
    @Autowired
    private WorkflowCalendarService calendarService;
    
    private static final String JOB_GROUP = "CALENDAR_CRON_JOBS";
    private static final String TRIGGER_GROUP = "CALENDAR_CRON_TRIGGERS";
    
    @Override
    public void scheduleCalendar(Long calendarId) throws SchedulerException {
        logger.info("Scheduling calendar {} for cron execution", calendarId);
        
        WorkflowCalendar calendar = calendarService.getCalendarEntityById(calendarId);
        if (calendar == null) {
            throw new SchedulerException("Calendar not found for ID: " + calendarId);
        }
        
        if (!calendar.isActive()) {
            throw new SchedulerException("Calendar " + calendarId + " is not active");
        }
        
        if (!calendar.hasCronScheduling()) {
            throw new SchedulerException("Calendar " + calendarId + " does not have cron expression");
        }
        
        // Create job key
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        
        // Create job detail
        JobDetail jobDetail = JobBuilder.newJob(CalendarCronExecutionJob.class)
                .withIdentity(jobKey)
                .withDescription("Cron execution job for calendar: " + calendar.getCalendarName())
                .usingJobData("calendarId", calendarId)
                .usingJobData("region", calendar.getRegion())
                .build();
        
        // Create trigger
        CronTrigger trigger;
        try {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_" + calendarId, TRIGGER_GROUP)
                    .withDescription("Cron trigger for calendar: " + calendar.getCalendarName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(calendar.getCronExpression())
                            .inTimeZone(TimeZone.getTimeZone(calendar.getEffectiveTimezone())))
                    .build();
        } catch (Exception e) {
            throw new SchedulerException("Invalid cron expression: " + calendar.getCronExpression(), e);
        }
        
        // Schedule the job
        scheduler.scheduleJob(jobDetail, trigger);
        
        logger.info("Successfully scheduled calendar {} with cron expression: {}", 
            calendarId, calendar.getCronExpression());
    }
    
    @Override
    public void unscheduleCalendar(Long calendarId) throws SchedulerException {
        logger.info("Unscheduling calendar {} from cron execution", calendarId);
        
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + calendarId, TRIGGER_GROUP);
        
        // Delete trigger first
        if (scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey);
        }
        
        // Delete job
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        
        logger.info("Successfully unscheduled calendar {}", calendarId);
    }
    
    @Override
    public void rescheduleCalendar(Long calendarId) throws SchedulerException {
        logger.info("Rescheduling calendar {} with updated cron expression", calendarId);
        
        // First unschedule the existing job
        unscheduleCalendar(calendarId);
        
        // Then schedule it again with updated configuration
        scheduleCalendar(calendarId);
        
        logger.info("Successfully rescheduled calendar {}", calendarId);
    }
    
    @Override
    public void scheduleAllActiveCalendars() throws SchedulerException {
        logger.info("Scheduling all active calendars with cron expressions");
        
        List<WorkflowCalendarDto> activeCalendars = calendarService.getActiveCalendarsWithCron();
        
        for (WorkflowCalendarDto calendar : activeCalendars) {
            try {
                scheduleCalendar(calendar.getCalendarId());
                logger.info("Scheduled calendar: {} ({})", calendar.getCalendarName(), calendar.getCalendarId());
            } catch (Exception e) {
                logger.error("Failed to schedule calendar: {} ({})", calendar.getCalendarName(), calendar.getCalendarId(), e);
                // Continue with other calendars
            }
        }
        
        logger.info("Completed scheduling all active calendars");
    }
    
    @Override
    public List<Long> getScheduledCalendarIds() throws SchedulerException {
        List<Long> scheduledIds = new ArrayList<>();
        
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(JOB_GROUP));
        
        for (JobKey jobKey : jobKeys) {
            String jobName = jobKey.getName();
            if (jobName.startsWith("calendar_")) {
                try {
                    Long calendarId = Long.parseLong(jobName.substring("calendar_".length()));
                    scheduledIds.add(calendarId);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid job name format: {}", jobName);
                }
            }
        }
        
        return scheduledIds;
    }
    
    @Override
    public boolean isCalendarScheduled(Long calendarId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        return scheduler.checkExists(jobKey);
    }
    
    @Override
    public String getNextExecutionTime(Long calendarId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + calendarId, TRIGGER_GROUP);
        
        if (!scheduler.checkExists(triggerKey)) {
            return null;
        }
        
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            Date nextFireTime = trigger.getNextFireTime();
            return nextFireTime != null ? nextFireTime.toString() : null;
        }
        
        return null;
    }
    
    @Override
    public void pauseCalendar(Long calendarId) throws SchedulerException {
        logger.info("Pausing calendar {} execution", calendarId);
        
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
            logger.info("Successfully paused calendar {}", calendarId);
        } else {
            logger.warn("Calendar {} is not scheduled", calendarId);
        }
    }
    
    @Override
    public void resumeCalendar(Long calendarId) throws SchedulerException {
        logger.info("Resuming calendar {} execution", calendarId);
        
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        
        if (scheduler.checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
            logger.info("Successfully resumed calendar {}", calendarId);
        } else {
            logger.warn("Calendar {} is not scheduled", calendarId);
        }
    }
    
    @Override
    public String getCalendarExecutionStatus(Long calendarId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("calendar_" + calendarId, JOB_GROUP);
        
        if (!scheduler.checkExists(jobKey)) {
            return "NOT_SCHEDULED";
        }
        
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return "NOT_SCHEDULED";
            }
            
            // Check if job is paused
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (triggers.isEmpty()) {
                return "NOT_SCHEDULED";
            }
            
            // Check trigger state
            for (Trigger trigger : triggers) {
                Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                switch (state) {
                    case NORMAL:
                        return "SCHEDULED";
                    case PAUSED:
                        return "PAUSED";
                    case ERROR:
                        return "ERROR";
                    case BLOCKED:
                        return "BLOCKED";
                    case COMPLETE:
                        return "COMPLETE";
                    default:
                        return "UNKNOWN";
                }
            }
            
            return "UNKNOWN";
            
        } catch (Exception e) {
            logger.error("Error getting execution status for calendar {}", calendarId, e);
            return "ERROR";
        }
    }
}
