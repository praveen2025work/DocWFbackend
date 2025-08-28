package com.docwf.job;

import com.docwf.dto.CreateWorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.entity.WorkflowConfig;
import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowConfigService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Calendar-Specific Quartz Job for executing workflow configurations
 * This job is triggered by specific calendar events and creates workflow instances
 * for workflows assigned to that particular calendar
 */
@Component
public class CalendarWorkflowExecutionJob implements Job {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarWorkflowExecutionJob.class);
    
    @Autowired
    private WorkflowExecutionService workflowExecutionService;
    
    @Autowired
    private WorkflowConfigService workflowConfigService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            // Extract calendar ID from job data map
            Long calendarId = extractCalendarId(context);
            if (calendarId == null) {
                logger.error("Calendar ID not found in job context");
                return;
            }
            
            logger.info("Starting CalendarWorkflowExecutionJob for calendar {} at {}", calendarId, LocalDateTime.now());
            
            // Get workflow configurations assigned to this specific calendar
            List<WorkflowConfig> calendarWorkflows = workflowConfigService.getWorkflowsByCalendarId(calendarId);
            
            if (calendarWorkflows.isEmpty()) {
                logger.info("No workflows found for calendar {}", calendarId);
                return;
            }
            
            // Execute workflows for this calendar
            for (WorkflowConfig workflowConfig : calendarWorkflows) {
                try {
                    executeWorkflowForCalendar(workflowConfig, calendarId);
                } catch (Exception e) {
                    logger.error("Failed to execute workflow {} for calendar {}", workflowConfig.getName(), calendarId, e);
                }
            }
            
            logger.info("Completed CalendarWorkflowExecutionJob for calendar {} at {}", calendarId, LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Error in CalendarWorkflowExecutionJob", e);
            throw new JobExecutionException(e);
        }
    }
    
    /**
     * Execute a single workflow for the specific calendar
     */
    private void executeWorkflowForCalendar(WorkflowConfig workflowConfig, Long calendarId) {
        logger.info("Executing workflow {} for calendar {}", workflowConfig.getName(), calendarId);
        
        // Create workflow instance using the existing service method
        CreateWorkflowInstanceDto createDto = new CreateWorkflowInstanceDto(
            workflowConfig.getWorkflowId(), 
            getSystemUserId(), 
            calendarId
        );
        
        WorkflowInstanceDto workflowInstance = workflowExecutionService.startWorkflowWithCalendar(createDto);
        
        logger.info("Successfully created workflow instance {} for calendar {}", workflowInstance.getInstanceId(), calendarId);
    }
    
    /**
     * Extract calendar ID from job context
     */
    private Long extractCalendarId(JobExecutionContext context) {
        try {
            // Try to get calendar ID from job data map
            Object calendarIdObj = context.getJobDetail().getJobDataMap().get("calendarId");
            if (calendarIdObj instanceof Long) {
                return (Long) calendarIdObj;
            } else if (calendarIdObj instanceof String) {
                return Long.parseLong((String) calendarIdObj);
            } else if (calendarIdObj instanceof Integer) {
                return ((Integer) calendarIdObj).longValue();
            }
            
            // Fallback: try to get from trigger data map
            Object triggerCalendarId = context.getTrigger().getJobDataMap().get("calendarId");
            if (triggerCalendarId instanceof Long) {
                return (Long) triggerCalendarId;
            }
            
            logger.warn("Calendar ID not found in job or trigger context");
            return null;
            
        } catch (Exception e) {
            logger.error("Error extracting calendar ID from job context", e);
            return null;
        }
    }
    
    /**
     * Get system user ID for automated workflow creation
     */
    private Long getSystemUserId() {
        // This should be configured or retrieved from system configuration
        return 1L; // System user ID
    }
}
