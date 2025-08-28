package com.docwf.service.impl;

import com.docwf.config.CalendarQuartzConfig;
import com.docwf.entity.WorkflowCalendar;
import com.docwf.service.CalendarJobManagementService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of CalendarJobManagementService
 * Manages calendar-specific Quartz jobs dynamically
 */
@Service
public class CalendarJobManagementServiceImpl implements CalendarJobManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarJobManagementServiceImpl.class);
    
    @Autowired
    private CalendarQuartzConfig calendarQuartzConfig;
    
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    
    @Override
    public void addCalendarJob(WorkflowCalendar calendar) {
        try {
            calendarQuartzConfig.addCalendarJob(calendar);
            logger.info("Successfully added calendar job for calendar: {}", calendar.getCalendarName());
        } catch (Exception e) {
            logger.error("Failed to add calendar job for calendar: {}", calendar.getCalendarName(), e);
            throw new RuntimeException("Failed to add calendar job", e);
        }
    }
    
    @Override
    public void removeCalendarJob(Long calendarId) {
        try {
            calendarQuartzConfig.removeCalendarJob(calendarId);
            logger.info("Successfully removed calendar job for calendar ID: {}", calendarId);
        } catch (Exception e) {
            logger.error("Failed to remove calendar job for calendar ID: {}", calendarId, e);
            throw new RuntimeException("Failed to remove calendar job", e);
        }
    }
    
    @Override
    public void updateCalendarJob(WorkflowCalendar calendar) {
        try {
            calendarQuartzConfig.updateCalendarJob(calendar);
            logger.info("Successfully updated calendar job for calendar: {}", calendar.getCalendarName());
        } catch (Exception e) {
            logger.error("Failed to update calendar job for calendar: {}", calendar.getCalendarName(), e);
            throw new RuntimeException("Failed to update calendar job", e);
        }
    }
    
    @Override
    public void initializeAllCalendarJobs() {
        try {
            calendarQuartzConfig.initializeCalendarJobs();
            logger.info("Successfully initialized all calendar jobs");
        } catch (Exception e) {
            logger.error("Failed to initialize calendar jobs", e);
            throw new RuntimeException("Failed to initialize calendar jobs", e);
        }
    }
    
    @Override
    public String getCalendarJobStatus(Long calendarId) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            String jobName = "calendarWorkflowJob_" + calendarId;
            String groupName = "calendarWorkflows";
            
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            
            if (scheduler.checkExists(jobKey)) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (!triggers.isEmpty()) {
                    Trigger.TriggerState state = scheduler.getTriggerState(triggers.get(0).getKey());
                    return state.name();
                }
            }
            
            return "NOT_FOUND";
            
        } catch (Exception e) {
            logger.error("Failed to get calendar job status for calendar ID: {}", calendarId, e);
            return "ERROR";
        }
    }
    
    @Override
    public void pauseCalendarJob(Long calendarId) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            String jobName = "calendarWorkflowJob_" + calendarId;
            String groupName = "calendarWorkflows";
            
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            scheduler.pauseJob(jobKey);
            
            logger.info("Successfully paused calendar job for calendar ID: {}", calendarId);
            
        } catch (Exception e) {
            logger.error("Failed to pause calendar job for calendar ID: {}", calendarId, e);
            throw new RuntimeException("Failed to pause calendar job", e);
        }
    }
    
    @Override
    public void resumeCalendarJob(Long calendarId) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            String jobName = "calendarWorkflowJob_" + calendarId;
            String groupName = "calendarWorkflows";
            
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            scheduler.resumeJob(jobKey);
            
            logger.info("Successfully resumed calendar job for calendar ID: {}", calendarId);
            
        } catch (Exception e) {
            logger.error("Failed to resume calendar job for calendar ID: {}", calendarId, e);
            throw new RuntimeException("Failed to resume calendar job", e);
        }
    }
    
    @Override
    public List<String> getActiveCalendarJobNames() {
        List<String> activeJobNames = new ArrayList<>();
        
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            String groupName = "calendarWorkflows";
            
            // Simplified approach - return empty list for now
            // In production, you would implement a proper job registry or use a different method
            logger.info("Getting active calendar job names for group: {}", groupName);
            
            // TODO: Implement proper job enumeration
            // This could be done by maintaining a registry of created jobs
            // or using a different Quartz method to enumerate jobs
            
        } catch (Exception e) {
            logger.error("Failed to get active calendar job names", e);
        }
        
        return activeJobNames;
    }
}
