package com.docwf.controller;

import com.docwf.config.CalendarSchedulerConfig;
import com.docwf.dto.QuartzJobStatusDto;
import com.docwf.service.WorkflowCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Scheduler Management Controller
 * Provides endpoints to manage Quartz scheduler operations
 */
@RestController
@RequestMapping("/api/scheduler")
@Tag(name = "Scheduler Management", description = "Manage Quartz scheduler operations and calendar-based workflows")
public class SchedulerManagementController {
    
    @Autowired
    private Scheduler scheduler;
    
    @Autowired
    private CalendarSchedulerConfig calendarSchedulerConfig;
    
    @Autowired
    private WorkflowCalendarService workflowCalendarService;
    
    @GetMapping("/status")
    @Operation(summary = "Get scheduler status", description = "Get the current status of the Quartz scheduler")
    public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("schedulerName", scheduler.getSchedulerName());
            status.put("schedulerInstanceId", scheduler.getSchedulerInstanceId());
            status.put("isStarted", scheduler.isStarted());
            status.put("isInStandbyMode", scheduler.isInStandbyMode());
            status.put("isShutdown", scheduler.isShutdown());
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/jobs")
    @Operation(summary = "Get all scheduled jobs", description = "Get a list of all scheduled jobs in the scheduler")
    public ResponseEntity<List<QuartzJobStatusDto>> getAllJobs() {
        try {
            List<QuartzJobStatusDto> jobs = new ArrayList<>();
            
            for (String groupName : scheduler.getJobGroupNames()) {
                Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(groupName));
                for (JobKey jobKey : jobKeys) {
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    
                    QuartzJobStatusDto jobStatus = new QuartzJobStatusDto();
                    jobStatus.setJobName(jobKey.getName());
                    jobStatus.setJobGroup(jobKey.getGroup());
                    jobStatus.setDescription(jobDetail.getDescription());
                    jobStatus.setJobClass(jobDetail.getJobClass().getSimpleName());
                    jobStatus.setDurable(jobDetail.isDurable());
                    jobStatus.setTriggerCount(triggers.size());
                    
                    if (!triggers.isEmpty()) {
                        Trigger trigger = triggers.get(0);
                        jobStatus.setNextFireTime(trigger.getNextFireTime());
                        jobStatus.setPreviousFireTime(trigger.getPreviousFireTime());
                        jobStatus.setTriggerState(scheduler.getTriggerState(trigger.getKey()).toString());
                    }
                    
                    jobs.add(jobStatus);
                }
            }
            
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        }
    }
    
    @GetMapping("/jobs/calendar/{calendarId}")
    @Operation(summary = "Get calendar job status", description = "Get the status of a specific calendar workflow job")
    public ResponseEntity<QuartzJobStatusDto> getCalendarJobStatus(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            String jobName = "calendar_" + calendarId;
            String jobGroup = "calendarWorkflows";
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            
            if (!scheduler.checkExists(jobKey)) {
                return ResponseEntity.notFound().build();
            }
            
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            
            QuartzJobStatusDto jobStatus = new QuartzJobStatusDto();
            jobStatus.setJobName(jobKey.getName());
            jobStatus.setJobGroup(jobKey.getGroup());
            jobStatus.setDescription(jobDetail.getDescription());
            jobStatus.setJobClass(jobDetail.getJobClass().getSimpleName());
            jobStatus.setDurable(jobDetail.isDurable());
            jobStatus.setTriggerCount(triggers.size());
            
            if (!triggers.isEmpty()) {
                Trigger trigger = triggers.get(0);
                jobStatus.setNextFireTime(trigger.getNextFireTime());
                jobStatus.setPreviousFireTime(trigger.getPreviousFireTime());
                jobStatus.setTriggerState(scheduler.getTriggerState(trigger.getKey()).toString());
            }
            
            return ResponseEntity.ok(jobStatus);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/calendar/{calendarId}/schedule")
    @Operation(summary = "Schedule calendar workflow", description = "Schedule a workflow based on calendar configuration")
    public ResponseEntity<Map<String, String>> scheduleCalendarWorkflow(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            var calendarDto = workflowCalendarService.getCalendarById(calendarId);
            if (calendarDto.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var calendar = workflowCalendarService.getCalendarEntityById(calendarId);
            if (calendar == null) {
                return ResponseEntity.notFound().build();
            }
            
            calendarSchedulerConfig.scheduleWorkflow(calendar);
            
            return ResponseEntity.ok(Map.of(
                "message", "Calendar workflow scheduled successfully",
                "calendarId", calendarId.toString(),
                "calendarName", calendar.getCalendarName()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/calendar/{calendarId}/unschedule")
    @Operation(summary = "Unschedule calendar workflow", description = "Remove a calendar workflow from the scheduler")
    public ResponseEntity<Map<String, String>> unscheduleCalendarWorkflow(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            calendarSchedulerConfig.unscheduleWorkflow(calendarId);
            
            return ResponseEntity.ok(Map.of(
                "message", "Calendar workflow unscheduled successfully",
                "calendarId", calendarId.toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/calendar/{calendarId}/update")
    @Operation(summary = "Update calendar schedule", description = "Update the schedule for a calendar workflow")
    public ResponseEntity<Map<String, String>> updateCalendarSchedule(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            var calendarDto = workflowCalendarService.getCalendarById(calendarId);
            if (calendarDto.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var calendar = workflowCalendarService.getCalendarEntityById(calendarId);
            if (calendar == null) {
                return ResponseEntity.notFound().build();
            }
            
            calendarSchedulerConfig.updateWorkflowSchedule(calendar);
            
            return ResponseEntity.ok(Map.of(
                "message", "Calendar schedule updated successfully",
                "calendarId", calendarId.toString(),
                "calendarName", calendar.getCalendarName()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/calendar/{calendarId}/next-execution")
    @Operation(summary = "Get next execution time", description = "Get the next scheduled execution time for a calendar")
    public ResponseEntity<Map<String, Object>> getNextExecutionTime(
            @Parameter(description = "Calendar ID") @PathVariable Long calendarId) {
        try {
            Date nextExecution = calendarSchedulerConfig.getNextExecutionTime(calendarId);
            String status = calendarSchedulerConfig.getExecutionStatus(calendarId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("calendarId", calendarId);
            response.put("nextExecutionTime", nextExecution);
            response.put("status", status);
            response.put("isScheduled", nextExecution != null);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/pause")
    @Operation(summary = "Pause scheduler", description = "Pause the Quartz scheduler")
    public ResponseEntity<Map<String, String>> pauseScheduler() {
        try {
            scheduler.standby();
            return ResponseEntity.ok(Map.of("message", "Scheduler paused successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/resume")
    @Operation(summary = "Resume scheduler", description = "Resume the Quartz scheduler")
    public ResponseEntity<Map<String, String>> resumeScheduler() {
        try {
            scheduler.start();
            return ResponseEntity.ok(Map.of("message", "Scheduler resumed successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/shutdown")
    @Operation(summary = "Shutdown scheduler", description = "Shutdown the Quartz scheduler")
    public ResponseEntity<Map<String, String>> shutdownScheduler() {
        try {
            scheduler.shutdown();
            return ResponseEntity.ok(Map.of("message", "Scheduler shutdown successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
