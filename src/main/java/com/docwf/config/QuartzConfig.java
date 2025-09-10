package com.docwf.config;

import com.docwf.job.WorkflowExecutionJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.text.ParseException;

/**
 * Quartz Configuration for Workflow Execution Jobs
 * Configures automated workflow execution based on calendar triggers
 */
@Configuration
public class QuartzConfig {
    
    /**
     * Define the WorkflowExecutionJob
     */
    @Bean
    public JobDetail workflowExecutionJobDetail() {
        return JobBuilder.newJob(WorkflowExecutionJob.class)
                .withIdentity("workflowExecutionJob", "workflowJobs")
                .withDescription("Job to execute workflow configurations based on calendar triggers")
                .storeDurably()
                .build();
    }
    
    /**
     * Define the trigger for WorkflowExecutionJob
     * This will run every 5 minutes to check for workflows that need execution
     */
    @Bean
    public Trigger workflowExecutionJobTrigger() {
        try {
            return TriggerBuilder.newTrigger()
                    .forJob(workflowExecutionJobDetail())
                    .withIdentity("workflowExecutionTrigger", "workflowTriggers")
                    .withDescription("Trigger for workflow execution job")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 */5 * * * ?")) // Every 5 minutes
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to create workflow execution trigger", e);
        }
    }
    
    /**
     * Alternative trigger for business hours only (9 AM to 5 PM, Monday to Friday)
     * Uncomment this and comment the above trigger if you want business hours only
     */
    /*
    @Bean
    public Trigger businessHoursWorkflowExecutionTrigger() {
        try {
            return TriggerBuilder.newTrigger()
                    .forJob(workflowExecutionJobDetail())
                    .withIdentity("businessHoursWorkflowExecutionTrigger", "workflowTriggers")
                    .withDescription("Trigger for workflow execution job during business hours only")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/15 9-17 ? * MON-FRI")) // Every 15 minutes, 9 AM to 5 PM, Mon-Fri
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to create business hours workflow execution trigger", e);
        }
    }
    */
    
    /**
     * Calendar-based trigger for specific workflow schedules
     * This can be customized based on individual workflow calendar requirements
     */
    @Bean
    public Trigger calendarBasedWorkflowExecutionTrigger() {
        try {
            return TriggerBuilder.newTrigger()
                    .forJob(workflowExecutionJobDetail())
                    .withIdentity("calendarBasedWorkflowExecutionTrigger", "workflowTriggers")
                    .withDescription("Calendar-based trigger for workflow execution")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ?")) // Daily at 9 AM
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to create calendar-based workflow execution trigger", e);
        }
    }
}
