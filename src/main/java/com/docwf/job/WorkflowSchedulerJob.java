package com.docwf.job;

import com.docwf.service.WorkflowExecutionService;
import com.docwf.service.WorkflowConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.docwf.dto.WorkflowInstanceTaskDto;

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
                // Send notifications for overdue tasks
                for (var task : overdueTasks) {
                    sendOverdueTaskNotification(task);
                }
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
                // Send notifications for tasks needing attention
                for (var task : attentionTasks) {
                    sendAttentionTaskNotification(task);
                }
            }
            logger.info("Completed tasks needing attention check job");
        } catch (Exception e) {
            logger.error("Error in tasks needing attention check job", e);
        }
    }
    
    /**
     * Send notification for overdue task
     */
    private void sendOverdueTaskNotification(WorkflowInstanceTaskDto task) {
        try {
            logger.info("Sending overdue notification for task {} (instance: {})", 
                task.getTaskName(), task.getInstanceId());
            
            // TODO: Integrate with actual notification service
            // notificationService.sendOverdueTaskNotification(task);
            
        } catch (Exception e) {
            logger.error("Error sending overdue task notification for task {}", task.getTaskId(), e);
        }
    }
    
    /**
     * Send notification for task needing attention
     */
    private void sendAttentionTaskNotification(WorkflowInstanceTaskDto task) {
        try {
            logger.info("Sending attention notification for task {} (instance: {})", 
                task.getTaskName(), task.getInstanceId());
            
            // TODO: Integrate with actual notification service
            // notificationService.sendAttentionTaskNotification(task);
            
        } catch (Exception e) {
            logger.error("Error sending attention task notification for task {}", task.getTaskId(), e);
        }
    }
}
