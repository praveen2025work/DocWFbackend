package com.docwf.job;

import com.docwf.dto.CreateWorkflowInstanceDto;
import com.docwf.dto.WorkflowInstanceDto;
import com.docwf.entity.WorkflowConfig;
import com.docwf.entity.WorkflowInstance;
import com.docwf.entity.WorkflowInstanceTask;
import com.docwf.entity.WorkflowInstanceTaskFile;
import com.docwf.entity.WorkflowUser;
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
 * Quartz Job for executing workflow configurations
 * This job is triggered by calendar events and creates workflow instances
 */
@Component
public class WorkflowExecutionJob implements Job {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutionJob.class);
    
    @Autowired
    private WorkflowExecutionService workflowExecutionService;
    
    @Autowired
    private WorkflowConfigService workflowConfigService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("Starting WorkflowExecutionJob at {}", LocalDateTime.now());
            
            // Get workflow configurations that should be executed based on calendar
            List<WorkflowConfig> activeWorkflows = workflowConfigService.getActiveWorkflowEntitiesForExecution();
            
            for (WorkflowConfig workflowConfig : activeWorkflows) {
                try {
                    executeWorkflow(workflowConfig);
                } catch (Exception e) {
                    logger.error("Failed to execute workflow: {}", workflowConfig.getName(), e);
                }
            }
            
            logger.info("Completed WorkflowExecutionJob at {}", LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Error in WorkflowExecutionJob", e);
            throw new JobExecutionException(e);
        }
    }
    
    /**
     * Execute a single workflow configuration
     */
    private void executeWorkflow(WorkflowConfig workflowConfig) {
        logger.info("Executing workflow: {}", workflowConfig.getName());
        
        // Create workflow instance using the existing service method
        CreateWorkflowInstanceDto createDto = new CreateWorkflowInstanceDto(
            workflowConfig.getWorkflowId(), 
            getSystemUserId(), 
            workflowConfig.getCalendarId()
        );
        
        WorkflowInstanceDto workflowInstance = workflowExecutionService.startWorkflowWithCalendar(createDto);
        
        logger.info("Successfully created workflow instance: {}", workflowInstance.getInstanceId());
    }
    
    /**
     * Get system user ID for automated workflow creation
     */
    private Long getSystemUserId() {
        // This should be configured or retrieved from system configuration
        return 1L; // System user ID
    }
}
