package com.docwf.job;

import com.docwf.entity.WorkflowCalendar;
import com.docwf.entity.WorkflowConfig;
import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowCalendarService;
import com.docwf.service.WorkflowConfigService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Job that executes workflows based on calendar schedules
 * This job runs at scheduled times and creates workflow instances from configurations
 */
@Component
public class CalendarWorkflowExecutionJob implements Job {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarWorkflowExecutionJob.class);
    
    @Autowired
    private WorkflowExecutionService workflowExecutionService;
    
    @Autowired
    private WorkflowCalendarService workflowCalendarService;
    
    @Autowired
    private WorkflowConfigService workflowConfigService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Starting Calendar Workflow Execution Job at {}", LocalDateTime.now());
        
        try {
            // Get calendar ID from job data
            Long calendarId = context.getJobDetail().getJobDataMap().getLongValue("calendarId");
            String calendarName = context.getJobDetail().getJobDataMap().getString("calendarName");
            
            logger.info("Executing workflows for calendar: {} (ID: {})", calendarName, calendarId);
            
            // Check if today is a valid execution day for this calendar
            LocalDate today = LocalDate.now();
            if (!workflowCalendarService.isDateValid(calendarId, today)) {
                logger.info("Today ({}) is not a valid execution day for calendar: {}", today, calendarName);
                return;
            }
            
            // Get all active workflows that use this calendar
            List<WorkflowConfig> workflows = workflowConfigService.getWorkflowsByCalendarId(calendarId);
            
            if (workflows.isEmpty()) {
                logger.info("No active workflows found for calendar: {}", calendarName);
                return;
            }
            
            int executedCount = 0;
            int skippedCount = 0;
            
            for (WorkflowConfig workflow : workflows) {
                try {
                    // Check if workflow should be executed based on its trigger type and schedule
                    if (shouldExecuteWorkflow(workflow, today)) {
                        // Create workflow instance
                        Long instanceId = workflowExecutionService.startWorkflowWithCalendar(
                            workflow.getWorkflowId(), 
                            1L, // System user ID - you might want to make this configurable
                            calendarId
                        ).getInstanceId();
                        
                        logger.info("Created workflow instance {} for workflow: {} (ID: {})", 
                            instanceId, workflow.getName(), workflow.getWorkflowId());
                        executedCount++;
                    } else {
                        logger.debug("Skipping workflow: {} - not scheduled for execution", workflow.getName());
                        skippedCount++;
                    }
                } catch (Exception e) {
                    logger.error("Error executing workflow: {} (ID: {})", workflow.getName(), workflow.getWorkflowId(), e);
                }
            }
            
            logger.info("Calendar Workflow Execution Job completed. Executed: {}, Skipped: {}", executedCount, skippedCount);
            
        } catch (Exception e) {
            logger.error("Error in Calendar Workflow Execution Job", e);
            throw new JobExecutionException("Failed to execute calendar workflow job", e);
        }
    }
    
    /**
     * Determines if a workflow should be executed based on its configuration and current date
     */
    private boolean shouldExecuteWorkflow(WorkflowConfig workflow, LocalDate today) {
        // Check if workflow is active
        if (!"Y".equals(workflow.getIsActive())) {
            return false;
        }
        
        // For now, we'll execute all active workflows
        // You can add more sophisticated logic here based on workflow parameters
        // For example, check if the workflow has specific execution criteria
        
        // Check if workflow has been executed recently (within the same day)
        // This prevents duplicate executions
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        // You could add a check here to see if the workflow was already executed today
        // For now, we'll allow execution
        
        return true;
    }
}