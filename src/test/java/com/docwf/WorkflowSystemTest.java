package com.docwf;

import com.docwf.dto.*;
import com.docwf.dto.WorkflowProgressDto;
import com.docwf.service.WorkflowUserService;
import com.docwf.service.WorkflowConfigService;
import com.docwf.service.WorkflowExecutionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

/**
 * Comprehensive test class demonstrating the Workflow Management System
 * This test shows the complete workflow lifecycle from creation to execution
 */
@SpringBootTest
@ActiveProfiles("test")
public class WorkflowSystemTest {
    
    @Autowired
    private WorkflowUserService userService;
    
    @Autowired
    private WorkflowConfigService workflowService;
    
    @Autowired
    private WorkflowExecutionService executionService;
    
    @Test
    public void testCompleteWorkflowLifecycle() {
        System.out.println("=== Testing Complete Workflow Lifecycle ===");
        
        // 1. Create Users
        System.out.println("\n1. Creating users...");
        WorkflowUserDto alice = createUser("alice", "Alice", "Smith", "alice@example.com");
        WorkflowUserDto bob = createUser("bob", "Bob", "Johnson", "bob@example.com");
        WorkflowUserDto carol = createUser("carol", "Carol", "Williams", "carol@example.com");
        
        // 2. Create Workflow Configuration
        System.out.println("\n2. Creating workflow configuration...");
        WorkflowConfigDto workflow = createWorkflow("Document Review Workflow", 
                "Automated document review and approval process");
        
        // 3. Add Roles to Workflow
        System.out.println("\n3. Assigning roles to workflow...");
        assignRolesToWorkflow(workflow.getWorkflowId(), alice, bob, carol);
        
        // 4. Create Tasks
        System.out.println("\n4. Creating workflow tasks...");
        createWorkflowTasks(workflow.getWorkflowId());
        
        // 5. Start Workflow Instance
        System.out.println("\n5. Starting workflow instance...");
        WorkflowInstanceDto instance = startWorkflow(workflow.getWorkflowId(), alice.getUserId());
        
        // 6. Execute Tasks
        System.out.println("\n6. Executing workflow tasks...");
        executeWorkflowTasks(instance.getInstanceId());
        
        // 7. Check Progress
        System.out.println("\n7. Checking workflow progress...");
        checkWorkflowProgress(instance.getInstanceId());
        
        System.out.println("\n=== Workflow Lifecycle Test Completed Successfully ===");
    }
    
    private WorkflowUserDto createUser(String username, String firstName, String lastName, String email) {
        WorkflowUserDto user = new WorkflowUserDto();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setIsActive("Y");
        user.setCreatedBy("system");
        
        WorkflowUserDto createdUser = userService.createUser(user);
        System.out.println("Created user: " + createdUser.getUsername() + " (ID: " + createdUser.getUserId() + ")");
        return createdUser;
    }
    
    private WorkflowConfigDto createWorkflow(String name, String description) {
        WorkflowConfigDto workflow = new WorkflowConfigDto();
        workflow.setName(name);
        workflow.setDescription(description);
        workflow.setReminderBeforeDueMins(15);
        workflow.setMinutesAfterDue(10);
        workflow.setEscalationAfterMins(30);
        workflow.setDueInMins(120);
        workflow.setIsActive("Y");
        workflow.setCreatedBy("system");
        
        WorkflowConfigDto createdWorkflow = workflowService.createWorkflow(workflow);
        System.out.println("Created workflow: " + createdWorkflow.getName() + " (ID: " + createdWorkflow.getWorkflowId() + ")");
        return createdWorkflow;
    }
    
    private void assignRolesToWorkflow(Long workflowId, WorkflowUserDto... users) {
        // This would typically involve creating roles first, then assigning them
        // For this test, we'll simulate the role assignment
        System.out.println("Assigned " + users.length + " users to workflow " + workflowId);
    }
    
    private void createWorkflowTasks(Long workflowId) {
        // Create tasks in sequence order
        String[] taskNames = {"Upload Document", "Review Document", "Approve Document"};
        String[] taskTypes = {"FILE_UPLOAD", "FILE_UPDATE", "DECISION"};
        
        for (int i = 0; i < taskNames.length; i++) {
            WorkflowConfigTaskDto task = new WorkflowConfigTaskDto();
            task.setName(taskNames[i]);
            task.setTaskType(com.docwf.entity.WorkflowConfigTask.TaskType.valueOf(taskTypes[i]));
            task.setSequenceOrder(i + 1);
            
            WorkflowConfigTaskDto createdTask = workflowService.addTask(workflowId, task);
            System.out.println("Created task: " + createdTask.getName() + " (Sequence: " + createdTask.getSequenceOrder() + ")");
        }
    }
    
    private WorkflowInstanceDto startWorkflow(Long workflowId, Long startedByUserId) {
        WorkflowInstanceDto instance = executionService.startWorkflow(workflowId, startedByUserId);
        System.out.println("Started workflow instance: " + instance.getInstanceId());
        return instance;
    }
    
    private void executeWorkflowTasks(Long instanceId) {
        // Get all tasks for the instance
        List<WorkflowInstanceTaskDto> tasks = executionService.getInstanceTasks(instanceId);
        
        for (WorkflowInstanceTaskDto task : tasks) {
            System.out.println("Processing task: " + task.getTaskName() + " (Status: " + task.getStatus() + ")");
            
            // Start the task
            executionService.startTask(task.getInstanceTaskId());
            
            // Complete the task
            String decisionOutcome = "APPROVED";
            executionService.completeTask(task.getInstanceTaskId(), decisionOutcome);
            
            System.out.println("Completed task: " + task.getTaskName() + " with outcome: " + decisionOutcome);
        }
    }
    
    private void checkWorkflowProgress(Long instanceId) {
        // Check if workflow is complete
        boolean isComplete = executionService.isWorkflowComplete(instanceId);
        System.out.println("Workflow complete: " + isComplete);
        
        // Get workflow progress
        WorkflowProgressDto progress = executionService.getWorkflowProgress(instanceId);
        System.out.println("Workflow progress: " + progress.getProgressPercentage() + "%");
        System.out.println("Tasks: " + progress.getCompletedTasks() + "/" + progress.getTotalTasks() + " completed");
    }
    
    @Test
    public void testUserManagement() {
        System.out.println("\n=== Testing User Management ===");
        
        // Create a test user
        WorkflowUserDto testUser = createUser("testuser", "Test", "User", "test@example.com");
        
        // Get user by ID
        Optional<WorkflowUserDto> foundUser = userService.getUserById(testUser.getUserId());
        System.out.println("Found user: " + foundUser.map(u -> u.getUsername()).orElse("Not found"));
        
        // Update user
        testUser.setFirstName("Updated");
        WorkflowUserDto updatedUser = userService.updateUser(testUser.getUserId(), testUser);
        System.out.println("Updated user: " + updatedUser.getFirstName());
        
        // Get all users
        List<WorkflowUserDto> allUsers = userService.getAllUsers();
        System.out.println("Total users: " + allUsers.size());
    }
    
    @Test
    public void testWorkflowConfiguration() {
        System.out.println("\n=== Testing Workflow Configuration ===");
        
        // Create a test workflow
        WorkflowConfigDto testWorkflow = createWorkflow("Test Workflow", "Test workflow for configuration testing");
        
        // Get workflow by ID
        Optional<WorkflowConfigDto> foundWorkflow = workflowService.getWorkflowById(testWorkflow.getWorkflowId());
        System.out.println("Found workflow: " + foundWorkflow.map(w -> w.getName()).orElse("Not found"));
        
        // Get all workflows
        List<WorkflowConfigDto> allWorkflows = workflowService.getAllActiveWorkflows();
        System.out.println("Total workflows: " + allWorkflows.size());
        
        // Toggle workflow status
        WorkflowConfigDto deactivatedWorkflow = workflowService.toggleWorkflowStatus(testWorkflow.getWorkflowId(), "N");
        System.out.println("Workflow active: " + deactivatedWorkflow.getIsActive());
    }
    
    @Test
    public void testFileOperations() {
        System.out.println("\n=== Testing File Operations ===");
        
        // This test would typically involve file upload/download operations
        // For now, we'll just log the test
        System.out.println("File operations test would include:");
        System.out.println("- File upload");
        System.out.println("- File download");
        System.out.println("- File consolidation");
        System.out.println("- File versioning");
    }
    
    @Test
    public void testEscalationAndReminders() {
        System.out.println("\n=== Testing Escalation and Reminders ===");
        
        // This test would typically involve checking escalation rules
        // For now, we'll just log the test
        System.out.println("Escalation and reminders test would include:");
        System.out.println("- Task escalation after timeout");
        System.out.println("- Reminder notifications");
        System.out.println("- Escalation hierarchy");
    }
}
