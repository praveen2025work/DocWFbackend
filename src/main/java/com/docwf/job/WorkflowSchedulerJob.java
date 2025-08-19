package com.docwf.job;

import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WorkflowSchedulerJob {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowSchedulerJob.class);
    
    @Autowired
    private WorkflowExecutionService executionService;
    
    @Autowired
    private WorkflowConfigService configService;
    
    /**
     * Trigger workflow reminders every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void triggerWorkflowReminders() {
        try {
            logger.info("Starting workflow reminder job");
            executionService.triggerWorkflowReminders();
            logger.info("Completed workflow reminder job");
        } catch (Exception e) {
            logger.error("Error in workflow reminder job", e);
        }
    }
    
    /**
     * Trigger workflow escalations every 10 minutes
     */
    @Scheduled(fixedRate = 600000) // 10 minutes
    public void triggerWorkflowEscalations() {
        try {
            logger.info("Starting workflow escalation job");
            executionService.triggerWorkflowEscalations();
            logger.info("Completed workflow escalation job");
        } catch (Exception e) {
            logger.error("Error in workflow escalation job", e);
        }
    }
    
    /**
     * Check for overdue tasks every 15 minutes
     */
    @Scheduled(fixedRate = 900000) // 15 minutes
    public void checkOverdueTasks() {
        try {
            logger.info("Starting overdue task check job");
            var overdueTasks = executionService.getOverdueTasks();
            if (!overdueTasks.isEmpty()) {
                logger.info("Found {} overdue tasks", overdueTasks.size());
                // TODO: Send notifications for overdue tasks
            }
            logger.info("Completed overdue task check job");
        } catch (Exception e) {
            logger.error("Error in overdue task check job", e);
        }
    }
    
    /**
     * Check for tasks needing attention every 30 minutes
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes
    public void checkTasksNeedingAttention() {
        try {
            logger.info("Starting tasks needing attention check job");
            var attentionTasks = executionService.getTasksNeedingAttention();
            if (!attentionTasks.isEmpty()) {
                logger.info("Found {} tasks needing attention", attentionTasks.size());
                // TODO: Send notifications for tasks needing attention
            }
            logger.info("Completed tasks needing attention check job");
        } catch (Exception e) {
            logger.error("Error in tasks needing attention check job", e);
        }
    }
}
