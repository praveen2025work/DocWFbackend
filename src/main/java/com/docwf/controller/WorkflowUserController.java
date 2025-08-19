package com.docwf.controller;

import com.docwf.dto.WorkflowUserDto;
import com.docwf.service.WorkflowUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Workflow User Management", description = "APIs for managing workflow users")
public class WorkflowUserController {
    
    @Autowired
    private WorkflowUserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new workflow user with the provided details")
    public ResponseEntity<WorkflowUserDto> createUser(
            @Valid @RequestBody WorkflowUserDto userDto) {
        WorkflowUserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    public ResponseEntity<WorkflowUserDto> getUserById(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        Optional<WorkflowUserDto> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    public ResponseEntity<WorkflowUserDto> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        Optional<WorkflowUserDto> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    public ResponseEntity<WorkflowUserDto> getUserByEmail(
            @Parameter(description = "Email address") @PathVariable String email) {
        Optional<WorkflowUserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with optional filtering")
    public ResponseEntity<List<WorkflowUserDto>> getAllUsers(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) String isActive) {
        List<WorkflowUserDto> users;
        if (isActive != null) {
            users = userService.getAllActiveUsers();
        } else {
            users = userService.getAllUsers();
        }
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    public ResponseEntity<WorkflowUserDto> updateUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody WorkflowUserDto userDto) {
        WorkflowUserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Deletes a user from the system")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/role/{roleName}")
    @Operation(summary = "Get users by role", description = "Retrieves all users assigned to a specific role")
    public ResponseEntity<List<WorkflowUserDto>> getUsersByRole(
            @Parameter(description = "Role name") @PathVariable String roleName) {
        List<WorkflowUserDto> users = userService.getUsersByRoleName(roleName);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/workflow/{workflowId}")
    @Operation(summary = "Get users by workflow", description = "Retrieves all users assigned to a specific workflow")
    public ResponseEntity<List<WorkflowUserDto>> getUsersByWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable Long workflowId) {
        List<WorkflowUserDto> users = userService.getUsersByWorkflowId(workflowId);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{userId}/escalation-hierarchy")
    @Operation(summary = "Get escalation hierarchy", description = "Retrieves the escalation hierarchy for a user")
    public ResponseEntity<List<WorkflowUserDto>> getEscalationHierarchy(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<WorkflowUserDto> hierarchy = userService.getEscalationHierarchy(userId);
        return ResponseEntity.ok(hierarchy);
    }
    
    @GetMapping("/check/username/{username}")
    @Operation(summary = "Check username availability", description = "Checks if a username is already taken")
    public ResponseEntity<Boolean> checkUsernameExists(
            @Parameter(description = "Username to check") @PathVariable String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/email/{email}")
    @Operation(summary = "Check email availability", description = "Checks if an email is already registered")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email to check") @PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
    
    @PatchMapping("/{userId}/status")
    @Operation(summary = "Toggle user status", description = "Activates or deactivates a user")
    public ResponseEntity<WorkflowUserDto> toggleUserStatus(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Active status (Y/N)") @RequestParam String isActive) {
        WorkflowUserDto user = userService.toggleUserStatus(userId, isActive);
        return ResponseEntity.ok(user);
    }
    
    @PatchMapping("/{userId}/escalation")
    @Operation(summary = "Set user escalation", description = "Sets the escalation user for a specific user")
    public ResponseEntity<WorkflowUserDto> setEscalation(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Escalation user ID") @RequestParam(required = false) Long escalationToUserId) {
        WorkflowUserDto user = userService.setEscalation(userId, escalationToUserId);
        return ResponseEntity.ok(user);
    }
}
