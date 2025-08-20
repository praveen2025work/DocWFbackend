package com.docwf.controller;

import com.docwf.dto.WorkflowUserDto;
import com.docwf.service.WorkflowUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing workflow users with predicate-based search")
public class WorkflowUserController {
    
    @Autowired
    private WorkflowUserService userService;
    
    // ===== CORE CRUD OPERATIONS =====
    
    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new workflow user")
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
    
    // ===== PREDICATE-BASED SEARCH =====
    
    @GetMapping("/search")
    @Operation(summary = "Search users with predicates", description = "Search users using multiple criteria with pagination")
    public ResponseEntity<Page<WorkflowUserDto>> searchUsers(
            @Parameter(description = "Username (partial match)") @RequestParam(required = false) String username,
            @Parameter(description = "First name (partial match)") @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name (partial match)") @RequestParam(required = false) String lastName,
            @Parameter(description = "Email (partial match)") @RequestParam(required = false) String email,
            @Parameter(description = "Active status (Y/N)") @RequestParam(required = false) String isActive,
            @Parameter(description = "Role name") @RequestParam(required = false) String roleName,
            @Parameter(description = "Workflow ID") @RequestParam(required = false) Long workflowId,
            @Parameter(description = "Created after date (ISO format)") @RequestParam(required = false) String createdAfter,
            @Parameter(description = "Created before date (ISO format)") @RequestParam(required = false) String createdBefore,
            Pageable pageable) {
        
        Page<WorkflowUserDto> users = userService.searchUsers(
                username, firstName, lastName, email, isActive, roleName, workflowId, createdAfter, createdBefore, pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with optional filtering and pagination")
    public ResponseEntity<Page<WorkflowUserDto>> getAllUsers(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) String isActive,
            Pageable pageable) {
        Page<WorkflowUserDto> users = userService.getAllUsers(isActive, pageable);
        return ResponseEntity.ok(users);
    }
    
    // ===== UTILITY OPERATIONS =====
    
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
}
